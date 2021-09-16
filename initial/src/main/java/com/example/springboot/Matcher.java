package com.example.springboot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Matcher {


    private static ArrayList<Order> buyOrder;
    private static ArrayList<Order> sellOrder;
    private static ArrayList<Trade> tradeHistory;
    private static Map<Integer, Integer> aggBuyList;
    private static Map<Integer, Integer> aggSellList;

    public Matcher() {
        buyOrder = new ArrayList<>(); // Array to store buy orders
        sellOrder = new ArrayList<>(); // Array to store sell orders
        tradeHistory = new ArrayList<>(); // Array to store successful trades
        aggSellList = new HashMap<>(); // Dictionary to store aggregated sell orders
        aggBuyList = new HashMap<>(); // Dictionary to store aggregated buy orders
    }

    // Getter for the buy list
    public ArrayList<Order> getBuyOrder() {
        return buyOrder;
    }

    //Setter for the buy list
    public void setBuyOrder(ArrayList<Order> buyOrder) {
        Matcher.buyOrder = buyOrder;
    }
    // Getter for the sell list
    public  ArrayList<Order> getSellOrder() {
        return sellOrder;
    }
    // Setter for the sell list
    public  void setSellOrder(ArrayList<Order> sellOrder) {
        Matcher.sellOrder = sellOrder;
    }
    // Getter for the trade history
    public  ArrayList<Trade> getTradeHistory() {
        return tradeHistory;
    }
    // Setter for the tradeHistory
    public  void setTradeHistory(ArrayList<Trade> tradeHistory) {
        Matcher.tradeHistory = tradeHistory;
    }
    //Getter for the aggBuyList
    public  Map<Integer, Integer> getAggBuyList() {
        return aggBuyList;
    }
    // Setter for the aggBuyList
    public  void setAggBuyList(Map<Integer, Integer> aggBuyList) {
        Matcher.aggBuyList = aggBuyList;
    }
    //Getter for the aggSellList
    public  Map<Integer, Integer> getAggSellList() {
        return aggSellList;
    }
    //Setter for the aggSellList
    public  void setAggSellList(Map<Integer, Integer> aggSellList) {
        Matcher.aggSellList = aggSellList;
    }


    /**
     * Compare function to sort the lists out in price order and then in date order
     * @param {Order a} First order  being compared.
     * @param {Order b} Second order being compared.
     **/
    public  int compareTo(Order a, Order b){
        if(a.price==b.price){
            return a.orderDate.compareTo(b.orderDate);
        } else if (a.price>b.price){
            return 1;
        } else{
            return -1;
        }
    }


    /**
     * Returns the new order entered.
     *
     * @param {String} account The account name.
     * @param {Integer} price The price of the order, must be a positive number.
     * @param {Integer} quantity The amount of coins to buy.
     * @param {String} action The trade action to do, must be a buy or a sell.
     * @return {Order} newOrder.
     **/
    public  Order createOrder(String account, int price, int quantity, String action){
        Order newOrder= new Order(account, price, quantity, action);

        return newOrder;
    }



    /** Updating the orders history if there is a match
     * Creates a new trade and then adds it to the trade list
     * @param {Order} newOrder New Order being compared.
     * @param {Order} ExistingOrder Existing Order being compared.
     * @param {number} quantity Number of stocks bought or sold in a trade.
     **/
    public  void  createTrade(Order newOrder, Order existingOrder, int quantity){
        Trade newTrade=new Trade(newOrder,existingOrder,quantity);
        tradeHistory.add(newTrade);
        }


    /**
     * Checks if the input data is valid.
     * @param {Order} newOrder The input for the new order.
     **/
    public boolean validityCheck(Order newOrder){
        //System.out.println("This is order");
        //System.out.println(newOrder);

        if(newOrder.quantity<0 || newOrder.price<0){
            return false;
        } else if(!Objects.equals(newOrder.action, "Buy") && !Objects.equals(newOrder.action, "Sell")){
            return false;
        } else{
            checkMatch(newOrder);
            return true;
        }
    }



    /**
     * Checks if there is a match for the new order.
     * @param {Order} newOrder The input for the new order.
     **/
    public void checkMatch(Order newOrder){

        if(Objects.equals(newOrder.action, "Buy")) {
            if (sellOrder.size() == 0) {
                buyOrder.add(newOrder);
            } else {
                for (int i = 0; i < sellOrder.size(); i++) {
                    if (newOrder.price >= sellOrder.get(i).price) {
                        buyMatch(newOrder, sellOrder.get(i));
                    } else if (i == sellOrder.size() - 1 && newOrder.price < sellOrder.get(i).price) {
                        buyOrder.add(newOrder);
                    }
                }
            }
        } else if (Objects.equals(newOrder.action, "Sell")){
            if(buyOrder.size()==0){
                sellOrder.add(newOrder);
            } else {
                for(int i=0; i<buyOrder.size();i++){
                    if(newOrder.price<= buyOrder.get(i).price){

                        sellMatch(newOrder,buyOrder.get(i));
                        break;
                    } else if(i==buyOrder.size()-1 && newOrder.price>buyOrder.get(i).price){
                        sellOrder.add(newOrder);
                    }
                }

            }
        }
    }



    /**
     * Updates the order list if there is a match
     * Adds a successfull trade to the trade list
     * @param {Order} newOrder New Order being compared.
     * @param {Order} ExistingOrder Existing Order being compared.
     **/
    public void buyMatch(Order newOrder, Order existingOrder){
        if(newOrder.quantity==existingOrder.quantity){
            createTrade(newOrder,existingOrder, newOrder.quantity);
            sellOrder.remove(sellOrder.indexOf(existingOrder));
        } else if(newOrder.quantity< existingOrder.quantity){
            createTrade(newOrder,existingOrder, newOrder.quantity);
            existingOrder.quantity= existingOrder.quantity- newOrder.quantity;
        } else {
            createTrade(newOrder, existingOrder, existingOrder.quantity);
            newOrder.quantity = newOrder.quantity - existingOrder.quantity;
            sellOrder.remove(sellOrder.indexOf(existingOrder));
            checkMatch(newOrder);
        }
    }



    /**
     * Updates the order list if there is a match
     * Adds a successfull trade to the trade list
     * @param {Order} newOrder New Order being compared.
     * @param {Order} ExistingOrder Existing Order being compared.
     **/
    public void sellMatch(Order newOrder, Order existingOrder) {
        if (newOrder.quantity == existingOrder.quantity) {
            createTrade(newOrder, existingOrder, newOrder.quantity);
            buyOrder.remove(buyOrder.indexOf(existingOrder));
        } else if (newOrder.quantity < existingOrder.quantity) {
            createTrade(newOrder, existingOrder, newOrder.quantity);
            existingOrder.quantity = existingOrder.quantity - newOrder.quantity;
        } else {
            createTrade(newOrder, existingOrder, existingOrder.quantity);
            newOrder.quantity = newOrder.quantity - existingOrder.quantity;
            buyOrder.remove(buyOrder.indexOf(existingOrder));
            checkMatch(newOrder);
        }
    }


    /** Adds the orders from the buy list into an aggregated
     * list that shows the quantity of orders for a specific price
     **/
    public void aggSell(){
        for(int i=0; i<sellOrder.size();i++){
            if (aggSellList.get(sellOrder.get(i).price)==null){
                aggSellList.put(sellOrder.get(i).price,sellOrder.get(i).quantity);
            } else{
                System.out.println(aggSellList);
                aggSellList.put(sellOrder.get(i).price,aggSellList.get(sellOrder.get(i).price)+sellOrder.get(i).quantity);
            }
        }
    }



    /** Adds the orders from the buy list into an aggregated
     * list that shows the quantity of orders for a specific price
     **/
    public void aggBuy(){
        for(int i=0; i<buyOrder.size();i++){
            if (aggBuyList.get(buyOrder.get(i).price)==null){
                aggBuyList.put(buyOrder.get(i).price,buyOrder.get(i).quantity);
            } else{
                System.out.println(aggSellList);
                aggBuyList.put(buyOrder.get(i).price,aggBuyList.get(buyOrder.get(i).price)+buyOrder.get(i).quantity);
            }
        }
    }

    /**
     * Sorts the buy array with high prices to low prices
     * Then sorts them by the time, earliest ones being at the front.
     * @param {buy} val greater than or smaller than zero .
     **/
    public void sortBuy(){
        buyOrder.sort((Order a, Order b)-> compareTo(a,b));
    };



    /**
     * Sorts the sell array with low prices to high prices
     * Then sorts them by the time, earliest ones being at the front.
     * @param {sell} val greater than or smaller than zero .
     **/
    public void sortSell(){
        sellOrder.sort((Order a, Order b)-> compareTo(a,b));
    };



    public void main(String[] args){
      // validityCheck(createOrder("Hassan",5,555,"Buy")) ;




    }

}
