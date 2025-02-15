//package ru.kata.spring.boot_security.demo.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import ru.kata.spring.boot_security.demo.model.User;
//import ru.kata.spring.boot_security.demo.service.UserService;
//
//import javax.validation.Valid;
//
//@Controller(value = "/admin")
//public class AdminsController {
//
//    private final UserService userService;
//
//    @Autowired
//    AdminsController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @GetMapping(value = "/index")
//    public String showAll(ModelMap model) {
//        model.addAttribute("users", userService.index());
//        return "index";
//    }
//
//    @GetMapping(value = "/show")
//    public String showOne(ModelMap model, @RequestParam int id) {
//        model.addAttribute("user", userService.show(id));
//        return "show";
//    }
//
//    @GetMapping(value = "/create")
//    public String create(ModelMap model) {
//        model.addAttribute("user", new User());
//        return "create";
//    }
//
//    @PostMapping("/create")
//    public String save(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
//        if (bindingResult.hasErrors())
//            return "create";
//        userService.save(user);
//        return "redirect:index";
//    }
//
//    @GetMapping(value = "/edit")
//    public String edit(ModelMap model, @RequestParam int id) {
//        model.addAttribute("user", userService.show(id));
//        return "edit";
//    }
//
//    @PostMapping("/update")
//    public String update(@ModelAttribute("user") @Valid User user,
//                         BindingResult bindingResult,
//                         @RequestParam int id) {
//        if (bindingResult.hasErrors()) {
//            return "edit";
//        }
//        userService.update(id, user);
//        return "redirect:show?id=" + id;
//    }
//
//    @PostMapping(value = "/delete")
//    public String delete(@RequestParam int id) {
//        userService.delete(id);
//        return "redirect:index";
//    }
//}