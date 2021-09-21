package com.example.springboot;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private String username;
    private String password;
    //private String token;

    public User(){

    }
    public User(String username, String password){
        this.username =username;
        this.password =password;

    }
    public User(String username, String password, String token){
        this.username =username;
        this.password =password;
        //this.token=token;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //public void setToken(String token){this.token=token;}

    //public String getToken(){ return this.token;}

}
