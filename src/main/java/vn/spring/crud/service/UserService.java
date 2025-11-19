package vn.spring.crud.service;

import org.springframework.stereotype.Service;
import vn.spring.crud.entity.User;
import vn.spring.crud.repository.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface UserService {
    public User createUser(User user);

    public List<User> getAllUsers();

    public User getUserById(Long id);

    public User updateUser(Long id, User input);

    public void deleteUser(Long id);


}
