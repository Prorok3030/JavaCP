package com.example.tutorboot.repo;

import com.example.tutorboot.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<Category,Long> {
}
