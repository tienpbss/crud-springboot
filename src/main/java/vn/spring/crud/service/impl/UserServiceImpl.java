package vn.spring.crud.service.impl;


import org.springframework.stereotype.Service;
import vn.spring.crud.entity.User;
import vn.spring.crud.repository.UserRepository;
import vn.spring.crud.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    };

    @Override
    public User getUserById(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        else {
            throw new NoSuchElementException("Can not find user with id: " + id);
        }
    };

    @Override
    public User updateUser(Long id, User input) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("Can not find user with id: " + id);
        }
        if (this.userRepository.existsByEmail(input.getEmail())) {
            throw new IllegalArgumentException("Email already exists: "+input.getEmail());
        }
        User user = userOptional.get();
        user.setEmail(input.getEmail());
        user.setUsername(input.getUsername());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!this.userRepository.existsById(id)) {
            throw new NoSuchElementException("Can not find user with id: " + id);
        }
        this.userRepository.deleteById(id);
    }

}
