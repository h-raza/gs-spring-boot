package com.example.springboot;

public class Trade {
    public Order newOrder;
    public Order existingOrder;
    public String newOrderAccount;
    public String existingOrderAccount;
    public int newOrderPrice;
    public int existingOrderPrice;
    public int quantity; // Quantity of the stocks that were bought in the successful trade

    public Trade(Order newOrder,Order existingOrder, int quantity){
        this.newOrder=newOrder ;
        this.existingOrder=existingOrder;
        this.newOrderAccount=newOrder.account;
        this.existingOrderAccount=existingOrder.account;
        this.newOrderPrice= newOrder.price;
        this.existingOrderPrice= existingOrder.price;
        this.quantity=quantity;
    }

    @Override
    public String toString() {
        return "Trade{" +
                "newOrder=" + newOrder +
                ", existingOrder=" + existingOrder +
                ", quantity=" + quantity +
                '}';
    }
}

