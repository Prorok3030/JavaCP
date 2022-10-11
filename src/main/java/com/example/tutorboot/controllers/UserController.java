package com.example.tutorboot.controllers;

import com.example.tutorboot.models.Role;
import com.example.tutorboot.models.User;
import com.example.tutorboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@EnableGlobalMethodSecurity(prePostEnabled = true) //для работы аннотации @PreAuthorize
@Controller
public class UserController {

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    private final UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users")
    public String users(Model model){
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/userEdit/{user}")
    public String userEdit(@PathVariable User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("roleUser", Role.USER);
        return "userEdit";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/userEdit/{user}")
    public String userPostEdit(@AuthenticationPrincipal User user1,
                               @RequestParam(name="roles[]", required = false) String[] roles,
                               @RequestParam("id") User user){
        user.getRoles().clear();

        if(roles!=null) {
            Arrays.stream(roles).forEach(r -> user.getRoles().add(Role.valueOf(r)));
        }
        if (user.getId() == user1.getId()) {
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication); //назначение контексту обновленного пользователя
            userService.save(user);
            return "redirect:/";
        }
        else {
            userService.save(user);
            return "redirect:/users";
        }
    }

    @GetMapping("/profileEdit/{user}")
    public String profileEdit(@PathVariable User user, Model model){
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "profileEdit";
    }
    @PostMapping("/profileEdit/{user}")
    public String profilePostEdit(@Valid User user, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            return "profileEdit";
        }
        User DbUser = userService.findByUsername(user.getUsername());
        if (DbUser != null) {
            model.addAttribute("message", "Пользователь уже существует");
            return "profileEdit";
        }
        Authentication  authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication); //назначение контексту обновленного пользователя
        userService.save(user);
        return "redirect:/profile";
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/userDelete/{id}")
    public String userDelete(@PathVariable("id") long id, @AuthenticationPrincipal User user){
        User deletableUser = userService.findOne(id);
        if (user.getId() == deletableUser.getId()){
            userService.delete(id);
            return "redirect:/logout";
        }
        else {
            userService.delete(id);
            return "redirect:/users";
        }
    }

    @GetMapping("/userDel/{id}")
    public String userDel(@PathVariable("id") long id, @AuthenticationPrincipal User user){
        User deletableUser = userService.findOne(id);
        if (user.getId() == deletableUser.getId()){
            userService.delete(id);
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
    } //11859
}
