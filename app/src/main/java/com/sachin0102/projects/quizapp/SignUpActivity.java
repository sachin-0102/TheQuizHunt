package com.sachin0102.projects.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sachin0102.projects.quizapp.databinding.ActivitySignUpBinding;

import org.jetbrains.annotations.NotNull;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore database;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("Creating Account");
        dialog.setCancelable(false);


        binding.createNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email , password,name;
                name = binding.nameBox.getText().toString();
                email= binding.emailBox.getText().toString();
                password=binding.passwordBox.getText().toString();

                User user = new User(name,email,password);

                dialog.show();
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String uid = task.getResult().getUser().getUid();
                            database.collection("users")
                                    .document(uid)
                                    .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {

                                    dialog.dismiss();
                                    if(task.isSuccessful()){
                                        startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            Toast.makeText(SignUpActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            dialog.dismiss();
                            Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });
    }
}