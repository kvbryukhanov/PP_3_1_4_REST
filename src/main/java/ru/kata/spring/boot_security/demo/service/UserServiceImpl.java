package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.DAO.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> index() {
        return userDao.index();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> show(int id) {
        return userDao.show(id);
    }

    @Override
    @Transactional
    public void save(User user, List<Long> roleIds) {
        user.setPassword(encodePassword(user.getPassword()));
        Set<Role> roles = new HashSet<>(roleService.findByIds(roleIds)); // Должен быть метод в сервисе
        user.setRoles(roles);
        userDao.save(user);
    }

    @Override
    @Transactional
    public void save(User user) {
        user.setPassword(encodePassword(user.getPassword()));
        userDao.save(user);
    }

    @Override
    @Transactional
    public void update(int id, User user, List<Long> roleIds) {
        Optional<User> existingUserOpt = this.show(id);

        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();

            // Обновляем роли
            if (roleIds != null) {
                Set<Role> roles = new HashSet<>(roleService.findByIds(roleIds));
                existingUser.setRoles(roles);
            } else {
                existingUser.setRoles(existingUser.getRoles());
            }

            if (!user.getUsername().equals(existingUser.getUsername())) {
                if (userDao.findUserByUsername(user.getUsername()).isPresent()) {
                    throw new IllegalArgumentException("Username already exists.");
                }
                existingUser.setUsername(user.getUsername());
            }

            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());

            if (!user.getPassword().isEmpty()) {
                existingUser.setPassword(encodePassword(user.getPassword()));
            } else {
                existingUser.setPassword(existingUser.getPassword());
            }

            userDao.update(id, existingUser);
        } else {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

    @Override
    @Transactional
    public void delete(int id) {
        userDao.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.loadUserByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    @Override
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
