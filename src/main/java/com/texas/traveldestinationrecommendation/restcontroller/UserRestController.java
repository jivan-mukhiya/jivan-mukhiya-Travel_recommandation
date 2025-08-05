package com.texas.traveldestinationrecommendation.restcontroller;

import com.texas.traveldestinationrecommendation.dto.UserDto;
import com.texas.traveldestinationrecommendation.services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class UserRestController {

    private final UserServices userServices;


    @GetMapping("/list")
    public ResponseEntity<List<UserDto>> getAllUser() {

        return new ResponseEntity<>(userServices.getAllUser(), HttpStatus.OK);
    }


    @PostMapping("/add/{email}")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto user, @PathVariable String email) {

        return new ResponseEntity<>(userServices.AddUser(user,email), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {

        return new ResponseEntity<>(userServices.getUserById(id), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto user) {
        return new ResponseEntity<>(userServices.UpdateUser(user,user.getUserId()), HttpStatus.OK);
    }
}
