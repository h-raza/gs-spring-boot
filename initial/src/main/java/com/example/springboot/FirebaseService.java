package com.example.springboot;


import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firestore.v1.Document;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseService {
    public Order saveBuyOrder(Order order) throws ExecutionException, InterruptedException {

        Firestore dbFfirestore= FirestoreClient.getFirestore();
        System.out.println(order+"This is the order");
        ApiFuture<WriteResult> collectionsApiFuture= dbFfirestore.collection("BuyOrders").document(
                order.account).set(order);
        System.out.println("GOT TO THE SERVICE BIT");
       return order;
    }
    public Order saveSellOrder(Order order) throws ExecutionException, InterruptedException {

        Firestore dbFfirestore= FirestoreClient.getFirestore();
        System.out.println(order+"This is the order");
        ApiFuture<WriteResult> collectionsApiFuture= dbFfirestore.collection("SellOrders").document(
                order.account).set(order);
        System.out.println("GOT TO THE SERVICE BIT");
        return order;
    }

    public Order getOrder(String name) throws ExecutionException, InterruptedException {

        Firestore dbFfirestore= FirestoreClient.getFirestore();
        DocumentReference documentReference=dbFfirestore.collection("BuyOrders").document(name);
        ApiFuture<DocumentSnapshot> future= documentReference.get();

        DocumentSnapshot document=future.get();
        System.out.println(document);


        if(document.exists()){
            Order order=document.toObject(Order.class);
            return order;
        }else{
            return null;
        }
    }


    public void loop(String collection) throws ExecutionException, InterruptedException {
        Firestore dbFirestore=FirestoreClient.getFirestore();
        CollectionReference collectionReference=dbFirestore.collection(collection);
        ApiFuture<QuerySnapshot> future=collectionReference.get();
        QuerySnapshot reference=future.get();
        for(DocumentSnapshot document: reference){
            System.out.println(document);
        }
       // System.out.println(reference.getDocuments().get(0).toObject(Order.class));


    }

    public void postArray(ArrayList<Order>  orderList ) throws ExecutionException, InterruptedException {
        System.out.println("got to here");
             Firestore dbFirestore=FirestoreClient.getFirestore();
        CollectionReference collectionReference=dbFirestore.collection("BuyOrders");
        ApiFuture<QuerySnapshot> future=collectionReference.get();
        QuerySnapshot reference=future.get();
        for(DocumentSnapshot orderObject: reference){

            ApiFuture<WriteResult> deletion=dbFirestore.collection("BuyOrders").document(Objects.requireNonNull(orderObject.toObject(Order.class)).account).delete();
        }

        for (Order order : orderList) {
            ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("BuyOrders").document(
                    order.account).set(order);
        }

    }
}
