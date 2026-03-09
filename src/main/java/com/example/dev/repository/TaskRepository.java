package com.example.dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dev.model.Task;

public interface TaskRepository extends JpaRepository<Task, String> {

}
