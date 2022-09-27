package com.example.tutorboot.service;

import com.example.tutorboot.models.Tasks;
import com.example.tutorboot.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TasksService {
    @Autowired
    TaskRepository taskRepository;

    public Page<Tasks> findPaginated(Pageable pageable, List<Tasks> tasks) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Tasks> list;

        if (tasks.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, tasks.size());
            list = tasks.subList(startItem, toIndex);
        }

        Page<Tasks> tasksPage
                = new PageImpl<Tasks>(list, PageRequest.of(currentPage, pageSize), tasks.size());

        return tasksPage;
    }
}
