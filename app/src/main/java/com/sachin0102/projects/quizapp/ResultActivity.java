package com.sachin0102.projects.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sachin0102.projects.quizapp.databinding.ActivityResultBinding;

public class ResultActivity extends AppCompatActivity {

    ActivityResultBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int correctAnswers = getIntent().getIntExtra("correct", 0);
        int totalQuestions = getIntent().getIntExtra("total",0);

        binding.score.setText(String.format("%d/%d",correctAnswers,totalQuestions));

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("points", FieldValue.increment(correctAnswers));

    }
}