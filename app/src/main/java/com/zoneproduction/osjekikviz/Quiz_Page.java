package com.zoneproduction.osjekikviz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Quiz_Page extends AppCompatActivity {

    TextView time, correct, wrong;
    TextView question;
    TextView a, b ,c ,d;
    Button next, finish;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference().child("Questions");

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    DatabaseReference databaseReferenceSecond = database.getReference();

    String quizQuestion;
    String quizAnswerA;
    String quizAnswerB;
    String quizAnswerC;
    String quizAnswerD;
    String quizCorrectAnswer;
    int questionCount;
    int questionNumber = 1;

    String userAnswer;
    int userCorrect = 0;
    int userWrong = 0;

    private FrameLayout adContainerView;
    private AdView adView;

    CountDownTimer countDownTimer;
    private static final long TOTAL_TIME = 26000;
    Boolean timerContinue;
    long timeLeft = TOTAL_TIME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz__page);

        //ad
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        //get the reference to your FrameLayout
        adContainerView = findViewById(R.id.adView_container);

        //Create an AdView and put it into your FrameLayout
        adView = new AdView(this);
        adContainerView.addView(adView);
        adView.setAdUnitId("ca-app-pub-9808535698128800/3436184144");
        loadBanner();

        //************************************

        time = findViewById(R.id.textViewTime);
        correct = findViewById(R.id.textViewCorrect);
        wrong = findViewById(R.id.textViewWrong);

        question = findViewById(R.id.textViewQuestion);
        a = findViewById(R.id.textViewA);
        b = findViewById(R.id.textViewB);
        c = findViewById(R.id.textViewC);
        d = findViewById(R.id.textViewD);

        next = findViewById(R.id.buttonNext);
        finish = findViewById(R.id.buttonFinish);
        finish.setClickable(false);


        game();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
                game();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendScore();
                Intent i = new Intent(Quiz_Page.this, Score_Page.class);
                startActivity(i);
                finish();
            }
        });

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
                userAnswer = "a";
                if(quizCorrectAnswer.equals(userAnswer)){
                    a.setBackground(getDrawable(R.drawable.mytextviewcorrect));
                    userCorrect++;
                    correct.setText(""+userCorrect);
                }else{
                    a.setBackground(getDrawable(R.drawable.mytextviewwrong));
                    userWrong++;
                    wrong.setText("" + userWrong);
                    findAnswer();
                }
                a.setClickable(false);
                b.setClickable(false);
                c.setClickable(false);
                d.setClickable(false);
                next.setClickable(true);
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
                userAnswer = "b";
                if(quizCorrectAnswer.equals(userAnswer)){
                    b.setBackground(getDrawable(R.drawable.mytextviewcorrect));
                    userCorrect++;
                    correct.setText(""+userCorrect);
                }else{
                    b.setBackground(getDrawable(R.drawable.mytextviewwrong));
                    userWrong++;
                    wrong.setText("" + userWrong);
                    findAnswer();
                }
                a.setClickable(false);
                b.setClickable(false);
                c.setClickable(false);
                d.setClickable(false);
                next.setClickable(true);
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
                userAnswer = "c";
                if(quizCorrectAnswer.equals(userAnswer)){
                    c.setBackground(getDrawable(R.drawable.mytextviewcorrect));
                    userCorrect++;
                    correct.setText(""+userCorrect);
                }else{
                    c.setBackground(getDrawable(R.drawable.mytextviewwrong));
                    userWrong++;
                    wrong.setText("" + userWrong);
                    findAnswer();
                }
                a.setClickable(false);
                b.setClickable(false);
                c.setClickable(false);
                d.setClickable(false);
                next.setClickable(true);
            }
        });

        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
                userAnswer = "d";
                if(quizCorrectAnswer.equals(userAnswer)){
                    d.setBackground(getDrawable(R.drawable.mytextviewcorrect));
                    userCorrect++;
                    correct.setText(""+userCorrect);
                }else{
                    d.setBackground(getDrawable(R.drawable.mytextviewwrong));
                    userWrong++;
                    wrong.setText("" + userWrong);
                    findAnswer();
                }
                a.setClickable(false);
                b.setClickable(false);
                c.setClickable(false);
                d.setClickable(false);
                next.setClickable(true);
            }
        });
    }

    public void game(){

        next.setClickable(false);
        startTimer();
        a.setBackground(getDrawable(R.drawable.mytextview));
        b.setBackground(getDrawable(R.drawable.mytextview));
        c.setBackground(getDrawable(R.drawable.mytextview));
        d.setBackground(getDrawable(R.drawable.mytextview));
        a.setClickable(true);
        b.setClickable(true);
        c.setClickable(true);
        d.setClickable(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                questionCount = (int) dataSnapshot.getChildrenCount();

                quizQuestion = dataSnapshot.child(String.valueOf(questionNumber)).child("q").getValue().toString();
                quizAnswerA = dataSnapshot.child(String.valueOf(questionNumber)).child("a").getValue().toString();
                quizAnswerB = dataSnapshot.child(String.valueOf(questionNumber)).child("b").getValue().toString();
                quizAnswerC = dataSnapshot.child(String.valueOf(questionNumber)).child("c").getValue().toString();
                quizAnswerD = dataSnapshot.child(String.valueOf(questionNumber)).child("d").getValue().toString();
                quizCorrectAnswer = dataSnapshot.child(String.valueOf(questionNumber)).child("answer").getValue().toString();

                question.setText(quizQuestion);
                a.setText(quizAnswerA);
                b.setText(quizAnswerB);
                c.setText(quizAnswerC);
                d.setText(quizAnswerD);
                if(questionNumber < questionCount){
                    questionNumber++;
                }else{
                    Toast.makeText(Quiz_Page.this, "Odgovorili ste na sva pitanja", Toast.LENGTH_LONG).show();
                    finish.setVisibility(View.VISIBLE);
                    next.setClickable(false);
                    finish.setClickable(true);
                    next.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Quiz_Page.this, "Ispričavamo se, nastao je problem", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void findAnswer(){

        switch (quizCorrectAnswer) {
            case "a":
                a.setBackground(getDrawable(R.drawable.mytextviewcorrect));
                break;
            case "b":
                b.setBackground(getDrawable(R.drawable.mytextviewcorrect));
                break;
            case "c":
                c.setBackground(getDrawable(R.drawable.mytextviewcorrect));
                break;
            case "d":
                d.setBackground(getDrawable(R.drawable.mytextviewcorrect));
                break;
        }
    }

    public void startTimer(){

        countDownTimer = new CountDownTimer(timeLeft,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                timeLeft = millisUntilFinished;
                updateCountDownText();

            }

            @Override
            public void onFinish() {
                timerContinue = false;
                pauseTimer();
                question.setText("Vrijeme je isteklo, iduće pitanje");
                userWrong++;
                wrong.setText("" + userWrong);
            }
        }.start();
        timerContinue = true;
    }

    public void resetTimer(){

        timeLeft = TOTAL_TIME;
        updateCountDownText();
    }

    public void updateCountDownText(){

        int second = (int) (timeLeft / 1000) % 60;
        time.setText("" + second);

    }

    public void pauseTimer(){

        countDownTimer.cancel();
        timerContinue = false;

    }

    public void sendScore(){

        String userUID = user.getUid();
        databaseReferenceSecond.child("scores").child(userUID).child("correct").setValue(userCorrect).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(Quiz_Page.this, "Uspješno ste završili kviz", Toast.LENGTH_SHORT).show();

            }
        });
        databaseReferenceSecond.child("scores").child(userUID).child("wrong").setValue(userWrong);
    }

    private AdSize getAdSize() {
        //Determine the screen width to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        //you can also pass your selected width here in dp
        int adWidth = (int) (widthPixels / density);

        //return the optimal size depends on your orientation (landscape or portrait)
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    private void loadBanner() {
        AdRequest adRequest = new AdRequest.Builder().build();

        AdSize adSize = getAdSize();
        // Set the adaptive ad size to the ad view.
        adView.setAdSize(adSize);

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

}