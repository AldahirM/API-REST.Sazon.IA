package com.proyecto.SazonIA.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.SazonIA.model.User;
import com.proyecto.SazonIA.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void getAll() {
        userRepository.findAll();
    }

    public User getById(Integer user_id) {
        return userRepository.findById(user_id).get();
    }

    public Optional<User> getByIdOptional(Integer user_id) {
        return userRepository.findById(user_id);
    }    

    public void delete(Integer user_id) {
        userRepository.deleteById(user_id);
    }



}
