package com.example.tutorboot.service;

import com.example.tutorboot.models.Tasks;
import com.example.tutorboot.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TasksService {

    private TaskRepository taskRepository;
    @Autowired
    public TasksService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Tasks> findAll(){
        return taskRepository.findAll();
    }

    public Tasks findOne(Long id){
        Optional<Tasks> foundTask = taskRepository.findById(id);
        return foundTask.orElse(null);
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


}
