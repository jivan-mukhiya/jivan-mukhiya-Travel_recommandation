package com.texas.traveldestinationrecommendation.Implementation;

import com.texas.traveldestinationrecommendation.dto.AdminDto;
import com.texas.traveldestinationrecommendation.dto.ResponseDto;
import org.springframework.stereotype.Service;

@Service
public class AdminLoginService {

    public ResponseDto getAdminLogin(AdminDto adminDto) {
        if ("admin".equals(adminDto.getUsername()) &&
                "admin123".equals(adminDto.getPassword())) {

            return new ResponseDto(true, "Login successful");
        } else {
            return new ResponseDto(false, "Invalid username or password");
        }
    }
}
