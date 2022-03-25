package com.project.of.busnavigationsystem.NavBar.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.project.of.busnavigationsystem.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class readdriver extends AppCompatActivity {


    DatabaseReference databaseReference;
    FirebaseUser user;
    public static List<String> driverRoutes;
    String uid;
    FirebaseAuth myFirebaseAuth;
    EditText fullname, email, contact, dob, busNumber, busRoute;
    TextView driverFullName, driverEmail;
    String routesString;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readdriver);
        driverFullName = findViewById(R.id.driverFullName);
        driverEmail = findViewById(R.id.driverEmail);
        fullname = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        contact = findViewById(R.id.contact);
        dob = findViewById(R.id.DOB);
        busNumber = findViewById(R.id.busNumber);
        busRoute = findViewById(R.id.busRoute);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        //currentuser reference and getting the uid of the current user
        myFirebaseAuth = FirebaseAuth.getInstance();
        if (myFirebaseAuth.getCurrentUser() != null) {
            user = myFirebaseAuth.getCurrentUser();
            uid = user.getUid();
        }
        //initializing driverRoutes list
        driverRoutes = new ArrayList<>();
        //reference to the node pointing to Driver
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Driver");

        databaseReference.orderByChild("email").equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Driver driver = new Driver();
                    Log.d("DRIVER", "onDataChange: " + snapshot);
                    driver = child.getValue(Driver.class);
                    driverEmail.setText(driver.getEmail());
                    driverFullName.setText(driver.getFirstName() + "" + driver.getLastName());
                    fullname.setText(driver.getFirstName() + "" + driver.getLastName());
                    email.setText(driver.getEmail());
                    contact.setText(driver.getPhoneNumber().toString());
                    dob.setText(driver.getDob());
                    busNumber.setText(driver.getBusNo());
                    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                    };
                    List<String> routesList = snapshot.child(child.getKey()).child("busRoute").getValue(t);
                    driverRoutes.addAll(routesList);
                    if (routesString == null) {
                        routesString = "";
                        for (int i = 0; i < driverRoutes.size(); i++) {
                            routesString += driverRoutes.get(i) + "\n";
                        }
                    }
                    busRoute.setText(routesString);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


       /* //ValueEventListener for triggering the event only once
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("db", "onDataChange CHECKING DATASNAPSHOT: "+ dataSnapshot);

                //clearing the list before inserting data into it
                itemlist.clear();
                //looping the children to get the keys
                for (final DataSnapshot keyNode:dataSnapshot.getChildren()){
                    //Log.d("CHECKING", "onDataChange: "+keyNode);
                    //taking reference from the key to get inside that node
                    databaseReference.child(keyNode.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //get all the users
                            User testuser = keyNode.getValue(User.class);
                            if (user!=null) {
                                useremail = user.getEmail();

                                //verified using the email we get the current user
                                if (useremail.equals(testuser.getEmail())) {
                                    //if we get the snapshot of the user we need, save the key to currentuser
                                    currentuserkey = keyNode.getKey();
                                    //take the reference from the currentuserkey
                                    databaseReference.child(currentuserkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            //make a  User type data for storing the obtained value
                                            currentuser = dataSnapshot.getValue(Driver.class);
                                            //retrieving route list of driver
                                            databaseReference.child(currentuserkey + "/busRoute").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
                                                    };
                                                    List<String> routesList = dataSnapshot.getValue(t);
                                                    driverRoutes.addAll(routesList);
                                                    //adding the data using the getter into the list
                                                    itemlist.add("Name: " + currentuser.getFirstName() + " " + currentuser.getLastName());
                                                    itemlist.add("E-mail: " + currentuser.getEmail());
                                                    itemlist.add("Contact: " + currentuser.getPhoneNumber());
                                                    itemlist.add("Date of Birth: " + currentuser.getDob());
                                                    itemlist.add("Bus Number: " + currentuser.getBusNo());
                                                    if (routesString == null) {
                                                        routesString = "";
                                                        for (int i = 0; i < driverRoutes.size(); i++) {
                                                            routesString += "" + driverRoutes.get(i) + "\n\t\t\t\t\t\t\t\t\t\t";
                                                        }
                                                    }
                                                    if (routesString != null) {
                                                        itemlist.add("Bus Route(s): " + routesString);
                                                    }

                                                    //adapter for list
                                                    adapter = new ArrayAdapter<>(readdriver.this, android.R.layout.simple_list_item_1, itemlist);
                                                    l1.setAdapter(adapter);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                }
                                            });


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReference.addListenerForSingleValueEvent(valueEventListener);*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}


