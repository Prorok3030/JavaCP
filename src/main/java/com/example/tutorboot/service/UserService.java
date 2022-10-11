package com.example.tutorboot.service;

import com.example.tutorboot.models.User;
import com.example.tutorboot.repo.DifficultyRepository;
import com.example.tutorboot.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public void save(User user){
        userRepository.save(user);
    }


    public void UserExpUp (User user, Integer diff){
        user.setExperience(user.getExperience() + diff);
        if (user.getExperience() >= 100){
            user.setLevel(user.getLevel() + 1);
            user.setExperience(user.getExperience() - 100);
        }
    }
    @Transactional (readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional (readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional (readOnly = true)
    public User findOne(Long id){
        Optional<User> foundCategory = userRepository.findById(id);
        return foundCategory.orElse(null);
    }
    @Transactional
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    public void UserSkillUp(String skill, Integer difPoint, User user){
        switch (skill) {
            case "Сила": user.setStrength(user.getStrength() + difPoint);
                break;
            case "Интеллект": user.setIntelligence(user.getIntelligence() + difPoint);
                break;
            case "Здоровье": user.setHealth(user.getHealth() + difPoint);
                break;
            case "Креативность": user.setCreativity(user.getCreativity() + difPoint);
                break;
            case "Общение": user.setCommunication(user.getCommunication() + difPoint);
                break;
        }
    }

    public String UserRank(Integer level){
        String rank = "";
        if (level < 5){
            rank="Начинающий приключенец";
        }
        else if (level >= 5 && level < 10){
            rank="Подающий надежды";
        }
        else if (level >= 10 && level < 20){
            rank="Местный герой";
        }
        else if (level >= 20 && level < 50){
            rank="Опытный приключенец";
        }
        else if (level >= 50 && level < 100){
            rank="Народный герой";
        }
        else if (level >= 100){
            rank="Легендарный герой";
        }
        return rank;
    }

}
