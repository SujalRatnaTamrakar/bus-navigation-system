package com.example.chirag.navigationdrawer;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.opencensus.metrics.LongGauge;

public class readuser extends AppCompatActivity {
    ListView l1;
    ArrayAdapter<String> adapter;
    DatabaseReference databaseReference;
    FirebaseUser user;
    private TextView testView_test;
    User testuser;
    String currentuserkey;
    List<String> itemlist,testlist;
    String uid;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readuser);
        l1=(ListView)findViewById(R.id.listView);

        //currentuser reference and getting the uid of the current user
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();


        //initializing the itemlist
        itemlist = new ArrayList<>();
        //reference to the node pointing to User
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
        //ValueEventListener for triggering the event only once
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

                            String useremail = user.getEmail();
                            //verified using the email we get the current user
                            if(useremail.equals(testuser.getEmail())){
                                //if we get the snapshot of the user we need, save the key to currentuser
                                currentuserkey=keyNode.getKey();
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
                                        itemlist.add("DateofBirth"+currentuser.getDob());


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
        databaseReference.addListenerForSingleValueEvent(valueEventListener);

    }

}
