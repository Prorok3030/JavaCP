package com.example.tutorboot.service;

import com.example.tutorboot.models.User;
import com.example.tutorboot.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service //TODO посмотреть значение аннотации Service
public class UserService implements UserDetailsService {

    @Autowired //TODO возможно поменять Autowired на конструктор (везде)
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    //TODO Переместить в другое место
    public void UserExpUp (User user, Integer diff){
        user.setExperience(user.getExperience() + diff);
        if (user.getExperience() >= 50){
            user.setLevel(user.getLevel() + 1);
            user.setExperience(user.getExperience() - 50);
        }
    }

    //ThePoint (Попробовать поработать над дизайном (progress bar) или сделать профиль;

}
