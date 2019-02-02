package com.techandlearn.singin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText edUserName;
    private Button btnLogin;
    private ProgressBar progressBar;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser !=null){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        initTheViews();
        bindBtnLoginOnClick();
    }

    private void initTheViews(){
        edUserName = findViewById(R.id.edUserName);
        btnLogin  = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.pBar);
    }

    private void bindBtnLoginOnClick(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edUserName.getText().toString())){
                    Toast.makeText(LoginActivity.this, "Enter user name", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String userName = edUserName.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.GONE);

                mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            updateUserName(userName,firebaseUser );
                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                            btnLogin.setVisibility(View.VISIBLE);
                            Toast.makeText(LoginActivity.this, "Some thing went wrong..", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void updateUserName(String userName , FirebaseUser firebaseUser){
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(userName)
                .build();

        firebaseUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }else {
                    progressBar.setVisibility(View.GONE);
                    btnLogin.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginActivity.this, "Some thing went wrong..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
