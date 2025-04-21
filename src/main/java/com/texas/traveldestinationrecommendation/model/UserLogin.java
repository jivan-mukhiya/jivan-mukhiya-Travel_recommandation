package com.texas.traveldestinationrecommendation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "User_login")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String Username;
    private String password;
    private String email;
    private String role;
}
