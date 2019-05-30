package com.alonemusk.talk2us;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class counselorList extends AppCompatActivity {
    //Responsible for showing list of counselor ie
DatabaseReference databaseReference;
public static final String Tag="hello";
LinearLayout ll;
    FirebaseAuth mauth;

TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselor_list);

        ll=findViewById(R.id.ll);
// form the counselor in firebase getting name of all existing counselor

        //only client will have access to this activity
          databaseReference = FirebaseDatabase.getInstance().getReference("counselor");
         String str=databaseReference.getKey();
         Log.d("counselor",str);
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                       String str=  dataSnapshot1.getKey();


                       if(str.equals("name")){
                           str = dataSnapshot1.getValue().toString();


                        LayoutInflater layoutInflater=getLayoutInflater();
                        View v=layoutInflater.inflate(R.layout.card,null);

                         tv=v.findViewById(R.id.name);
                        tv.setText(str);
                        ll.addView(v);
                        tv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String ph=dataSnapshot.getKey();
                                mauth= FirebaseAuth.getInstance();
                                FirebaseUser firebaseUser=mauth.getCurrentUser();

                                String  number=firebaseUser.getPhoneNumber();



                                Intent intent=new Intent(getApplicationContext(),chatting.class);
                                intent.putExtra("to",ph);//counselor number
                                intent.putExtra("from",number);//client number

                                intent.putExtra("WhichtoWhich","");
                                startActivity(intent);
                            }
                        });





                    }}
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            }) ;
            }

    }






