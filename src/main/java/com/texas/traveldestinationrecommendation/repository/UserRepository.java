package com.texas.traveldestinationrecommendation.repository;

import com.texas.traveldestinationrecommendation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
