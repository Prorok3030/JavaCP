package com.example.tutorboot.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotEmpty(message = "Название не может быть пустым")
    @Size(min = 2, max = 100, message = "Длина названия должна быть от 2 до 100 символов")
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "skill_id")
    private Skill skill;
    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "difficulty_id")
    private Difficulty difficulty;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    public Tasks(String name, Skill skill, Difficulty diff, Category cat, User user) {
        this.name = name;
        this.skill = skill;
        this.difficulty = diff;
        this.category = cat;
        this.user = user;
    }


    public Long getUserId() {
             return user.getId();
    }

    public String getDifficultyName() {
        return difficulty.getName();
    }

    public String getSkillName() {
        return skill.getName();
    }

    public Tasks() {
    }


    public Tasks(String name, Skill skill, Difficulty difficulty, User user, Category category) {
        this.name = name;
        this.skill = skill;
        this.difficulty = difficulty;
        this.user = user;
        this.category = category;
    }

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

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
