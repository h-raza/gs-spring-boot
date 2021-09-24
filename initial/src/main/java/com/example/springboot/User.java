package com.example.springboot;


import javax.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Integer Id;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @JoinColumn (name="token")
    private String token;

    public User(){

    }
//    public User(String username, String password, String token){
//        this.username =username;
//        this.password =password;
//        this.token=token;
//
//    }

    public User(String username, String password){
        this.username =username;
        this.password =password;
        this.token=null;
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

    public void setToken(String token){this.token=token;}

    public String getToken(){ return this.token;}

}
