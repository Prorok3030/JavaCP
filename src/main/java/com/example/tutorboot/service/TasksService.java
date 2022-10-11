package com.example.tutorboot.service;

import com.example.tutorboot.models.Tasks;
import com.example.tutorboot.models.User;
import com.example.tutorboot.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TasksService {

    private static List<String> skills;
    static {
        skills = new ArrayList<>();
        skills.add("Сила");
        skills.add("Интеллект");
        skills.add("Здоровье");
        skills.add("Креативность");
        skills.add("Общение");
    }

    public static List<String> getSkills() {
        return skills;
    }

    @Autowired
    public TasksService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
    private final TaskRepository taskRepository;

    public List<Tasks> findAll(){
        return taskRepository.findAll();
    }


    public Tasks findOne(Long id){
        Optional<Tasks> foundTask = taskRepository.findById(id);
        return foundTask.orElse(null);
    }

    public List<Tasks> findByUser(User user){
        return taskRepository.findByUser(user);
    }

    @Transactional
    public void save(Tasks tasks){
        taskRepository.save(tasks);
    }

    @Transactional
    public void update(Long id, Tasks updatedTask){
        updatedTask.setId(id);
        taskRepository.save(updatedTask);
    }

    @Transactional
    public void delete(Long id){
        taskRepository.deleteById(id);
    }


    public Page<Tasks> findPaginated(Pageable pageable, List<Tasks> tasks, User user, Optional<String> sortBy) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Tasks> list;

        if (tasks.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, tasks.size());
            list = tasks.subList(startItem, toIndex); //в list записывается только нужная часть tasks
        }

        Pageable pageable1 = PageRequest.of(currentPage, pageSize, Sort.Direction.ASC, sortBy.orElse("id"));

        Page<Tasks> tasksPage
                = taskRepository.findByUser(pageable1,user);

        return tasksPage;
    }
}
