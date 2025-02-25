package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminsController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    AdminsController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPanel(Model model, Principal principal) {
        User currentUser = userService.findUserByUsername(principal.getName()).get();
        model.addAttribute("users", userService.index());
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("user", currentUser);
        model.addAttribute("newUser", new User());
        return "admin";
    }

    @PostMapping("/create")
    public String save(@ModelAttribute("user") @Valid User user,
                       BindingResult bindingResult,
                       @RequestParam("roleIds") List<Long> roleIds) {
        if (bindingResult.hasErrors()) {
            return "redirect:/admin";
        }

        userService.save(user, roleIds);
        return "redirect:/admin"; // Редирект на страницу админ-панели
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @RequestParam int id,
                         @RequestParam(value = "roleIds", required = false)
                         List<Long> roleIds) {
        if (bindingResult.hasErrors()) {
            return "redirect:/admin";
        }

        userService.update(id, user, roleIds);
        return "redirect:/admin";
    }

    @PostMapping(value = "/delete")
    public String delete(@RequestParam int id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}