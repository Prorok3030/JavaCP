package com.example.tutorboot.controllers;

import com.example.tutorboot.models.Role;
import com.example.tutorboot.models.User;
import com.example.tutorboot.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String registrationPost(User user, Map<String, Object> model){
        User DbUser = userRepository.findByUsername(user.getUsername());

        if (DbUser != null) {
            model.put("message", "Пользователь уже существует");
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.ADMIN));
        userRepository.save(user);

        return "redirect:/tasks";
    }
}
