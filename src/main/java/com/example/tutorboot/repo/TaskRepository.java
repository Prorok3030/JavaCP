package com.example.tutorboot.repo;

import com.example.tutorboot.models.Tasks;
import com.example.tutorboot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Tasks, Long> {
    List<Tasks> findByUsers (User user); //Найти задачи для конкретного пользователя по user_id

}
