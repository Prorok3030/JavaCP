package com.example.tutorboot.repo;

import com.example.tutorboot.models.Tasks;
import com.example.tutorboot.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Tasks, Long> {
    List<Tasks> findByUser (User user);

    Page<Tasks> findByUser (Pageable pageable, User user);//Найти задачи для конкретного пользователя по user_id

}
