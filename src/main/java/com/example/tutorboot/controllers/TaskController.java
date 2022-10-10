package com.example.tutorboot.controllers;

import com.example.tutorboot.models.Category;
import com.example.tutorboot.models.Difficulty;
import com.example.tutorboot.models.Tasks;
import com.example.tutorboot.models.User;
import com.example.tutorboot.repo.DifficultyRepository;
import com.example.tutorboot.repo.TaskRepository;
import com.example.tutorboot.repo.UserRepository;
import com.example.tutorboot.service.CategoriesService;
import com.example.tutorboot.service.TasksService;
import com.example.tutorboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository, TasksService tasksService, CategoriesService categoriesService) {
        this.taskRepository = taskRepository;
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

    private static List<String> skills;
    static {
        skills = new ArrayList<>();
        skills.add("Сила");
        skills.add("Интеллект");
        skills.add("Здоровье");
        skills.add("Креативность");
        skills.add("Общение");
    }


    @GetMapping("/tasks")
    public String home(@AuthenticationPrincipal User user, Model model,
                       @RequestParam("page") Optional<Integer> page,
                       @RequestParam("size") Optional<Integer> size,
                       @RequestParam("sortBy") Optional<String> sortBy) {
        List<Tasks> tasks = taskRepository.findByUser(user);
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

//        Long id = userRepository.findByUsername(principal.getName()).getId();
//        model.addAttribute("id", "Твой id: " + id); //TODO Возможно оставить только переменную, а строки в кавычках перенести в html
        //model.addAttribute("tasks", tasks);
        model.addAttribute("username", "Привет, " + user.getUsername() + "!");
        model.addAttribute("user", user);
        return "tasks";
    }

    @GetMapping("/taskAdd")
    public String taskAdd(@AuthenticationPrincipal User user, Tasks tasks, Model model){
        Iterable<Difficulty> difficulty = difficultyRepository.findAll();
        model.addAttribute("difficulties", difficulty);
        model.addAttribute("skills", skills);
        model.addAttribute("categories", categoriesService.findByUser(user));
        return "taskAdd";
    }

    @PostMapping("/taskAdd")
    public String taskPostAdd(@AuthenticationPrincipal User user,
                              @Valid Tasks tasks, BindingResult bindingResult, Model model){

        tasks.setUser(user);

        if(bindingResult.hasErrors()){
            Iterable<Difficulty> difficulty = difficultyRepository.findAll();
            model.addAttribute("difficulties", difficulty);
            model.addAttribute("skills", skills);
            model.addAttribute("categories", categoriesService.findAll());
            return "taskAdd";
        }
        taskRepository.save(tasks);
        return "redirect:/taskAdd";
    }

    @GetMapping("/taskEdit/{id}")
    public String taskEdit(@PathVariable("id") long id, Model model){
        Optional<Tasks> tasks = taskRepository.findById(id); //TODO Optional (в методе taskSkillUp есть альтернативное решение)
        Iterable<Difficulty> difficulty = difficultyRepository.findAll();
        Tasks task1 = tasksService.findOne(id);
        Category category1 = task1.getCategory();
        model.addAttribute("tasks", tasks);
        model.addAttribute("difficulties", difficulty);
        model.addAttribute("categories", categoriesService.findAll());
        model.addAttribute("category", category1.getId());
        model.addAttribute("skills", skills);
        return "taskEdit";
    }

    @PostMapping("/taskEdit/{id}")
    public String taskPostEdit(@Valid Tasks tasks, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            Tasks task1 = tasksService.findOne(tasks.getId());
            Category category1 = task1.getCategory();
            model.addAttribute("difficulties", difficultyRepository.findAll());
            model.addAttribute("categories", categoriesService.findAll());
            model.addAttribute("category", category1.getId());
            model.addAttribute("skills", skills);
            return "taskEdit";
        }

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
            case "Сила": user.setStrength(user.getStrength() + difPoint);
                break;
            case "Интеллект": user.setIntelligence(user.getIntelligence() + difPoint);
                break;
            case "Здоровье": user.setHealth(user.getHealth() + difPoint);
                break;
            case "Креативность": user.setCreativity(user.getCreativity() + difPoint);
                break;
            case "Общение": user.setCommunication(user.getCommunication() + difPoint);
                break;
        }
        userService.UserExpUp(user,difPoint);
        userRepository.save(user);
        taskRepository.deleteById(id);
        return "redirect:/tasks";
    }
}
