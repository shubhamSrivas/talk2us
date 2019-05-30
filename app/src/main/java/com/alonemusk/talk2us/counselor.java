package com.alonemusk.talk2us;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class counselor extends AppCompatActivity {
DatabaseReference databaseReference;
TextView tv;
String phone;
LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselor);
        Intent intent=getIntent();
       phone= intent.getStringExtra("phone");
        databaseReference= FirebaseDatabase.getInstance().getReference("counselor/"+phone+"/message/");
        ll=findViewById(R.id.ll);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                final String str=dataSnapshot.getKey().toString();
                Log.d("strings",str);

                LayoutInflater layoutInflater=getLayoutInflater();
                View v=layoutInflater.inflate(R.layout.card,null);

                tv=v.findViewById(R.id.name);
                tv.setText(str);
                ll.addView(v);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ph=str;


                        Intent intent=new Intent(getApplicationContext(),chatting.class);
                        intent.putExtra("to",ph);
                        intent.putExtra("from",phone);
                        intent.putExtra("WhichtoWhich","Counselor2client");
                        Log.d("tooo",ph);
                        Log.d("fromm",phone);
                        startActivity(intent);
                    }
                });



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
        });







    }
}
