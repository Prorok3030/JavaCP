package com.example.tutorboot.controllers;

import com.example.tutorboot.models.Category;
import com.example.tutorboot.models.Difficulty;
import com.example.tutorboot.models.Tasks;
import com.example.tutorboot.models.User;
import com.example.tutorboot.repo.CategoriesRepository;
import com.example.tutorboot.repo.DifficultyRepository;
import com.example.tutorboot.repo.TaskRepository;
import com.example.tutorboot.repo.UserRepository;
import com.example.tutorboot.service.CategoriesService;
import com.example.tutorboot.service.TasksService;
import com.example.tutorboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class TaskController {

    private final TaskRepository taskRepository;
    private final CategoriesRepository categoriesRepository;

    public TaskController(TaskRepository taskRepository, CategoriesRepository categoriesRepository, TasksService tasksService, CategoriesService categoriesService) {
        this.taskRepository = taskRepository;
        this.categoriesRepository = categoriesRepository;
        this.tasksService = tasksService;
        this.categoriesService = categoriesService;
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DifficultyRepository difficultyRepository;

    @Autowired
    private UserService userService;

    private final TasksService tasksService;

    private final CategoriesService categoriesService;

//    private static List<String> difficulties;
//    static {
//        difficulties = new ArrayList<>();
//        difficulties.add("low");
//        difficulties.add("medium");
//        difficulties.add("high");
//    }

    private static List<String> skills;
    static {
        skills = new ArrayList<>();
        skills.add("strength");
        skills.add("intelligence");
        skills.add("health");
        skills.add("creativity");
        skills.add("communication");
    }


    @GetMapping("/tasks")
    public String home(@AuthenticationPrincipal User user, Long id, Model model, Principal principal) {
//        Long id = userRepository.findByUsername(principal.getName()).getId();
//        model.addAttribute("id", "Твой id: " + id); //TODO Возможно оставить только переменную, а строки в кавычках перенести в html
//        categoriesService.createNullCategory(user);
        Iterable<Tasks> tasks = taskRepository.findByUsers(user);
        model.addAttribute("tasks", tasks);
        model.addAttribute("username", "Привет, " + principal.getName() + "!");
        model.addAttribute("user", user);
//        model.addAttribute("friend", userService.findById(id));

        return "tasks";
    }

    @GetMapping("/taskAdd")
    public String taskAdd(@AuthenticationPrincipal User user, Model model){
        Iterable<Difficulty> difficulty = difficultyRepository.findAll();

        model.addAttribute("difficulties", difficulty);
        model.addAttribute("skills", skills);
        model.addAttribute("categories", categoriesRepository.findByUser(user));
        model.addAttribute("users", userService.listFriendsToTask(user));
        return "taskAdd";
    }

    @PostMapping("/taskAdd")
    public String taskPostAdd(@AuthenticationPrincipal User user,
                              @RequestParam String name, @RequestParam String skill, @RequestParam String difficulty, @RequestParam String category_name, List<User> friend, Model model){
        Difficulty diff = difficultyRepository.findByName(difficulty);
        Category cat = categoriesRepository.findByNameAndUser(category_name, user);
//        List<User> lusers = new ArrayList<>();
//        lusers.add(user);

        Tasks tasks = new Tasks(name, skill, diff, friend, cat);
        taskRepository.save(tasks);
        return "redirect:/tasks";
    }

    @GetMapping("/taskEdit/{id}")
    public String taskEdit(@PathVariable("id") long id, @AuthenticationPrincipal User user,Model model){
        Optional<Tasks> tasks = taskRepository.findById(id); //TODO Optional (в методе taskSkillUp есть альтернативное решение)
        Iterable<Difficulty> difficulty = difficultyRepository.findAll();
        Tasks task1 = tasksService.findOne(id);
        Category category1 = task1.getCategory();
        model.addAttribute("tasks", tasks);
        model.addAttribute("difficulties", difficulty);
        //сделать метод, который достает id категории по id задачи,
        //чтобы сохранилось старое значение категории
        //и чтобы было красиво!!
        model.addAttribute("categories", categoriesRepository.findByUser(user));
        model.addAttribute("category", category1.getId());
        model.addAttribute("skills", skills);
        return "taskEdit";
    }

    @PostMapping("/taskEdit/{id}")
    public String taskPostEdit(@AuthenticationPrincipal User user, Tasks tasks){
        List<User> lusers = new ArrayList<>();
        lusers.add(user);
        tasks.setUsers(lusers); //TODO Исправить, чтобы пользователь не терялся после передачи формы Edit
        taskRepository.save(tasks);
        return "redirect:/tasks";
    }

    @GetMapping("/taskDelete/{id}")
    public String taskDelete(@PathVariable("id") long id){
        taskRepository.deleteById(id);
        return "redirect:/tasks";
    }

    @GetMapping("/taskSkillUp/{id}")
    public String taskSkillUp(@AuthenticationPrincipal User user ,@PathVariable("id") long id, Model model){
        Tasks tasks = taskRepository.findById(id).orElse(null);
        Difficulty difficulty = tasks.getDifficulty();
        Integer difPoint = difficulty.getPoints();

        switch (tasks.getSkill_name()) {
            case "strength": user.setStrength(user.getStrength() + difPoint);
                break;
            case "intelligence": user.setIntelligence(user.getIntelligence() + difPoint);
                break;
            case "health": user.setHealth(user.getHealth() + difPoint);
                break;
            case "creativity": user.setCreativity(user.getCreativity() + difPoint);
                break;
            case "communication": user.setCommunication(user.getCommunication() + difPoint);
                break;
        }
        userService.UserExpUp(user,difPoint);
        userRepository.save(user);
        taskRepository.deleteById(id);
        return "redirect:/tasks";
    }
}
