package com.project.of.busnavigationsystem.NavBar.Profile;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.of.busnavigationsystem.R;

import java.util.ArrayList;
import java.util.List;

public class readuser extends AppCompatActivity {
    DatabaseReference databaseReference;
    FirebaseUser user;
    String uid;
    EditText fullname, email, contact, dob;
    TextView userFullName, userEmail;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readuser);
        userFullName = findViewById(R.id.userFullName);
        userEmail = findViewById(R.id.userEmail);
        fullname = findViewById(R.id.fullName);
        email = findViewById(R.id.email);
        contact = findViewById(R.id.contact);
        dob = findViewById(R.id.DOB);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //currentuser reference and getting the uid of the current user
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            user = FirebaseAuth.getInstance().getCurrentUser();
            uid = user.getUid();
        }

        //reference to the node pointing to User
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

        databaseReference.orderByChild("email").equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    User user = new User();
                    user = child.getValue(User.class);
                    userEmail.setText(user.getEmail());
                    userFullName.setText(user.getFirstName() + "" + user.getLastName());
                    fullname.setText(user.getFirstName() + "" + user.getLastName());
                    email.setText(user.getEmail());
                    contact.setText(user.getphoneNumber().toString());
                    dob.setText(user.getDob());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        

        /*//ValueEventListener for triggering the event only once
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
                                String useremail = user.getEmail();
                                //verified using the email we get the current user
                                if (useremail.equals(testuser.getEmail())) {
                                    //if we get the snapshot of the user we need, save the key to currentuser
                                    currentuserkey = keyNode.getKey();
                                    //take the reference from the currentuserkey
                                    databaseReference.child(currentuserkey).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            //make a  User type data for storing the obtained value
                                            User currentuser = dataSnapshot.getValue(User.class);
                                            //testView_test.setText(currentuser.getFirstName());

                                            //adding the data using the getter into the list
                                            itemlist.add("Name: " + currentuser.getFirstName() + " " + currentuser.getLastName());
                                            itemlist.add("E-mail: " + currentuser.getEmail());
                                            itemlist.add("Contact: " + currentuser.getphoneNumber());
                                            itemlist.add("Date of Birth: " + currentuser.getDob());


                                            //adapter for list
                                            adapter = new ArrayAdapter<>(readuser.this, android.R.layout.simple_list_item_1, itemlist);
                                            l1.setAdapter(adapter);


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
