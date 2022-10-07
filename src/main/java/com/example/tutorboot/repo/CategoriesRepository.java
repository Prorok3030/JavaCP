package com.example.tutorboot.repo;

import com.example.tutorboot.models.Category;
import com.example.tutorboot.models.Tasks;
import com.example.tutorboot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriesRepository extends JpaRepository<Category,Long> {
    List<Category> findByUser (User user);

    Category findByNameAndUser (String name, User user);
}
