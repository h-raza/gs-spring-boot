package com.example.springboot;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class Matcher {
    @Autowired
    private static FirebaseService firebaseService;


    private static ArrayList<Order> buyOrder;
    private static ArrayList<Order> sellOrder;
    private static ArrayList<Trade> tradeHistory;
    private static Map<Integer, Integer> aggBuyList;
    private static Map<Integer, Integer> aggSellList;
    private static ArrayList<PrivateOrder> privateOrders;

    public Matcher() {
        buyOrder = new ArrayList<>(); // Array to store buy orders
        sellOrder = new ArrayList<>(); // Array to store sell orders
        tradeHistory = new ArrayList<>(); // Array to store successful trades
        aggSellList = new HashMap<>(); // Dictionary to store aggregated sell orders
        aggBuyList = new HashMap<>(); // Dictionary to store aggregated buy orders
        privateOrders= new ArrayList<>(); //Arraylist to store the private orders
        firebaseService=new FirebaseService(); // Firebase database

    }
    Firestore dbFirestore= FirestoreClient.getFirestore();
    CollectionReference buyCollectionReference=dbFirestore.collection("BuyOrders");
    CollectionReference sellCollectionReference=dbFirestore.collection("SellOrders");


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

    // Getter for privateOrders
    public  ArrayList<PrivateOrder> getPrivateOrders() {
        return privateOrders;
    }
    //Setter for privateOrders
    public  void setPrivateOrders(ArrayList<PrivateOrder> privateOrders) {
        Matcher.privateOrders = privateOrders;
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
    public boolean validityCheck(Order newOrder) throws ExecutionException, InterruptedException {
       // System.out.println(newOrder+"This is order");
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
    public void checkMatch(Order newOrder) throws ExecutionException, InterruptedException {
        Firestore dbFirestore= FirestoreClient.getFirestore();
        CollectionReference buyCollectionReference=dbFirestore.collection("BuyOrders");
        CollectionReference sellCollectionReference=dbFirestore.collection("SellOrders");
        ApiFuture<QuerySnapshot> futureBuy=buyCollectionReference.get();
        ApiFuture<QuerySnapshot> futureSell=sellCollectionReference.get();
        QuerySnapshot referenceBuy=futureBuy.get();
        QuerySnapshot referenceSell=futureSell.get();

        if(Objects.equals(newOrder.action, "Buy")) {

            if (firebaseService.size("SellOrders") == 0) {
                firebaseService.saveBuyOrder(newOrder);

            } else {
                for (int i = 0; i < firebaseService.size("SellOrders"); i++) {
                    if (newOrder.price >= referenceSell.getDocuments().get(i).toObject(Order.class).price) {
                        buyMatch(newOrder, referenceSell.getDocuments().get(i).toObject(Order.class),referenceSell.getDocuments().get(i));

                    } else if (i == firebaseService.size("SellOrders") - 1 && newOrder.price < referenceSell.getDocuments().get(i).toObject(Order.class).price) {
                        firebaseService.saveBuyOrder(newOrder);
                    }
                }
            }
        } else if (Objects.equals(newOrder.action, "Sell")){
            System.out.println("enters the sell function");
            if(firebaseService.size("BuyOrders")==0){
                firebaseService.saveSellOrder(newOrder);
                System.out.println("gets to the if statement");
            } else {
                for(int i=0; i<firebaseService.size("BuyOrders");i++){
                    if(newOrder.price<= referenceBuy.getDocuments().get(i).toObject(Order.class).price){

                        sellMatch(newOrder,referenceBuy.getDocuments().get(i).toObject(Order.class),referenceBuy.getDocuments().get(i));

                        System.out.println(referenceBuy.getDocuments().get(i));
                        System.out.println(referenceBuy.getDocuments().get(i).getClass()+"THIS IS THE CLASS OF THE ORDER");
                        break;
                    } else if(i==firebaseService.size("BuyOrders")-1 && newOrder.price>referenceBuy.getDocuments().get(i).toObject(Order.class).price){

                        firebaseService.saveSellOrder(newOrder);
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
    public void buyMatch(Order newOrder, Order existingOrder, QueryDocumentSnapshot existingOrderObject) throws ExecutionException, InterruptedException {
        if(newOrder.quantity==existingOrder.quantity){
            createTrade(newOrder,existingOrder, newOrder.quantity);
            existingOrderObject.getReference().delete();
            //sellOrder.remove(sellOrder.indexOf(existingOrder));
        } else if(newOrder.quantity< existingOrder.quantity){
            createTrade(newOrder,existingOrder, newOrder.quantity);
            existingOrder.quantity= existingOrder.quantity- newOrder.quantity;
        } else {
            createTrade(newOrder, existingOrder, existingOrder.quantity);
            newOrder.quantity = newOrder.quantity - existingOrder.quantity;
            existingOrderObject.getReference().delete();
            //sellOrder.remove(sellOrder.indexOf(existingOrder));
            checkMatch(newOrder);
        }
    }



    /**
     * Updates the order list if there is a match
     * Adds a successfull trade to the trade list
     * @param {Order} newOrder New Order being compared.
     * @param {Order} ExistingOrder Existing Order being compared.
     **/
    public void sellMatch(Order newOrder, Order existingOrder, QueryDocumentSnapshot existingOrderObject) throws ExecutionException, InterruptedException {
        if (newOrder.quantity == existingOrder.quantity) {
            createTrade(newOrder, existingOrder, newOrder.quantity);
            existingOrderObject.getReference().delete();
           // buyOrder.remove(buyOrder.indexOf(existingOrder));
        } else if (newOrder.quantity < existingOrder.quantity) {
            createTrade(newOrder, existingOrder, newOrder.quantity);
            existingOrder.quantity = existingOrder.quantity - newOrder.quantity;
        } else {
            createTrade(newOrder, existingOrder, existingOrder.quantity);
            newOrder.quantity = newOrder.quantity - existingOrder.quantity;
            existingOrderObject.getReference().delete();
            //buyOrder.remove(buyOrder.indexOf(existingOrder));
            checkMatch(newOrder);
        }
    }


    /** Adds the orders from the buy list into an aggregated
     * list that shows the quantity of orders for a specific price
     **/
    public void aggSell() throws ExecutionException, InterruptedException {
        aggSellList.clear();
        ApiFuture<QuerySnapshot> futureSell=sellCollectionReference.get();
        QuerySnapshot referenceSell=futureSell.get();


        for(DocumentSnapshot orderObject: referenceSell){
            System.out.println(orderObject.toObject((Order.class)));
            int price= Objects.requireNonNull(orderObject.toObject(Order.class)).price;
            int quantity= Objects.requireNonNull(orderObject.toObject(Order.class)).quantity;

            if (aggSellList.get(price)==null){
                aggSellList.put(price,quantity);
                System.out.println(aggSellList+ "  THIS IS THE AGG LIST 2");
            } else{
                System.out.println(aggSellList.get(price)+"THIS IS THE PRICE");
                System.out.println(quantity+"  THIS IS THE QUANTITY");
                aggSellList.put(price,aggSellList.get(price)+quantity);
                System.out.println(aggSellList+ "  THIS IS THE AGG LIST 3");
            }
        }
    }



    /** Adds the orders from the buy list into an aggregated
     * list that shows the quantity of orders for a specific price
     **/
    public void aggBuy() throws ExecutionException, InterruptedException {
        aggBuyList.clear();
        ApiFuture<QuerySnapshot> futureBuy=buyCollectionReference.get();
        QuerySnapshot referenceBuy=futureBuy.get();


        for(DocumentSnapshot orderObject: referenceBuy){
            System.out.println(orderObject.toObject((Order.class)));
            int price= Objects.requireNonNull(orderObject.toObject(Order.class)).price;
            int quantity= Objects.requireNonNull(orderObject.toObject(Order.class)).quantity;

            if (aggBuyList.get(price)==null){
                aggBuyList.put(price,quantity);
                System.out.println(aggBuyList+ "  THIS IS THE AGG LIST 2");
            } else{
                System.out.println(aggBuyList.get(price)+"THIS IS THE PRICE");
                System.out.println(quantity+"  THIS IS THE QUANTITY");
                aggBuyList.put(price,aggBuyList.get(price)+quantity);
                System.out.println(aggBuyList+ "  THIS IS THE AGG LIST 3");
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

    public ArrayList<PrivateOrder> privateOrders(String account){
        for(int i=0; i< getBuyOrder().size();i++){
            if(getBuyOrder().get(i).account.equalsIgnoreCase(account)){
                privateOrders.add(new PrivateOrder(getBuyOrder().get(i).price,getBuyOrder().get(i).quantity,getBuyOrder().get(i).action));
            }
        }

        for(int i=0; i< getSellOrder().size();i++){
            if(getSellOrder().get(i).account.equalsIgnoreCase(account)){
                privateOrders.add(new PrivateOrder(getSellOrder().get(i).price,getSellOrder().get(i).quantity,getSellOrder().get(i).action));
            }
        }

        return privateOrders;
    }



    public void main(String[] args) throws ExecutionException, InterruptedException {
    //  firebaseService.saveBuyOrder(createOrder("Ali",5,555,"Buy")) ;




    }

}
