package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<User> findAll(){
        return (List<User>) userRepository.findAll();
    }

    public User getUserById(int id){
        return userRepository.findById(id).get();
    }


    public void saveOrUpdate(User user){
        userRepository.save(user);
    }

    public void delete(int id){
        userRepository.deleteById(id);
    }

//    public User getUserByUsername(String username) {
//        return userRepository.findByUsername(username);
//    }
}