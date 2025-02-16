package ru.kata.spring.boot_security.demo.initialize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.Set;

@Component
public class DataInitializer {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public DataInitializer(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void init() {
        if (roleService.getAllRoles().isEmpty()) {
            roleService.save(new Role("ROLE_ADMIN"));
            roleService.save(new Role("ROLE_USER"));
        }

        if (userService.findUserByUsername("admin").isEmpty()) {
            User defaultUser = new User();
            defaultUser.setFirstName("Admin");
            defaultUser.setLastName("Adminov");
            defaultUser.setUsername("admin");
            defaultUser.setPassword("admin");
            defaultUser.setRoles(Set.of(roleService.findByName("ROLE_ADMIN"),
                    roleService.findByName("ROLE_USER")));
            userService.save(defaultUser);
        }
        if (userService.findUserByUsername("user").isEmpty()) {
            User defaultUser = new User();
            defaultUser.setFirstName("User");
            defaultUser.setLastName("Userov");
            defaultUser.setUsername("user");
            defaultUser.setPassword("user");
            defaultUser.setRoles(Set.of(roleService.findByName("ROLE_USER")));
            userService.save(defaultUser);
        }
    }
}