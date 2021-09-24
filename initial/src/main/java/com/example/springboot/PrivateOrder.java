package com.example.springboot;

public class PrivateOrder {
    public Integer price;
    public Integer quantity;
    public String action;

    public PrivateOrder(Integer price, Integer quantity, String action){
        this.price=price;
        this.quantity=quantity;
        this.action=action;
    }

    @Override
    public String toString() {
        return "PrivateOrder{" +
                "price=" + price +
                ", quantity=" + quantity +
                ", action='" + action + '\'' +
                '}';
    }
}
