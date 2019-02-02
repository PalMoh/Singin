package com.techandlearn.singin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView tvUserName;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null){
            updateUI(currentUser);
        }else {
            startActivity(new Intent(this,LoginActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        tvUserName = findViewById(R.id.tvUserName);
    }

    private void updateUI(FirebaseUser firebaseUser){
        String userName = firebaseUser.getDisplayName();
        tvUserName.setText(String.format("Welcome :%s", userName));
    }
}
