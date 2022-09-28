package com.example.tutorboot.repo;

import com.example.tutorboot.models.Difficulty;
import com.example.tutorboot.models.Tasks;
import com.example.tutorboot.models.User;
import org.springframework.data.repository.CrudRepository;

public interface DifficultyRepository extends CrudRepository<Difficulty, Long> {
    Difficulty findByName(String name);

}
