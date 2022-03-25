package com.project.of.busnavigationsystem.Signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.androidbuts.multispinnerfilter.MultiSpinnerSearch;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.of.busnavigationsystem.NavBar.Profile.Driver;
import com.project.of.busnavigationsystem.Login.LoginActivity;
import com.project.of.busnavigationsystem.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class SignUpBus extends AppCompatActivity {

    private EditText FirstName;
    private EditText LastName;
    private EditText PhoneNo;
    private EditText Email;
    private MultiSpinnerSearch BusRoute;
    private EditText BusNo;
    private EditText password1;
    private EditText password2;
    private EditText DoB;
    public int usertype = 2;
    FirebaseAuth myFirebaseAuth;
    DatabaseReference refe;
    Driver driver;
    DatePickerDialog picker;
    List<String> selectedRoutes;
    String[] selectedRoutesArray;
    FirebaseUser curruser;
    public static Long phone;


    @SuppressLint("ClickableViewAccessibility")
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
        BusRoute = (MultiSpinnerSearch) findViewById(R.id.editTextbusroute);
        password1 = (EditText) findViewById(R.id.editTextpwd);
        password2 = (EditText) findViewById(R.id.editTextpwdreenter);
        DoB = (EditText) findViewById(R.id.editTextdob);
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
                                DoB.setError(null);
                            }
                        }, year, month, day);
                picker.show();

            }
        });

        final String[] routeList = {
                "Lagankhel-Jawalakhel-Kupondol-Tripureshor",
                "Lagankhel-Kupondol-Thapathali",
                "Bungamati-Bhaisipati-Charghare",
                "Attarkhel-Purano Buspark",
                "Bagbazaar-Ratnapark",
                "Bagbazaar-Kamalbinayak",
                "Balaju-Ratnapark",
                "Balkhu-NayaBaneshwor-Sankhamul",
                "Balkhu-NayaBaneshwor-Shantinagar-Bhatkya Pul",
                "Balkumari-Gopi Krishna",
                "Bhaktapur-Purano Thimi-Purano Bus Park",
                "Budhanilkantha School-Ratna Park",
                //"Chakrapath Parikrama",
                "Changu-Ratnapark",
                "Ratnapark-Samakhusi Chowk",
                "Boudha-Dillibazaar",
                "Putalisadak-Pulchowk"

        };
        java.util.Arrays.sort(routeList);
        final List<String> list = Arrays.asList(routeList);
        final List<KeyPairBoolData> listArray = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            KeyPairBoolData h = new KeyPairBoolData();
            h.setId(i + 1);
            h.setName(list.get(i));
            h.setSelected(false);
            listArray.add(h);
        }
        selectedRoutes = new ArrayList<>();
        /*BusRoute.setItems(listArray, -1, new SpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> items) {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isSelected()) {
                        selectedRoutes.add(items.get(i).getName().toString());
                    }
                }
                selectedRoutesArray = selectedRoutes.toArray(new String[selectedRoutes.size()]);

            }
        });*/

        BusRoute.setEmptyTitle("No Data Found!");
        BusRoute.setSearchHint("Search route");
        BusRoute.setSearchEnabled(true);
        BusRoute.setShowSelectAllButton(true);
        BusRoute.setItems(listArray, new MultiSpinnerListener() {
            @Override
            public void onItemsSelected(List<KeyPairBoolData> selectedItems) {
                for (int i = 0; i < selectedItems.size(); i++) {
                    if (selectedItems.get(i).isSelected()) {
                        selectedRoutes.add(selectedItems.get(i).getName().toString());
                    }
                }
                selectedRoutesArray = selectedRoutes.toArray(new String[selectedRoutes.size()]);
            }
        });


        Button signUp = (Button) findViewById(R.id.button2);
        TextView login = (TextView) findViewById(R.id.textView6);
        driver = new Driver();
        refe = FirebaseDatabase.getInstance().getReference().child("Driver");

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = FirstName.getText().toString();
                String lName = LastName.getText().toString();
                String email = Email.getText().toString();
                String busNo = BusNo.getText().toString();
                String pwd1 = password1.getText().toString();
                String pwd2 = password2.getText().toString();
                String DateofBirth = DoB.getText().toString();
                if (!PhoneNo.getText().toString().trim().equals("")) {
                    phone = Long.parseLong(PhoneNo.getText().toString().trim());
                }                HashMap<String, String> busRoute = new HashMap<String, String>();
                if (selectedRoutesArray!=null) {
                    for (int i = 0; i < selectedRoutesArray.length; i++) {
                        busRoute.put("R" + (i + 1), selectedRoutesArray[i]);
                    }
                }

                if (fName.isEmpty()) {
                    FirstName.setError("Provide First Name");
                    FirstName.requestFocus();
                } else if (lName.isEmpty()) {
                    LastName.setError("Provide Last Name");
                    LastName.requestFocus();
                } else if (email.isEmpty()) {
                    Email.setError("Provide Email");
                    Email.requestFocus();
                } else if (busNo.isEmpty()) {
                    LastName.setError("Provide BusNo");
                    LastName.requestFocus();
                } else if (busRoute.isEmpty() || busRoute.containsKey(null)) {
                    Toast.makeText(SignUpBus.this, "Bus Route is required!", Toast.LENGTH_SHORT).show();
                    BusRoute.requestFocus();
                } else if (pwd1.isEmpty()) {
                    password1.setError("Provide Password");
                    password1.requestFocus();
                } else if (pwd2.isEmpty()) {
                    password2.setError("Re enter the password");
                    password2.requestFocus();
                } else if (phone == null) {
                    PhoneNo.setError("Enter a valid phone number");
                    PhoneNo.requestFocus();
                } else if (phone != null && ((Long.toString(phone).length() != 10))) {
                    PhoneNo.setError("Enter a valid phone number");
                    PhoneNo.requestFocus();
                } else if (PhoneNo.getText().toString().isEmpty()) {
                    PhoneNo.setError("Enter a valid phone number");
                    PhoneNo.requestFocus();
                } else if (!pwd1.equals(pwd2)) {
                    password2.setError("Passwords doesn't match!");
                    password2.requestFocus();
                } else if (DoB.getText().toString().isEmpty()) {
                    DoB.setError("Select your Date of Birth!");
                    DoB.requestFocus();
                } else if (!pwd1.equals(pwd2)) {
                    password1.setError("Passwords doesn't match!");
                    password2.setError("Passwords doesn't match!");
                    password2.requestFocus();
                    password1.requestFocus();
                } else if (pwd1.equals(pwd2)) {
                    driver.setFirstName(FirstName.getText().toString().trim());
                    driver.setLastName(LastName.getText().toString().trim());
                    driver.setEmail(email);
                    driver.setPhoneNumber(phone);
                    driver.setPassword1(pwd1);
                    driver.setPassword2(pwd2);
                    driver.setBusNo(busNo);
                    driver.setDob(DateofBirth);
                    driver.setUsertype(usertype);


                    String key = refe.child("Driver").push().getKey();
                    refe.child(key).setValue(driver);
                    refe.child(key).child("busRoute").setValue(selectedRoutes);


                    //Toast.makeText(SignUpBus.this, "Database Appended for Bus Driver", Toast.LENGTH_SHORT).show();
                    myFirebaseAuth.createUserWithEmailAndPassword(email, pwd2).addOnCompleteListener(SignUpBus.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignUpBus.this, "SignUp error occurred", Toast.LENGTH_LONG).show();
                            } else {
                                //curruser=FirebaseAuth.getInstance().getCurrentUser();
                                //FirebaseAuth.getInstance().signOut();
                                Intent intToLogIn = new Intent(SignUpBus.this, LoginActivity.class);
                                startActivity(intToLogIn);
                                //Toast.makeText(SignUpBus.this, "Account has been made", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignUpBus.this, "Error Occurred", Toast.LENGTH_LONG).show();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpBus.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }


}
