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
    public User show(int id) {
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

        User existingUser = this.show(id);

        if (roleIds != null) {
            Set<Role> roles = new HashSet<>(roleService.findByIds(roleIds));
            user.setRoles(roles);
        } else {
            user.setRoles(existingUser.getRoles());
        }

        if (!user.getPassword().isEmpty()) {
            user.setPassword(encodePassword(user.getPassword()));
        } else {
            user.setPassword(existingUser.getPassword());
        }

        userDao.update(id, user);
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
