package vn.spring.crud.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import vn.spring.crud.IntegrationTest;
import vn.spring.crud.entity.User;
import vn.spring.crud.repository.UserRepository;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@AutoConfigureMockMvc
public class UserControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private User user_blankName;
    private User user_blankEmail;
    private User user_invalidEmail;
    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        testUser = new User();
        testUser.setUsername("test");
        testUser.setEmail("test@gg.com");

        user_blankName = new User("", "test2@gg.com");
        user_blankEmail = new User("have name", "");
        user_invalidEmail = new User("name", "invalid email");
    }

    @Test
    public void createUser_Success() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value("test"))
                .andExpect(jsonPath("$.data.email").value("test@gg.com"));
    }

    @Test
    public void createUser_EmailAlreadyExist_BadRequest() throws Exception {
        this.userRepository.saveAndFlush(testUser);
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Email already exists"));
    }

    @Test
    public void createUser_UsernameBlank() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user_blankName)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Username can not blank"));

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
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    public void getUserById_Success() throws Exception {
        this.userRepository.save(testUser);
        mockMvc.perform(get("/users/{id}", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value(testUser.getUsername()))
                .andExpect(jsonPath("$.data.email").value(testUser.getEmail()));
    }

    @Test
    public void getUserById_NotFound() throws Exception {
        Long id = -1L;
        mockMvc.perform(get("/users/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Can not find user with id: " + id));
    }

    @Test
    public void updateUser_Success() throws Exception {
        User savedUser = userRepository.save(testUser);
        User userDetails = new User("jane", "jane@gmail.com");
        mockMvc.perform(put("/users/{id}", savedUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.data.username").value(userDetails.getUsername()))
                .andExpect(jsonPath("$.data.email").value(userDetails.getEmail()));
    }

    @Test
    public void updateUser_UserNotFound() throws Exception {
        User userDetails = new User("jane", "jane@gmail.com");
        Long id = -1L;
        mockMvc.perform(put("/users/{id}", id, userDetails)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDetails)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Can not find user with id: " + id));
    }


    @Test
    public void deleteUser_Success() throws Exception {
        User savedUser = userRepository.save(testUser);
        mockMvc.perform(delete("/users/{id}", savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Deleted user with id: " + savedUser.getId()));

    }
    @Test
    public void deleteUser_NotFound() throws Exception {
        mockMvc.perform(delete("/users/{id}", -1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false));
    }

//    @Test
//    public void crudFullFlow_Success() throws Exception {
//        String userString = mockMvc.perform(post("/users", testUser)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(testUser)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").isNotEmpty())
//                .andExpect(jsonPath("$.username").value(testUser.getUsername()))
//                .andExpect(jsonPath("$.email").value(testUser.getEmail()))
//                .andReturn().getResponse().getContentAsString();
//
//        User userObject = objectMapper.readValue(userString, User.class);
//
//        mockMvc.perform(get("/users"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("$", hasSize(1)));
//
//        mockMvc.perform(get("/users/{id}", userObject.getId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.username").value(testUser.getUsername()))
//                .andExpect(jsonPath("$.email").value(testUser.getEmail()));
//
//        User userDetails = new User("jane", "jane@gg.com");
//        mockMvc.perform(put("/users/{id}", userObject.getId(), userDetails)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(userDetails)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(userObject.getId()))
//                .andExpect(jsonPath("$.username").value(userDetails.getUsername()))
//                .andExpect(jsonPath("$.email").value(userDetails.getEmail()));
//
//        mockMvc.perform(delete("/users/{id}", userObject.getId()))
//                .andExpect(status().isNoContent());
//    }
}
