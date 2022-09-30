package com.example.tutorboot.controllers;

import com.example.tutorboot.models.Tasks;
import com.example.tutorboot.models.User;
import com.example.tutorboot.repo.UserRepository;
import com.example.tutorboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public String users(Model model){
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/userEdit/{id}")
    public String userEdit(@PathVariable("id") long id, Model model){
        Optional<User> user = userRepository.findById(id);
        model.addAttribute("user", user);
        return "userEdit";
    }

    @PostMapping("/userEdit/{id}")
    public String userPostEdit(User user){
        userRepository.save(user);
        return "redirect:/profile";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/userDelete/{id}")
    public String userDelete(@PathVariable("id") long id, @AuthenticationPrincipal User user){
        User deletableUser = userRepository.findById(id).orElse(null);
        if (user.getId() == deletableUser.getId()){
            userRepository.deleteById(id);
            return "redirect:/logout";
        }
        else {
            userRepository.deleteById(id);
            return "redirect:/users";
        }
    }

    @GetMapping("/userDel/{id}")
    public String userDel(@PathVariable("id") long id, @AuthenticationPrincipal User user){
        User deletableUser = userRepository.findById(id).orElse(null);
        if (user.getId() == deletableUser.getId()){
            userRepository.deleteById(id);
            return "redirect:/logout";
        }
        else {
            return "redirect:/profile";
        }
    }

    @GetMapping("/profile")
    public String Profile (@AuthenticationPrincipal User user, Model model){
        model.addAttribute("rank",userService.UserRank(user.getLevel()));
        model.addAttribute("user", user);
        return "profile";
    }


}
