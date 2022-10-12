package com.example.tutorboot.service;

import com.example.tutorboot.models.Skill;
import com.example.tutorboot.repo.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SkillService {

    @Autowired
    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }
    private final SkillRepository skillRepository;

    @Transactional(readOnly = true)
    public List<Skill> findAll() {
        return skillRepository.findAll();
    }
}
