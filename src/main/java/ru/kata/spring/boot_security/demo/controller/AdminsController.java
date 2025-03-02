package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminsController {

    private final UserService userService;

    @Autowired
    public AdminsController(UserService userService, RoleService roleService) {
        this.userService = userService;
    }

    @GetMapping
    public String adminPanel(Model model, Principal principal) {
        User currentUser = userService.findUserByUsername(principal.getName()).get();
        model.addAttribute("user", currentUser);
        return "admin";
    }
}