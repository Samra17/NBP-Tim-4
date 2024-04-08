package com.nbp.tim3.auth.service;

import com.nbp.tim3.auth.dto.UserResponse;
import com.nbp.tim3.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;




    public List<UserResponse> getAllUsers() {
        return userRepository.getAll().stream().map(UserResponse::new).toList();
    }

    public List<UserResponse> getAllManagers() {
        return userRepository
                .getManagers()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }

    public List<UserResponse> getAllCouriers() {
        return userRepository
                .getCouriers()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());

    }

    public String deleteUser(Integer id){
        /*var us = userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("User with id " + id + " does not exist!"));
        userRepository.deleteById(id);
        return "User with id " + id + " is successfully deleted!";*/

        return "Something";
    }

}