package com.example.springboot;

import javax.validation.constraints.*;
//import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.time.*;
import java.util.*;

public class Order implements Comparable<Order> {
    @Size(min=1)
    public String account;
    @Min(5)
    public int price;
    @Min(1)
    public int quantity;

   // @Action
    public String action;
    public Date orderDate=new Date();


    public Order(){

    }

    public Order(String account, int price, int quantity, String action) {
        this.account = account;
        this.price = price;
        this.quantity = quantity;
        this.action = action;
    }

    @Override
    public String toString() {
        return "Order{" +
                "account='" + account + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", action='" + action + '\'' +
               ", orderDate=" + orderDate +
                '}';
    }


    @Override

    public int compareTo(Order aa){
        if(this.price==aa.price){
            return 0;
        } else if (this.price>aa.price){
            return 1;
        } else{
            return -1;
        }
}};


