package com.example.tutorboot.controllers;

import com.example.tutorboot.models.Category;
import com.example.tutorboot.service.CategoriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("categories")
public class CategoriesController {

    private final CategoriesService categoriesService;

    @Autowired
    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping()
    public String index(Model model){
        model.addAttribute("categories", categoriesService.findAll());
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
    public String create(@ModelAttribute("category") Category category){
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
