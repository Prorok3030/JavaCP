package com.example.tutorboot.repo;

import com.example.tutorboot.models.Difficulty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DifficultyRepository extends JpaRepository<Difficulty, Long> {
    Difficulty findByName(String name);

}
