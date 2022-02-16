package com.zoneproduction.osjekikviz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pl.droidsonroids.gif.GifImageView;

public class Score_Page extends AppCompatActivity {

    TextView scoreCorrect, scoreWrong, position;
    GifImageView gif;
    Button playAgain, exit;

    String userCorrect;
    String userWrong;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference().child("scores");

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score__page);

        scoreCorrect = findViewById(R.id.textViewAnswersCorrect);
        scoreWrong = findViewById(R.id.textViewAnswerWrong);
        playAgain = findViewById(R.id.buttonPlayAgain);
        position = findViewById(R.id.textViewPosition);
        exit = findViewById(R.id.buttonExit);
        gif = findViewById(R.id.gifImageView);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String userUID = user.getUid();
                userCorrect = snapshot.child(userUID).child("correct").getValue().toString();
                userWrong = snapshot.child(userUID).child("wrong").getValue().toString();
                if(Integer.parseInt(userCorrect) < 10){
                    gif.setImageResource(R.drawable.lose);
                }else{
                    gif.setImageResource(R.drawable.congrats);
                }
                scoreCorrect.setText(userCorrect);
                scoreWrong.setText(userWrong);

                //vraćam broj korisnika
                int counter = (int) snapshot.getChildrenCount();
                String userCounter = String.valueOf(counter);
                position.setText(userCounter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Score_Page.this, MainActivity.class);
                startActivity(i);
                finish();

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }
}