package com.zoneproduction.osjekikviz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    Button start,signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signOut = findViewById(R.id.buttonSignOut);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                auth.signOut();
                Intent i = new Intent(MainActivity.this, Login_page.class);
                startActivity(i);
                finish();

            }
        });

        start = findViewById(R.id.buttonStart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, Quiz_Page.class);
                startActivity(i);

            }
        });
    }


}