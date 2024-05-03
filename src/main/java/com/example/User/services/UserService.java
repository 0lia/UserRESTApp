package com.example.User.services;

import com.example.User.entities.User;
import com.example.User.exceptions.UserNotFoundException;
import com.example.User.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void update(int id, User updated){
        updated.setId(id);
        userRepository.save(updated);
    }

    @Transactional
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateAddress(int id, String newAddress){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        user.setAddress(newAddress);
        userRepository.save(user);
    }

    @Transactional
    public void updatePhone(int id, String newPhone){
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        user.setPhoneNumber(newPhone);
        userRepository.save(user);
    }

    @Transactional
    public List<User> findByDateRange(Date from, Date to) {
        if(from.after(to)) {
            throw new IllegalArgumentException("\'from\' should be before \'to\'");
        }
        return userRepository.findUsersByBirthDateAfterAndBirthDateBefore(from, to);
    }

}
