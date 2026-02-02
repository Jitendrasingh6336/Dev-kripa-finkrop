package com.example.dev.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Agent {

	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String role; // Telecaller, Sales Executive
}
