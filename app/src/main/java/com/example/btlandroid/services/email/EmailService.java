package com.example.btlandroid.services.email;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class EmailService {
    public void sendResetPasswordEmail(String email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(Task::isSuccessful);
    }
}