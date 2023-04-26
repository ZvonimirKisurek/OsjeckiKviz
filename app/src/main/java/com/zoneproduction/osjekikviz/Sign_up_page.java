package com.zoneproduction.osjekikviz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Sign_up_page extends AppCompatActivity {

    EditText mail;
    EditText password;
    Button signUp;
    ProgressBar progressBar;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        mail = findViewById(R.id.editTextSignUpMail);
        password = findViewById(R.id.editTextSignUpPassword);
        signUp = findViewById(R.id.buttonSignUp);
        progressBar = findViewById(R.id.progressBarSignUp);
        progressBar.setVisibility(View.INVISIBLE);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUp.setClickable(false);
                String userEmail = mail.getText().toString();
                String userPassword = password.getText().toString();
                signUpFirebase(userEmail, userPassword);

            }
        });
    }

    //Kreiranje korisničkog računa
    public void signUpFirebase(String userEmail, String userPassword){

        progressBar.setVisibility(View.VISIBLE);
        auth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    Toast.makeText(Sign_up_page.this, "Vaš račun je kreiran", Toast.LENGTH_LONG).show();
                    finish();
                    progressBar.setVisibility(View.INVISIBLE);

                }else{

                    Toast.makeText(Sign_up_page.this, "Nastao je problem!", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}