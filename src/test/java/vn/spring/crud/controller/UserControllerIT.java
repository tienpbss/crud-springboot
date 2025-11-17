package vn.spring.crud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import vn.spring.crud.entity.User;
import vn.spring.crud.repository.UserRepository;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class UserControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    private User testUser;
    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        testUser = new User();
        testUser.setUsername("test");
        testUser.setEmail("test@gg.com");
    }

    @Test
    public void createUser_Success() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.username").value("test"))
                .andExpect(jsonPath("$.email").value("test@gg.com"));
    }

    @Test
    public void createUser_EmailAlreadyExist_BadRequest() throws Exception {
        this.userRepository.saveAndFlush(testUser);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllUsers_Success() throws Exception {
        this.userRepository.save(testUser);
        this.userRepository.save(new User("jane", "jane@gg.com"));
        this.userRepository.flush();
        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getUserById_Success() throws Exception {
        this.userRepository.save(testUser);
        mockMvc.perform(get("/users/{id}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(testUser.getUsername()))
                .andExpect(jsonPath("$.email").value(testUser.getEmail()));
    }

    @Test
    public void getUserById_NotFound() throws Exception {
        mockMvc.perform(get("/users/{id}", -1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateUser_Success() throws Exception {
        User savedUser = userRepository.save(testUser);
        User userDetails = new User("jane", "jane@gmail.com");
        mockMvc.perform(put("/users/{id}", savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.username").value(userDetails.getUsername()))
                .andExpect(jsonPath("$.email").value(userDetails.getEmail()));
    }

    @Test
    public void updateUser_UserNotFound() throws Exception {
        User userDetails = new User("jane", "jane@gmail.com");
        mockMvc.perform(put("/users/{id}", -1, userDetails)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDetails)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateUser_EmailAlreadyExist_BadRequest() throws Exception {
        User alreadyExistUser = userRepository.save(new User("jane", "jane@gg.com"));
        User savedUser = userRepository.save(testUser);
        User userDetails = new User(alreadyExistUser.getUsername(), alreadyExistUser.getEmail());
        mockMvc.perform(put("/users/{id}", savedUser.getId(), userDetails)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDetails)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteUser_Success() throws Exception {
        User savedUser = userRepository.save(testUser);
        mockMvc.perform(delete("/users/{id}", savedUser.getId()))
                .andExpect(status().isNoContent());

    }
    @Test
    public void deleteUser_NotFound() throws Exception {
        mockMvc.perform(delete("/users/{id}", -1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void crudFullFlow_Success() throws Exception {
        String userString = mockMvc.perform(post("/users", testUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.username").value(testUser.getUsername()))
                .andExpect(jsonPath("$.email").value(testUser.getEmail()))
                .andReturn().getResponse().getContentAsString();

        User userObject = objectMapper.readValue(userString, User.class);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(get("/users/{id}", userObject.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(testUser.getUsername()))
                .andExpect(jsonPath("$.email").value(testUser.getEmail()));

        User userDetails = new User("jane", "jane@gg.com");
        mockMvc.perform(put("/users/{id}", userObject.getId(), userDetails)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userObject.getId()))
                .andExpect(jsonPath("$.username").value(userDetails.getUsername()))
                .andExpect(jsonPath("$.email").value(userDetails.getEmail()));

        mockMvc.perform(delete("/users/{id}", userObject.getId()))
                .andExpect(status().isNoContent());
    }
}
