package com.mamun72.service;

import com.mamun72.entity.User;
import com.mamun72.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public Iterable<User> getAll(){
        return userRepo.findAll();
    }

    public boolean ifExist(String userName){
        return userRepo.existsById(userName);
    }

    public User saveUser(User user){
          return userRepo.save(user);
    }

    public Optional<User> getOneByName(String username){
        return userRepo.findById(username);
    }
}
