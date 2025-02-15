package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> index();

    User show(int id);

    void save(User user);

    void update(int id, ru.kata.spring.boot_security.demo.model.User user);

    void delete(int id);
}