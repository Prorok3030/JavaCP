package com.example.tutorboot.repo;

import com.example.tutorboot.models.Category;
import com.example.tutorboot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepository extends JpaRepository<Category,Long> {
    List<Category> findByUser (User user);

    Category findByNameAndUser (String name, User user);
}
