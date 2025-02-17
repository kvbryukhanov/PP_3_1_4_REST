package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
                       @RequestParam("roleIds") List<Long> roleIds,
                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", roleService.getAllRoles()); // Добавляем роли в модель
            model.addAttribute("users", userService.index()); // Добавляем пользователей в модель
            model.addAttribute("newUser", user); // Сохраняем введённые данные пользователя
            model.addAttribute("activeTab", "newUser"); // Указываем активную вкладку
            return "admin"; // Возвращаем представление admin.html
        }

        Set<Role> roles = new HashSet<>(roleService.findByIds(roleIds)); // Должен быть метод в сервисе
        user.setRoles(roles);
        userService.save(user);
        model.addAttribute("allRoles", roleService.getAllRoles()); // Добавляем роли в модель
        model.addAttribute("users", userService.index()); // Добавляем пользователей в модель
        model.addAttribute("newUser", user); // Сохраняем введённые данные пользователя
        model.addAttribute("activeTab", "newUser");
        return "redirect:/admin"; // Редирект на страницу админ-панели
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         ModelMap model,
                         @RequestParam int id,
                         @RequestParam(value = "roleIds", required = false)
                         List<Long> roleIds) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", roleService.getAllRoles());
            return "admin";
        }

        User existingUser = userService.show(id);

        user.setPassword(userService.encodePassword(user.getPassword()));

        if (roleIds != null) {
            Set<Role> roles = new HashSet<>(roleService.findByIds(roleIds));
            user.setRoles(roles);
        } else {
            user.setRoles(existingUser.getRoles());
        }

        userService.update(id, user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/delete")
    public String delete(@RequestParam int id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}