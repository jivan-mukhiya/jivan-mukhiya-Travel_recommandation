package com.texas.traveldestinationrecommendation.Implementation;

import com.texas.traveldestinationrecommendation.model.UserLogin;
import com.texas.traveldestinationrecommendation.repository.UserLoginRepository;
import com.texas.traveldestinationrecommendation.services.UserLoginServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserLoginServicesImpl implements UserLoginServices {


    private final UserLoginRepository userLoginRepository;
    private final ActivityLogger activityLogger;

    @Override
    public UserLogin createUserLogin(UserLogin userLogin) {

        if(userLoginRepository.findByUsernameOrEmail(userLogin.getUsername(), userLogin.getEmail()) != null){
            throw new IllegalArgumentException("Username or email address already in use");
        }
        UserLogin savedUser = userLoginRepository.save(userLogin);
        activityLogger.logActivity(
                "USER_CREATED",
                "System",
                "Created new user: " + userLogin.getUsername()
        );
        return savedUser;
    }

        @Override
        public UserLogin updateUserLogin(String email, String password) {

            UserLogin userLogin = userLoginRepository.findByEmail(email);

            if(userLogin == null){
               throw new IllegalArgumentException("email invalided to be found");
            }
    userLogin.setPassword(password);
            return userLoginRepository.save(userLogin);
        }

    @Override
    public UserLogin getLogin(UserLogin userLogin) {

        if (userLogin.getPassword() == null || userLogin.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        String usernameOrEmail = userLogin.getUsername() != null ?
                userLogin.getUsername() :
                userLogin.getEmail();

        if (usernameOrEmail == null || usernameOrEmail.isEmpty()) {
            throw new IllegalArgumentException("Username or email must be provided");
        }

        Optional<UserLogin> user = userLoginRepository.findByUsernameOrEmailAndPassword(
                usernameOrEmail,
                userLogin.getPassword()
        );

        return user.orElseThrow(() ->
                new IllegalArgumentException("Invalid username/email or password")
        );
    }
}
