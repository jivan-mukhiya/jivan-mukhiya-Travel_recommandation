package com.texas.traveldestinationrecommendation.dto;


import lombok.Data;
import org.springframework.http.HttpStatusCode;

@Data
public class AdminDto {

    private String username;
    private String password;
}
