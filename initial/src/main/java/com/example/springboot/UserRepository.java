package com.example.springboot;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Map;

public interface UserRepository extends CrudRepository<User, Integer> {


   // User findByUsername(String username);
}