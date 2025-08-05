package com.texas.traveldestinationrecommendation.restcontroller;

import com.texas.traveldestinationrecommendation.Implementation.AdminLoginService;
import com.texas.traveldestinationrecommendation.dto.AdminDto;
import com.texas.traveldestinationrecommendation.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AdminRestController {

    private final AdminLoginService adminLoginService;
        @PostMapping("/login")
        public ResponseEntity<ResponseDto> getLogin(@RequestBody AdminDto adminDto) {
            ResponseDto response = adminLoginService.getAdminLogin(adminDto);
            return ResponseEntity.ok(response);
        }

}
