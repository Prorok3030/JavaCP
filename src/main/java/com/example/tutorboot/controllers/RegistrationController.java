package com.example.tutorboot.controllers;

import com.example.tutorboot.models.Role;
import com.example.tutorboot.models.User;
import com.example.tutorboot.service.CategoriesService;
import com.example.tutorboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    @Autowired
    public RegistrationController(UserService userService, CategoriesService categoriesService) {
        this.userService = userService;
        this.categoriesService = categoriesService;
    }

    private final UserService userService;

    private final CategoriesService categoriesService;

    @GetMapping("/registration")
    public String registration(@ModelAttribute("user") User user){
        return "registration";
    }

    @PostMapping("/registration")
    public String registrationPost(@Valid User user, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "registration";
        }
        User DbUser = userService.findByUsername(user.getUsername());
        if (DbUser != null) {
            model.addAttribute("message", "Пользователь уже существует");
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        categoriesService.createCommon(user);
        userService.save(user);

        return "redirect:/tasks";
    }
}
