package com.texas.traveldestinationrecommendation.repository;

import com.texas.traveldestinationrecommendation.model.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserLoginRepository extends JpaRepository<UserLogin, Long> {

    UserLogin findByUsernameOrEmail(String username, String email);
    UserLogin findByEmail(String email);

    @Query("SELECT u FROM UserLogin u WHERE (u.username = :usernameOrEmail OR u.email = :usernameOrEmail) AND u.password = :password")
    Optional<UserLogin> findByUsernameOrEmailAndPassword(
            @Param("usernameOrEmail") String usernameOrEmail,
            @Param("password") String password);

    @Query("SELECT COUNT(u) FROM UserLogin u WHERE u.registrationDate >= :cutoffDate")
    long countNewUsersSince(LocalDateTime cutoffDate);
}
