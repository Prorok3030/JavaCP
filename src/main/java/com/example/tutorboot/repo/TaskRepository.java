package com.example.tutorboot.repo;

import com.example.tutorboot.models.Tasks;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepository extends CrudRepository<Tasks, Long> {
    List<Tasks> findAllByUserId (Long userId); //ThePoint

}
