package com.sachin0102.projects.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.sachin0102.projects.quizapp.databinding.ActivityQuizBinding;

import java.util.ArrayList;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    ActivityQuizBinding binding;

    ArrayList<Question> questions;
    Question question;
    int index =0;
    CountDownTimer timer;
    FirebaseFirestore database;
    int correctAnswers =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Random random = new Random();
        questions = new ArrayList<>();
        database = FirebaseFirestore.getInstance();
        final String categoryId = getIntent().getStringExtra("categoryId");
        Toast.makeText(this, categoryId, Toast.LENGTH_SHORT).show();
        database.collection("categories")
                .document(categoryId)
                .collection("questions")
                .whereGreaterThanOrEqualTo("index",2)
                .orderBy("index")
                .limit(5).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots.getDocuments().size()<5){

                    database.collection("categories")
                            .document(categoryId)
                            .collection("questions")
                            .whereLessThanOrEqualTo("index",2)
                            .orderBy("index")
                            .limit(1).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for(DocumentSnapshot snapshot:queryDocumentSnapshots){
                                Question question = snapshot.toObject(Question.class);
                                questions.add(question);
                            }
                            setNextQuestion();
                        }
                    });
                } else{
                    for(DocumentSnapshot snapshot:queryDocumentSnapshots){
                        Question question = snapshot.toObject(Question.class);
                        questions.add(question);
                    }
                    setNextQuestion();

                }
            }
        });



        resetTimer();
        setNextQuestion();

    }
    void setNextQuestion(){
        resetTimer();

        if(timer!=null)
        {
            timer.cancel();
        }
        timer.start();
        if(index<questions.size()){
            binding.questionCounter.setText(String.format("%d/%d",(index+1),(questions.size())));
            question = questions.get(index);
            binding.question.setText(question.getQuestion());
            binding.option1.setText(question.getOption1());
            binding.option2.setText(question.getOption2());
            binding.option3.setText(question.getOption3());
            binding.option4.setText(question.getOption4());
        }
    }

    void checkAnswer(TextView textView){
        String selectedAnswer = textView.getText().toString();
        if(selectedAnswer.equals(question.getAnswer())){
            correctAnswers++;
            textView.setBackground(getResources().getDrawable(R.drawable.option_right));
        }
        else
        {
            textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
        }
    }

    void resetTimer(){
        timer = new CountDownTimer(30200,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.timer.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {

            }
        };
    }

    void showAnswer(){
        if(question.getAnswer().equals(binding.option1.getText().toString())){
            binding.option1.setBackground(getResources().getDrawable(R.drawable.option_right));
        }
        if(question.getAnswer().equals(binding.option2.getText().toString())){
            binding.option2.setBackground(getResources().getDrawable(R.drawable.option_right));
        }
        if(question.getAnswer().equals(binding.option3.getText().toString())){
            binding.option3.setBackground(getResources().getDrawable(R.drawable.option_right));
        }
        if(question.getAnswer().equals(binding.option4.getText().toString())){
            binding.option4.setBackground(getResources().getDrawable(R.drawable.option_right));
        }
    }
    void reset(){
        binding.option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option4.setBackground(getResources().getDrawable(R.drawable.option_unselected));
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.option1:
            case R.id.option2:
            case R.id.option3:
            case R.id.option4:
                if(timer!=null){
                    timer.cancel();
                }
                TextView selected = (TextView) view;
                checkAnswer(selected);
                showAnswer();
                break;
            case R.id.nextButton:
                reset();

                if(index<questions.size()) {
                    index++;
                    setNextQuestion();
                }
                else
                {

                    Intent intent = new Intent(QuizActivity.this,ResultActivity.class);
                    intent.putExtra("correct",correctAnswers);
                    intent.putExtra("total",questions.size());
                    startActivity(intent);
                    finish();
                }
                break;

        }
    }
}