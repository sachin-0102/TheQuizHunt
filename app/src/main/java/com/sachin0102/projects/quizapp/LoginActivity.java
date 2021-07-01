package com.sachin0102.projects.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.sachin0102.projects.quizapp.databinding.ActivityLoginBinding;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Logging in...");
        dialog.setCancelable(false);

        if(auth.getCurrentUser()!=null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,password;
                email = binding.emailBox.getText().toString();
                password = binding.passwordBox.getText().toString();

                dialog.show();
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        dialog.dismiss();
                        if(task.isSuccessful()){
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        binding.createNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });



    }
}