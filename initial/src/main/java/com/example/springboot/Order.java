package com.example.springboot;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

public class Order implements Comparable<Order> {
    @Size(min=1)
    public String account;
    @Min(5)
    public int price;
    @Min(1)
    public int quantity;

    @Action
    public String action;
    public LocalDateTime orderDate;


    public Order(String account, int price, int quantity, String action) {
        this.account = account;
        this.price = price;
        this.quantity = quantity;
        this.action = action;
        this.orderDate= LocalDateTime.now();
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


