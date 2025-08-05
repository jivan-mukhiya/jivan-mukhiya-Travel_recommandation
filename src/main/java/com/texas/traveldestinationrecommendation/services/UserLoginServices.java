package com.texas.traveldestinationrecommendation.services;

import com.texas.traveldestinationrecommendation.model.UserLogin;

public interface UserLoginServices {


    UserLogin createUserLogin(UserLogin userLogin);
    UserLogin updateUserLogin(String email, String password);
    UserLogin getLogin(UserLogin userLogin);

}
