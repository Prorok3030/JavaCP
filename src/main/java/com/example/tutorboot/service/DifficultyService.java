package com.example.tutorboot.service;

import com.example.tutorboot.models.Difficulty;
import com.example.tutorboot.repo.DifficultyRepository;
import com.example.tutorboot.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DifficultyService {

    @Autowired
    public DifficultyService(DifficultyRepository difficultyRepository) {
        this.difficultyRepository = difficultyRepository;
    }
    private final DifficultyRepository difficultyRepository;

    @Transactional (readOnly = true)
    public List<Difficulty> findAll() {
        return difficultyRepository.findAll();
    }
}
