package vn.spring.crud.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.spring.crud.entity.User;
import vn.spring.crud.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private  UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("test");
        testUser.setEmail("test@gg.com");
    }

    @Test
    public void createUser_shouldThrowException_whenEmailAlreadyExists() {
        when(this.userRepository.existsByEmail(any(String.class))).thenReturn(true);
        Exception ex = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            this.userService.createUser(testUser);
        });
        Assertions.assertEquals("Email already exists", ex.getMessage());
        verify(this.userRepository, times(1)).existsByEmail(any(String.class));
        verify(this.userRepository, never()).save(any(User.class));
    }

    @Test
    public void createUser_shouldReturnUser_whenEmailValid() {
        when(this.userRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(this.userRepository.save(any(User.class))).thenReturn(testUser);
        User result = this.userService.createUser(testUser);
        verify(this.userRepository, times(1)).existsByEmail(any(String.class));
        verify(this.userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void getAllUsers_shouldReturnListUsers() {
        when(this.userRepository.findAll()).thenReturn(List.of(testUser));
        List<User> result = this.userService.getAllUsers();
        Assertions.assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void getUserById_shouldReturnUserOptional() {
        when(this.userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        Optional<User> result = this.userService.getUserById(1L);
        Assertions.assertTrue(result.isPresent());
        verify(this.userRepository, times(1)).findById(anyLong());
    }

    @Test
    public void updateUser_shouldThrowException_whenUserNotFound() {
        when(this.userRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            this.userService.updateUser(1L, testUser);
        });
        verify(this.userRepository, times(1)).findById(anyLong());
        verify(this.userRepository, never()).existsByEmail(anyString());
        verify(this.userRepository, never()).save(any(User.class));
    }

    @Test
    public void updateUser_shouldThrowException_whenEmailAlreadyExists() {
        when(this.userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(this.userRepository.existsByEmail(any(String.class))).thenReturn(true);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            this.userService.updateUser(1L, testUser);
        });
        verify(this.userRepository, times(1)).findById(anyLong());
        verify(this.userRepository, times(1)).existsByEmail(anyString());
        verify(this.userRepository, never()).save(any(User.class));
    }

    @Test
    public void updateUser_shouldReturnUser_whenSuccess() {
        User updateDetails = new User();
        updateDetails.setUsername("updated");
        updateDetails.setEmail("updated@gg.com");
        when(this.userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(this.userRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(this.userRepository.save(any(User.class))).thenReturn(testUser);
        User result = this.userService.updateUser(1L, updateDetails);
        Assertions.assertEquals("updated", result.getUsername());
        Assertions.assertEquals("updated@gg.com", result.getEmail());
        verify(this.userRepository, times(1)).findById(anyLong());
        verify(this.userRepository, times(1)).existsByEmail(anyString());
        verify(this.userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void deleteUser_shouldThrowException_whenUserNotFound() {
        when(this.userRepository.existsById(anyLong())).thenReturn(false);
        Exception ex= Assertions.assertThrows(NoSuchElementException.class, () -> {
            this.userService.deleteUser(1L);
        });
        Assertions.assertEquals("User not found", ex.getMessage());
        verify(this.userRepository, times(1)).existsById(anyLong());
        verify(this.userRepository, never()).deleteById(anyLong());
    }

    @Test
    public void deleteUser_shouldDeleteUser_whenUserExists() {
        when(this.userRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(this.userRepository).deleteById(anyLong());
        this.userService.deleteUser(1L);
        verify(this.userRepository, times(1)).existsById(anyLong());
        verify(this.userRepository, times(1)).deleteById(anyLong());
    }
}
