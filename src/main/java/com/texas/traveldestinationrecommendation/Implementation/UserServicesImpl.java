package com.texas.traveldestinationrecommendation.Implementation;

import com.texas.traveldestinationrecommendation.dto.UserDto;
import com.texas.traveldestinationrecommendation.model.User;
import com.texas.traveldestinationrecommendation.model.UserLogin;
import com.texas.traveldestinationrecommendation.repository.UserLoginRepository;
import com.texas.traveldestinationrecommendation.repository.UserRepository;
import com.texas.traveldestinationrecommendation.services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServicesImpl implements UserServices {

    private final UserRepository userRepository;
    private final UserLoginRepository userLoginRepository;

    @Override
    public List<UserDto> getAllUser() {

        return userRepository.findAll()
                .stream()
                .map(UserServicesImpl::convertToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {

        User user=userRepository.findUserByUserLogin_Id(id).orElseThrow(()->
                new IllegalArgumentException("User not found !!"));
        return convertToUserDto(user);
    }

    @Override
    public UserDto AddUser(UserDto user, String email) {

        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setGender(user.getGender());
        newUser.setDob(user.getDob());
        newUser.setProfession(user.getProfession());
        newUser.setBudgetMin(user.getBudgetMin());
        newUser.setBudgetMax(user.getBudgetMax());
        newUser.setTravelTypePreference(user.getTravelTypePreference());
        newUser.setPastVisitedDestinations(user.getPastVisitedDestinations());
        newUser.setSeasonPreference(user.getSeasonPreference());
        newUser.setPreferences(user.getPreferences());

        UserLogin userLogin = userLoginRepository.findByEmail(email);

        if(userLogin == null) {
            throw new IllegalArgumentException("User not found");
        }
        newUser.setUserLogin(userLogin);
        User us = userRepository.save(newUser);
       return convertToUserDto(us);
    }

    @Override
    public UserDto UpdateUser(UserDto user, Long id) {
        return userRepository.findById(id).map(us -> {
            us.setName(user.getName());
            us.setGender(user.getGender());
            us.setDob(user.getDob());
            us.setProfession(user.getProfession());
            us.setBudgetMin(user.getBudgetMin());
            us.setBudgetMax(user.getBudgetMax());
            us.setTravelTypePreference(user.getTravelTypePreference());
            us.setPastVisitedDestinations(user.getPastVisitedDestinations());
            us.setSeasonPreference(user.getSeasonPreference());
            us.setPreferences(user.getPreferences());

            UserLogin userLogin = userLoginRepository.findByEmail(us.getUserLogin().getEmail());
            if (userLogin == null) {
                throw new IllegalArgumentException("User not found");
            }

            us.setUserLogin(userLogin);
            userRepository.save(us); // Save updated user
            return convertToUserDto(us);
        }).orElseThrow(() -> new IllegalArgumentException("User ID not found: " + id));
    }



    @Override
    public void DeleteUser(Long id) {

    }


    public static UserDto convertToUserDto(User user) {
        return UserDto.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .gender(user.getGender())
                .dob(user.getDob())
                .profession(user.getProfession())
                .budgetMin(user.getBudgetMin())
                .budgetMax(user.getBudgetMax())
                .travelTypePreference(user.getTravelTypePreference())
                .seasonPreference(user.getSeasonPreference())
                .pastVisitedDestinations(user.getPastVisitedDestinations())
                .preferences(user.getPreferences())
                .UserEmail(user.getUserLogin().getEmail())
                .UserName(user.getUserLogin().getUsername())
                .build();

    }


}
