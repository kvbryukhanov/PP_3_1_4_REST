package ru.kata.spring.boot_security.demo.DAO;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao extends UserDetailsService {
    List<User> index();

    Optional<User> show(int id);

    void save(User user);

    void update(int id, User user);

    void delete(int id);

    Optional<User> findUserByUsername(String username);
}