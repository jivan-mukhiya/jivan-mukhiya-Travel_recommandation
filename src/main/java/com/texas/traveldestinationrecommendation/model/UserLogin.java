package com.texas.traveldestinationrecommendation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "User_login")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private LocalDateTime registrationDate = LocalDateTime.now();
}
