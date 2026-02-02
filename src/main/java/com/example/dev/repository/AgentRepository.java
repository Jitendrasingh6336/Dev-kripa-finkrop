package com.example.dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.dev.model.Agent;

public interface AgentRepository extends JpaRepository<Agent,String> {

}
