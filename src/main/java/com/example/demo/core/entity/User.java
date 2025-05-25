package com.example.demo.core.entity;


import jakarta.persistence.Entity;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Entity
public class User {
    @Id
    private Long id;
    private UUID username; // или UUID, если username может быть null
    private String chatId;
}
