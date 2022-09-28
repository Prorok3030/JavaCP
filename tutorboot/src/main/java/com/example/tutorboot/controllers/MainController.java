package com.example.tutorboot.controllers;

import com.example.tutorboot.models.Tasks;
import com.example.tutorboot.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }
    @GetMapping("/about")
    public String about(){
        return "about";
    }

}