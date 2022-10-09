package com.example.tutorboot.controllers;

import com.example.tutorboot.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.tutorboot.models.User;

import java.util.List;

@Controller
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

//    @PostMapping("/friend/{id}")
//    public String findFriend(@AuthenticationPrincipal User user, @PathVariable("id") Long id, Model model){
//        userService.addRequest(user,id);
//        return "redirect:/friends";
//    }

    @GetMapping("/friends")
    public String allFriends(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("friends", userService.allFriends(user));
        return "users/index";
    }


    @GetMapping("/requests")
    public String allRequests(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("requests", userService.allRequests(user));
        model.addAttribute("user", new User());
        return "users/request";
    }

    @PostMapping("/sendRequest")
    public String sendRequest(@AuthenticationPrincipal User user, @RequestParam(required = false, value = "id") Long id) {
        List<User> luser = userService.findAll();
        if (user.getId() != id) {
            User user2 = luser.stream().filter(user1 -> user1.getId() == id).findAny().orElse(null);
            Long idu = user2.getId();
            if (idu == id) {
//                User u3 = userService.findById(id);
                User userReq = user.getRequests().stream().filter(req1 -> req1.getId() == id).findAny().orElse(null);
                if (user.getRequests().isEmpty() || !user.getRequests().contains(userReq)) {
                    userService.addRequest(id, user);
//                    User userToRequest = userService.findById(id);
//                    List<User> listRequest = userToRequest.getRequests();
//                    listRequest.add(user);
//                    userToRequest.setRequests(listRequest);
//                    userService.save(userToRequest);
                }
            }
        } else {
            System.out.println("Эй, так нельзя!");
            return "redirect:/requests";
        }
        return "redirect:/requests";
    }

//                    user.getRequests().stream().filter(user1 -> user1.getId() == id).forEach(user1 -> {
//        if (user1.getId() != id){
//            userService.addRequest(id, user);
//        }});

    @DeleteMapping("/friendsDelete/{id}")
    public String deleteFriend(@AuthenticationPrincipal User user, @PathVariable("id") Long id) {
        userService.deleteFriend(user, id);
        return "redirect:/friends";
    }

    @DeleteMapping("/requestDelete/{id}")
    public String deleteRequest(@AuthenticationPrincipal User user, @PathVariable("id") Long id) {
        userService.deleteRequest(user, id);
        return "redirect:/requests";
    }

    @PostMapping("/addToFriend/{id}")
    public String addToFriend(@AuthenticationPrincipal User user, @PathVariable("id") Long id) {
        userService.addToFriend(id, user);
        return "redirect:/friends";
    }

}
