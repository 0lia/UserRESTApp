package com.example.User.repositories;

import com.example.User.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findUsersByBirthDateAfterAndBirthDateBefore(Date from, Date to);

}
