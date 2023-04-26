package com.zoneproduction.osjekikviz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login_page extends AppCompatActivity {

    EditText mail, password, name;
    Button SignIn;
    TextView signUp, forgotPassword;
    ProgressBar progressBarSignIn;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mail = findViewById(R.id.editTextMail);
        name = findViewById(R.id.editTextIme);
        password = findViewById(R.id.editTextPassword);
        SignIn = findViewById(R.id.buttonPrijava);
        signUp = findViewById(R.id.textViewKreirajRacun);
        forgotPassword = findViewById(R.id.textViewZaboravioSifru);
        progressBarSignIn = findViewById(R.id.progressBar);
        progressBarSignIn.setVisibility(View.INVISIBLE);

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userMail = mail.getText().toString();
                String userPassword = password.getText().toString();
                signInWithFirebase(userMail, userPassword);

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Login_page.this, Sign_up_page.class);
                startActivity(i);

            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Login_page.this, Forgot_Password.class);
                startActivity(i);

            }
        });
    }

    //korisnik se logira u sami sustav firebase
    public void signInWithFirebase(String userEmail, String userPassword) {

        progressBarSignIn.setVisibility(View.VISIBLE);
        SignIn.setClickable(false);
        auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Intent i = new Intent(Login_page.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    progressBarSignIn.setVisibility(View.INVISIBLE);
                    Toast.makeText(Login_page.this, "Prijava je uspješna", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(Login_page.this, "Morate prvo kreirati račun", Toast.LENGTH_LONG).show();
                    progressBarSignIn.setVisibility(View.INVISIBLE);
                    SignIn.setClickable(true);

                }
            }
        });
    }

    //korisnik ostaje ulogiran i ne zahtjeva ponovnu prijavu
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            Intent i = new Intent(Login_page.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}