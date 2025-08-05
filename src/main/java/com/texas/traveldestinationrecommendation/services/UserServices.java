package com.texas.traveldestinationrecommendation.services;

import com.texas.traveldestinationrecommendation.dto.UserDto;

import java.util.List;

public interface UserServices {

    List<UserDto> getAllUser();
    UserDto getUserById(Long id);
    UserDto AddUser(UserDto user,String email);
    UserDto UpdateUser(UserDto user, Long id);
    void DeleteUser(Long id);
}
