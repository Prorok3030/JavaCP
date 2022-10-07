package com.example.tutorboot.service;

import com.example.tutorboot.models.User;
import com.example.tutorboot.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    @Autowired //TODO возможно поменять Autowired на конструктор (везде)
    private UserRepository userRepository;

    private TasksService tasksService;

    @Autowired
    public UserService(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    //TODO Переместить в другое место
    public void UserExpUp(User user, Integer diff) {
        user.setExperience(user.getExperience() + diff);
        if (user.getExperience() >= 50) {
            user.setLevel(user.getLevel() + 1);
            user.setExperience(user.getExperience() - 50);
        }
    }
    //ThePoint (Попробовать поработать над дизайном (progress bar) или сделать профиль;

    @Transactional
    public void addFriend(Long id, User user) {
        Optional<User> foundedUser = userRepository.findById(id);
        user.getFriends().add(foundedUser.orElse(null));
        userRepository.save(user);
    }

//    @Transactional(readOnly = true)
//    public User findFriend(Long id) {
//        Optional<User> foundedUser = userRepository.findById(id);
//        return foundedUser.orElse(null);
//    }

    @Transactional(readOnly = true)
    public Set<User> listFriendsToTask(User user) {
        return user.getFriends();
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser.orElse(null);
    }

    @Transactional(readOnly = true)
    public Set<User> allRequests(User user) {
        Set<User> foundRequests = user.getRequests();
        System.out.println(foundRequests);
        return foundRequests;
    }

    @Transactional(readOnly = true)
    public Set<User> allFriends(User user) {
        Set<User> foundFriends = user.getFriends();
        System.out.println(foundFriends);
        System.out.println(user);
        return foundFriends;
    }

    @Transactional
    public void addRequest(Long id, User user) {
        Optional<User> foundRequest = userRepository.findById(id);
        user.getRequests().add(foundRequest.orElse(null));
        userRepository.save(user);
    }

    @Transactional
    public void deleteFriend(User user, Long id) {
        Set<User> listFriends = user.getFriends();
        listFriends.removeIf(f -> f.getId() == id);
        userRepository.save(user);
    }

    @Transactional
    public void deleteRequest(User user, Long id) {
        Set<User> listRequests = user.getRequests();
        listRequests.removeIf(f -> f.getId() == id);
        userRepository.save(user);
    }


}
