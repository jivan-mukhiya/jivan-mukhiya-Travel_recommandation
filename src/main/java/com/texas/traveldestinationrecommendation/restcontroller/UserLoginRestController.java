package com.texas.traveldestinationrecommendation.restcontroller;
import com.texas.traveldestinationrecommendation.model.UserLogin;
import com.texas.traveldestinationrecommendation.services.UserLoginServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/login")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class UserLoginRestController {

    private final UserLoginServices userLoginServices;

    @PostMapping("/create")
    public ResponseEntity<UserLogin> CreateUserLogin(@RequestBody  UserLogin userLogin) {

        return new ResponseEntity<>(userLoginServices.createUserLogin(userLogin), HttpStatus.CREATED);
    }

    @PostMapping("/user-login")
    public ResponseEntity<UserLogin> getLogin(@RequestBody UserLogin userLogin) {
        return new ResponseEntity<>(userLoginServices.getLogin(userLogin), HttpStatus.OK);
    }

//    @PutMapping("/forgot-password/{email,password}")
//    public ResponseEntity<UserLogin> ForgotPassword(@PathVariable String email, String password) {
//
//        return new ResponseEntity<>(userLoginServices.updateUserLogin(email, password), HttpStatus.OK);
//    }

}
