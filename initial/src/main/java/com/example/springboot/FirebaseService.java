package com.example.springboot;


import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firestore.v1.DeleteDocumentRequest;
import com.google.firestore.v1.Document;
import org.apache.coyote.http11.filters.IdentityOutputFilter;
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
        ApiFuture<WriteResult> collectionsApiFuture= dbFfirestore.collection("BuyOrders").document().set(order);
        //System.out.println("GOT TO THE SERVICE BIT");
       return order;
    }
    public Order saveSellOrder(Order order) throws ExecutionException, InterruptedException {

        Firestore dbFfirestore= FirestoreClient.getFirestore();
        System.out.println(order+"This is the order");
        ApiFuture<WriteResult> collectionsApiFuture= dbFfirestore.collection("SellOrders").document().set(order);
        //System.out.println("GOT TO THE SERVICE BIT");
        return order;
    }

//    public void deleteSellOrder(Order order) {
//        Firestore dbFfirestore= FirestoreClient.getFirestore();
//
//        ApiFuture<WriteResult> delete=dbFfirestore.collection("SellOrders").;
//    }

//    public Order getOrder(String name) throws ExecutionException, InterruptedException {
//
//        Firestore dbFfirestore= FirestoreClient.getFirestore();
//        DocumentReference documentReference=dbFfirestore.collection("SellOrders").document(name);
//        ApiFuture<DocumentSnapshot> future= documentReference.get();
//
//        DocumentSnapshot document=future.get();
//        System.out.println(document + "this is the document that needs deleting");
//        document.future.delete();
//
//
//
//        if(document.exists()){
//            Order order=document.toObject(Order.class);
//            return order;
//        }else{
//            return null;
//        }
//    }


    public void loop(String collection) throws ExecutionException, InterruptedException {
        Firestore dbFirestore=FirestoreClient.getFirestore();
        CollectionReference collectionReference=dbFirestore.collection(collection);
        ApiFuture<QuerySnapshot> future=collectionReference.get();
        QuerySnapshot reference=future.get();
        for(DocumentSnapshot document: reference){
            System.out.println(document.toObject(Order.class));
            System.out.println(reference.size()+"THIS SI THE SIZE OF THE ARRAY");
        }
      // System.out.println(reference.getDocuments().get(0).toObject(Order.class));


        for (int i=0; i<size(collection);i++){
            System.out.println(reference.getDocuments().get(i).toObject(Order.class).price+"This the price for the order");

        }

    }

    public int size(String collection) throws ExecutionException,InterruptedException{
        Firestore dbFirestore= FirestoreClient.getFirestore();
        CollectionReference collectionReference=dbFirestore.collection(collection);
        ApiFuture<QuerySnapshot> future=collectionReference.get();
        QuerySnapshot reference=future.get();
        return reference.size();
    }

    public void postArray( ) throws ExecutionException, InterruptedException {
        System.out.println("got to here");
             Firestore dbFirestore=FirestoreClient.getFirestore();
        CollectionReference collectionReference=dbFirestore.collection("BuyOrders");
        ApiFuture<QuerySnapshot> future=collectionReference.get();
        QuerySnapshot reference=future.get();
        for(DocumentSnapshot orderObject: reference){
            System.out.println(orderObject.toObject((Order.class)));
            orderObject.getReference().delete();
            //System.out.println(orderObject.getClass()+"THIS IS THE CLASS OF THE DODCUMENT");

           // ApiFuture<WriteResult> deletion=dbFirestore.collection("BuyOrders").document(Objects.requireNonNull(orderObject.toObject(Order.class)).account).delete();
        }

//        for (Order order : orderList) {
//            ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection("BuyOrders").document(
//                    order.account).set(order);
//        }

    }



}
