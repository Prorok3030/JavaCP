package com.example.tutorboot.controllers;

import com.example.tutorboot.models.Tasks;
import com.example.tutorboot.models.User;
import com.example.tutorboot.repo.TaskRepository;
import com.example.tutorboot.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/tasks")
    public String home(@AuthenticationPrincipal User user, Model model, Principal principal) {
        Long id = userRepository.findByUsername(principal.getName()).getId();
        model.addAttribute("id", "Твой id: " + id);
        Iterable<Tasks> tasks = taskRepository.findByUser(user);
        model.addAttribute("tasks", tasks);
        model.addAttribute("username", "Привет, " + principal.getName() + "!");
        return "tasks";
    }

    @GetMapping("/taskAdd")
    public String taskAdd(Model model){
        return "taskAdd";
    }

    @PostMapping("/taskAdd")
    public String taskPostAdd(@AuthenticationPrincipal User user,
                              @RequestParam String name, @RequestParam String skill, @RequestParam String difficulty, Model model){
        Tasks tasks = new Tasks(name, skill, difficulty, user);
        taskRepository.save(tasks);
        return "redirect:/taskAdd";
    }

    @GetMapping("/taskEdit/{id}")
    public String taskEdit(@PathVariable("id") long id, Model model){
        Optional<Tasks> tasks = taskRepository.findById(id); //Optional?
        model.addAttribute("tasks", tasks);
        return "taskEdit";
    }

    @PostMapping("/taskEdit/{id}")
    public String taskPostEdit(Tasks tasks){
        taskRepository.save(tasks);
        return "taskEdit";
    }

    @GetMapping("/taskDelete/{id}")
    public String taskDelete(@PathVariable("id") long id){
        taskRepository.deleteById(id);
        return "redirect:/tasks";
    }
}
