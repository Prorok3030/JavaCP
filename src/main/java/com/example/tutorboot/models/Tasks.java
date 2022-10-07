package com.example.tutorboot.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String skill_name;
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "difficulty_id")
    private Difficulty difficulty;

    @ManyToMany(mappedBy = "tasks")
    private List<User> users;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;


//    public Long getUserId() { //TODO Spring Boot magic
//             return user.getId();
//    }

    public String getDifficultyName() { //TODO Spring Boot magic
        return difficulty.getName();
    }

    public Tasks() {
    }


    public Tasks(String name, String skill_name, Difficulty difficulty, List<User> users, Category category) {
        this.name = name;
        this.skill_name = skill_name;
        this.difficulty = difficulty;
        this.users = users;
        this.category = category;
    }

//    public void addTasksToUser(User user){
//        if(users==null){
//            users = new ArrayList<>();
//        }
//        users.add(user);
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkill_name() {
        return skill_name;
    }

    public void setSkill_name(String skill_name) {
        this.skill_name = skill_name;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }


    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
