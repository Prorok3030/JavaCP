package com.example.tutorboot.repo;

import com.example.tutorboot.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long> {
    User findByUsername(String username);

}
