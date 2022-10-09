package com.example.tutorboot.service;

import com.example.tutorboot.models.User;
import com.example.tutorboot.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    public void addToFriend(Long id, User user) {
        User foundRequest = userRepository.findById(id).orElse(null);
        Set<User> listFriends = user.getFriends();
        if(!listFriends.contains(foundRequest)){
            listFriends.add(foundRequest);
            user.setFriends(listFriends);
            List<User> listRequests = user.getRequests();
            listRequests.remove(foundRequest);
            user.setRequests(listRequests);
            userRepository.save(user);
            User userWhoSendRequest = userRepository.findById(id).orElse(null);
            List<User> listReqForUserWhoSendRequest = userWhoSendRequest.getRequests();
            listReqForUserWhoSendRequest.remove(user);
            userWhoSendRequest.setRequests(listReqForUserWhoSendRequest);
            Set<User> listUserWhoSendRequest = userWhoSendRequest.getFriends();
            listUserWhoSendRequest.add(user);
            userWhoSendRequest.setFriends(listFriends);
            userRepository.save(userWhoSendRequest);
        }
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
    public List<User> allRequests(User user) {
//        List<User> allusers = userRepository.findAll();
//        List u = allusers.stream().filter(user1 -> user1.getRequests() == user.getRequests()).findAny().orElse(null);
        List<User> foundRequests = user.getRequests().stream().toList();
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
        User foundRequest = userRepository.findById(id).orElse(null);
        List<User> listRequests = user.getRequests();
        listRequests.add(foundRequest);
        user.setRequests(listRequests);
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
        List<User> listRequests = user.getRequests();
        listRequests.removeIf(f -> f.getId() == id);
        userRepository.save(user);
    }

    @Transactional
    public void save(User user){
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> findAll(){
        return userRepository.findAll();
    }


}
