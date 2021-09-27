package com.example.springboot;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;



@Service
public class FirebaseInitialize {
    @PostConstruct
    public void initialize(){
        try{
        FileInputStream serviceAccount =
                new FileInputStream("C:\\Users\\mraza\\OneDrive - Scott Logic Ltd\\Desktop\\serviceAccountKey.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (IOException e){
            e.printStackTrace();
        }

    }
}
