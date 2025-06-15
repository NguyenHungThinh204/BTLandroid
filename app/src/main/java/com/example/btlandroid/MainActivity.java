package com.example.btlandroid;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    //test committt
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find the button and set click listener
        com.google.android.material.button.MaterialButton openFragmentButton = findViewById(R.id.openFragmentButton);
        openFragmentButton.setOnClickListener(v -> {
            // Create an instance of PostShareFragment
            PostShareFragment postShareFragment = PostShareFragment.newInstance("param1", "param2");

            // Begin the fragment transaction
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            // Replace the current content with the fragment
            fragmentTransaction.replace(R.id.main, postShareFragment);
            fragmentTransaction.addToBackStack(null); // Optional: allows back navigation
            fragmentTransaction.commit();
        });
    }
}