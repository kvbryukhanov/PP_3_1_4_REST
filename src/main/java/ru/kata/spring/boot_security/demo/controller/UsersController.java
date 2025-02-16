package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UsersController {

    private final UserService userService;

    @Autowired
    UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/my_page")
    public String showOne(ModelMap model, Principal principal) {
        Optional<User> user = userService.findUserByUsername(principal.getName());
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("roles", user.get().getRoles());
        }
        return "my_page";
    }

}