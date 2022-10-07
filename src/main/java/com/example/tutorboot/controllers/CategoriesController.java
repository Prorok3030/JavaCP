package com.example.tutorboot.controllers;

import com.example.tutorboot.models.Category;
import com.example.tutorboot.models.User;
import com.example.tutorboot.repo.CategoriesRepository;
import com.example.tutorboot.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("categories")
public class CategoriesController {

    private final CategoriesService categoriesService;
    private final CategoriesRepository categoriesRepository;

    @Autowired
    public CategoriesController(CategoriesService categoriesService, CategoriesRepository categoriesRepository) {
        this.categoriesService = categoriesService;
        this.categoriesRepository = categoriesRepository;
    }

    @GetMapping()
    public String index(@AuthenticationPrincipal User user, Model model){
        List<Category> categories =  categoriesRepository.findByUser(user);
        model.addAttribute("categories", categories);
        return "categories/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model){
        model.addAttribute("category", categoriesService.findOne(id));
        return "categories/show";
    }

    @GetMapping("/new")
    public String newCategories(@ModelAttribute("category") Category category){
        return "categories/new";
    }

    @PostMapping()
    public String create(@AuthenticationPrincipal User user, @ModelAttribute("category") Category category){
        category.setUser(user);
        categoriesService.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id){
        model.addAttribute("category", categoriesService.findOne(id));
        return "categories/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("category") Category category,@PathVariable("id") Long id){
        categoriesService.update(id,category);
        return "redirect:/categories";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id){
        categoriesService.delete(id);
        return "redirect:/categories";
    }

}
