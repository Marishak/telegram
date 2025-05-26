package com.example.demo.core.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen_nsi")
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "chat_id")
    private Long chatId;
}
