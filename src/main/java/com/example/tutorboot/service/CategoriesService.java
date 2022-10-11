package com.example.tutorboot.service;

import com.example.tutorboot.models.Category;
import com.example.tutorboot.models.User;
import com.example.tutorboot.repo.CategoriesRepository;
import com.example.tutorboot.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;
    private final UserRepository userRepository;

    @Autowired
    public CategoriesService(CategoriesRepository categoriesRepository, UserRepository userRepository) {
        this.categoriesRepository = categoriesRepository;
        this.userRepository = userRepository;
    }

    public List<Category> findAll(){
        return  categoriesRepository.findAll();
    }

    public Category findOne(Long id){
        Optional<Category> foundCategory = categoriesRepository.findById(id);
        return foundCategory.orElse(null);
    }

    public void createCommon(User user){
        Category category = new Category("Общее",user);
        categoriesRepository.save(category);
    }

    public List<Category> findByUser(User user){
        return categoriesRepository.findByUser(user);
    }

    @Transactional
    public void save(Category category){
        categoriesRepository.save(category);
    }

    @Transactional
    public void update(Long id, Category updatedCategory){
        updatedCategory.setId(id);
        categoriesRepository.save(updatedCategory);
    }

    @Transactional
    public void delete(Long id){
        categoriesRepository.deleteById(id);
    }



}
