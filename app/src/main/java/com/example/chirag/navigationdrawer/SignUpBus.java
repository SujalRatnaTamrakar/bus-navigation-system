package com.example.chirag.navigationdrawer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class SignUpBus extends AppCompatActivity {

    private EditText FirstName;
    private EditText LastName;
    private EditText PhoneNo;
    private EditText Email;
    private EditText BusNo;
    private EditText BusRoute;
    private EditText password1;
    private EditText password2;
    private EditText DoB;
    public int usertype = 2;
    FirebaseAuth myFirebaseAuth;
    DatabaseReference refe;
    Driver driver;

    DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_bus);

        myFirebaseAuth = FirebaseAuth.getInstance();
        FirstName = (EditText) findViewById(R.id.editTextfirstname);
        LastName = (EditText) findViewById(R.id.editTextlastname);
        PhoneNo = (EditText) findViewById(R.id.editTextphone);
        Email = (EditText) findViewById(R.id.editTextemail);
        BusNo = (EditText) findViewById(R.id.editTextbusnumber);
        BusRoute = (EditText) findViewById(R.id.editTextbusroute);
        password1 = (EditText) findViewById(R.id.editTextpwd);
        password2 = (EditText) findViewById(R.id.editTextpwdreenter);
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
                picker = new DatePickerDialog(SignUpBus.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                DoB.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();

            }
        });

        Button signUp = (Button) findViewById(R.id.button2);
        TextView login = (TextView) findViewById(R.id.textView6);
        driver = new Driver();
        refe= FirebaseDatabase.getInstance().getReference().child("Driver");

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName= FirstName.getText().toString();
                String lName= LastName.getText().toString();
                String email= Email.getText().toString();
                String busNo = BusNo.getText().toString();
                String busRoute = BusRoute.getText().toString();
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
                } else if (email.isEmpty()){
                    Email.setError("Provide Email");
                    Email.requestFocus();
                } else if (busNo.isEmpty()) {
                    LastName.setError("Provide BusNo");
                    LastName.requestFocus();
                } else if (busRoute.isEmpty()) {
                    LastName.setError("Provide Bus Route");
                    LastName.requestFocus();
                }  else if (pwd1.isEmpty()){
                    password1.setError("Provide Password");
                    password1.requestFocus();
                } else if (pwd2.isEmpty()){
                    password2.setError("Re enter the password");
                    password2.requestFocus();
                }
                else if (Long.toString(phone).length()!=10){
                    PhoneNo.setError("Enter a valid phone number");
                    PhoneNo.requestFocus();
                } else if (pwd1.equals(pwd2)){

                    driver.setFirstName(FirstName.getText().toString().trim());
                    driver.setLastName(LastName.getText().toString().trim());
                    driver.setEmail(email);
                    driver.setPhoneNumber(phone);
                    driver.setPassword1(pwd1);
                    driver.setPassword2(pwd2);
                    driver.setBusNo(busNo);
                    driver.setBusRoute(busRoute);
                    driver.setDob(DateofBirth);
                    driver.setUsertype(usertype);
                    String key=refe.child("Driver").push().getKey();
                    refe.child(key).setValue(driver);

                    Toast.makeText(SignUpBus.this, "Database Appended for Bus Driver", Toast.LENGTH_SHORT).show();
                    myFirebaseAuth.createUserWithEmailAndPassword(email , pwd2).addOnCompleteListener(SignUpBus.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(SignUpBus.this, "SignUp error occurred",Toast.LENGTH_LONG).show();
                            } else {
                                FirebaseAuth.getInstance().signOut();
                                Intent intToLogIn= new Intent(SignUpBus.this , LoginActivity.class);
                                startActivity(intToLogIn);
                                Toast.makeText(SignUpBus.this, "Account has been made",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(SignUpBus.this, "Error Occurred",Toast.LENGTH_LONG).show();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent (SignUpBus.this , LoginActivity.class);
                startActivity(i);
            }
        });
    }
}
