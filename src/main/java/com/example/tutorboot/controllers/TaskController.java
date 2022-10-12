package com.example.tutorboot.controllers;

import com.example.tutorboot.models.*;
import com.example.tutorboot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class TaskController {

    @Autowired
    public TaskController(TasksService tasksService, UserService userService, CategoriesService categoriesService,
                          DifficultyService difficultyService, SkillService skillService) {
        this.tasksService = tasksService;
        this.userService = userService;
        this.categoriesService = categoriesService;
        this.difficultyService = difficultyService;
        this.skillService = skillService;
    }

    private final DifficultyService difficultyService;

    private final UserService userService;

    private final TasksService tasksService;

    private final CategoriesService categoriesService;

    private final SkillService skillService;

    @GetMapping("/tasks")
    public String home(@AuthenticationPrincipal User user, Model model,
                       @RequestParam("page") Optional<Integer> page,
                       @RequestParam("size") Optional<Integer> size,
                       @RequestParam("sortBy") Optional<String> sortBy) {
        List<Tasks> tasks = tasksService.findByUser(user);
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Tasks> tasksPage = tasksService.findPaginated(PageRequest.of(currentPage -1, pageSize), tasks, user, sortBy);
        model.addAttribute("tasksPage", tasksPage);
        model.addAttribute("name", sortBy.equals(Optional.of("name")) ? "id" : "name");
        int totalPages = tasksPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("totalPages", totalPages);
        }

        model.addAttribute("username", "Привет, " + user.getUsername() + "!");
        model.addAttribute("user", user);
        return "tasks";
    }

    @GetMapping("/taskAdd")
    public String taskAdd(@AuthenticationPrincipal User user, @ModelAttribute("tasks") Tasks tasks, Model model){
        model.addAttribute("difficulties",difficultyService.findAll());
        model.addAttribute("skills",skillService.findAll());
        model.addAttribute("categories", categoriesService.findByUser(user));
        return "taskAdd";
    }

    @PostMapping("/taskAdd")
    public String taskPostAdd(@AuthenticationPrincipal User user,
                              @Valid Tasks tasks, BindingResult bindingResult, Model model){

        tasks.setUser(user);

        if(bindingResult.hasErrors()){
            Iterable<Difficulty> difficulty = difficultyService.findAll();
            model.addAttribute("difficulties", difficulty);
            model.addAttribute("skills",skillService.findAll());
            model.addAttribute("categories", categoriesService.findAll());
            return "taskAdd";
        }
        tasksService.save(tasks);
        return "redirect:/taskAdd";
    }

    @GetMapping("/taskEdit/{id}")
    public String taskEdit(@PathVariable("id") long id, Model model){
        Tasks tasks = tasksService.findOne(id);
        Iterable<Difficulty> difficulty = difficultyService.findAll();
        Tasks task1 = tasksService.findOne(id);
        Category category1 = task1.getCategory();
        model.addAttribute("tasks", tasks);
        model.addAttribute("difficulties", difficulty);
        model.addAttribute("categories", categoriesService.findAll());
        model.addAttribute("category", category1.getId());
        model.addAttribute("skills", skillService.findAll());
        return "taskEdit";
    }

    @PostMapping("/taskEdit/{id}")
    public String taskPostEdit(@Valid Tasks tasks, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            Tasks task1 = tasksService.findOne(tasks.getId());
            Category category1 = task1.getCategory();
            model.addAttribute("difficulties", difficultyService.findAll());
            model.addAttribute("categories", categoriesService.findAll());
            model.addAttribute("category", category1.getId());
            model.addAttribute("skills", skillService.findAll());
            return "taskEdit";
        }

        tasksService.save(tasks);
        return "redirect:/tasks";
    }

    @GetMapping("/taskDelete/{id}")
    public String taskDelete(@PathVariable("id") long id){
        tasksService.delete(id);
        return "redirect:/tasks";
    }

    @GetMapping("/taskSkillUp/{id}")
    public String taskSkillUp(@AuthenticationPrincipal User user ,@PathVariable("id") long id, Model model){
        Tasks tasks = tasksService.findOne(id);
        Difficulty difficulty = tasks.getDifficulty();
        Skill skill = tasks.getSkill();
        userService.UserSkillUp(skill.getName(),difficulty.getPoints(), user);
        userService.UserExpUp(user,difficulty.getPoints());
        userService.save(user);
        tasksService.delete(id);
        return "redirect:/tasks";
    }
}
