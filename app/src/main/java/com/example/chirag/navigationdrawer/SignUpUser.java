package com.example.chirag.navigationdrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.Locale;

public class SignUpUser extends AppCompatActivity {

    private EditText FirstName;
    private EditText LastName;
    private EditText Email;
    private EditText PhoneNo;
    private EditText password1;
    private EditText password2;
    private EditText DoB;
    public int usertype = 1;
    FirebaseAuth myFirebaseAuth;
    DatabaseReference reff;
    User user;


    DatePickerDialog picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user);

        myFirebaseAuth = FirebaseAuth.getInstance();
        FirstName = (EditText) findViewById(R.id.editText);
        LastName = (EditText) findViewById(R.id.editText4);
        Email = (EditText) findViewById(R.id.editText5);
        PhoneNo = (EditText) findViewById(R.id.editText6);
        password1 = (EditText) findViewById(R.id.editText7);
        password2 = (EditText) findViewById(R.id.editText8);
        DoB = (EditText) findViewById(R.id.editTextdob) ;
        DoB.setInputType(InputType.TYPE_NULL);
        DoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(SignUpUser.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                DoB.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();

            }
        });

        final Button signUp = (Button) findViewById(R.id.button);
        TextView login = (TextView) findViewById(R.id.textView2);
        user = new User();
        reff= FirebaseDatabase.getInstance().getReference().child("User");

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName= FirstName.getText().toString();
                String lName= LastName.getText().toString();
                String email= Email.getText().toString();
                String pwd1= password1.getText().toString();
                String pwd2= password2.getText().toString();
                String DateofBirth=DoB.getText().toString();
                Long phone = Long.parseLong(PhoneNo.getText().toString().trim());

                if (fName.isEmpty()){
                    FirstName.setError("Provide First Name");
                    FirstName.requestFocus();
                } else if (lName.isEmpty()){
                    LastName.setError("Provide Last Name");
                    LastName.requestFocus();
                } else if (email.isEmpty()) {
                    Email.setError("Provide Email");
                    Email.requestFocus();
                } else if (pwd1.isEmpty()){
                    password1.setError("Provide Password");
                    password1.requestFocus();
                }else if (pwd2.isEmpty()){
                    password2.setError("Re enter the password");
                    password2.requestFocus();
                }
                else if (Long.toString(phone).length()!=10){
                    PhoneNo.setError("Enter a valid phone number");
                    PhoneNo.requestFocus();
                }
                else if (pwd1.equals(pwd2)){

                    user.setFirstName(FirstName.getText().toString().trim());
                    user.setLastName(LastName.getText().toString().trim());
                    user.setEmail(email);
                    user.setphoneNumber(phone);
                    user.setPassword1(pwd1);
                    user.setPassword2(pwd2);
                    user.setDob(DateofBirth);
                    user.setUsertype(usertype);
                    String key=reff.child("User").push().getKey();
                    reff.child(key).setValue(user);

                    Toast.makeText(SignUpUser.this, "Database Appended", Toast.LENGTH_SHORT).show();
                    myFirebaseAuth.createUserWithEmailAndPassword(email , pwd2).addOnCompleteListener(SignUpUser.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(SignUpUser.this, "SignUp error occurred",Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(SignUpUser.this, "Account has been made",Toast.LENGTH_LONG).show();
                                FirebaseAuth.getInstance().signOut();
                                Intent intToLogIn= new Intent(SignUpUser.this , LoginActivity.class);
                                startActivity(intToLogIn);
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(SignUpUser.this, "Error Occurred",Toast.LENGTH_LONG).show();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent (SignUpUser.this , LoginActivity.class);
                startActivity(i);
            }
        });
    }


}
