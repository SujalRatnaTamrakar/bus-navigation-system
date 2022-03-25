package com.project.of.busnavigationsystem.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.project.of.busnavigationsystem.MainActivities.Choose.ChooseActivity;
import com.project.of.busnavigationsystem.MainActivities.MainActivityy;
import com.project.of.busnavigationsystem.MainActivities.MainActivityy_driver;
import com.project.of.busnavigationsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText Email;
    private EditText Password;
    FirebaseAuth myFirebaseAuth;
    private FirebaseAuth.AuthStateListener myAuthStateListener;
    DatabaseReference postRef;
    FirebaseUser curruser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        //get firebaseauth reference
        myFirebaseAuth = FirebaseAuth.getInstance();

        Email = (EditText) findViewById(R.id.etname);
        Password = (EditText) findViewById(R.id.etpassword);
        final Button login = (Button) findViewById(R.id.btn);
        TextView signUp = (TextView) findViewById(R.id.textView3);

        myAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser myFirebaseUser = firebaseAuth.getCurrentUser();
                if (myFirebaseUser != null) {
                    postRef = FirebaseDatabase.getInstance().getReference().child("User");
                    postRef.orderByChild("email").equalTo(myFirebaseUser.getEmail().toString())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Intent intToMain = new Intent(LoginActivity.this, MainActivityy.class);
                                        intToMain.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intToMain);
                                    } else {
                                        Intent intToMain = new Intent(LoginActivity.this, MainActivityy_driver.class);
                                        intToMain.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intToMain);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                    //Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();


                }/* else {
                    Toast.makeText(LoginActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                }*/
            }
        };
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString();
                String pwd = Password.getText().toString();
                if (email.isEmpty() && pwd.isEmpty()) {
                    Email.setError("Provide Email");
                    Email.requestFocus();
                    Password.setError("Provide Password");
                    Password.requestFocus();
                } else if (email.isEmpty()) {
                    Email.setError("Provide Email");
                    Email.requestFocus();
                } else if (pwd.isEmpty()) {
                    Password.setError("Provide Password");
                    Password.requestFocus();
                } else {
                    myFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Login Error , check whether you entered valid email and password", Toast.LENGTH_SHORT).show();
                            } else {

                                //Check if already logged in start respective activity

                                curruser = FirebaseAuth.getInstance().getCurrentUser();
                                postRef = FirebaseDatabase.getInstance().getReference().child("User");
                                postRef.orderByChild("email").equalTo(curruser.getEmail().toString())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {

                                                                            @Override
                                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                if (dataSnapshot.exists()) {
                                                                                    Intent intToMain = new Intent(LoginActivity.this, MainActivityy.class);
                                                                                    intToMain.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                                                    startActivity(intToMain);
                                                                                    LoginActivity.this.finish();
                                                                                } else {
                                                                                    Intent intToMain = new Intent(LoginActivity.this, MainActivityy_driver.class);
                                                                                    intToMain.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                                                    startActivity(intToMain);
                                                                                    LoginActivity.this.finish();
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                            }
                                                                        }
                                        );


                            }
                        }
                    });
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToSignUp = new Intent(LoginActivity.this, ChooseActivity.class);
                startActivity(intToSignUp);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        myFirebaseAuth.addAuthStateListener(myAuthStateListener);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }
}
