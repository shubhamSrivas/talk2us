package com.alonemusk.talk2us;

import android.content.Intent;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class chatting extends AppCompatActivity {
    LinearLayout ll;
    EditText message;
    Button send;
    String cp;
    String srt;
    String number;
    String which2which;// since this activity remains same for both client and counselor this which2which is a approach to find which intent have started this activity
    ArrayList<String> chats;
    save_retrive_data save_retrive_data;
    FirebaseAuth mauth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);



        //shared pref class used to save and retrive messages locally in form of arraylist (though not working)
        save_retrive_data=new save_retrive_data(getApplicationContext());

        initialize();
        // displaying all previous messages

        /* this is the fist thing that need to be improved, right now its displaying messages by adding textView in linear layout manually
        what we wanna do is use layoutinflator
         */

        for(int i=0;i<chats.size();i++){
            TextView tv=new TextView(getApplicationContext());
            tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv.setText(chats.get(i));
            tv.setTextSize(16);
            tv.setPadding(5,4,0,0);
            ll.addView(tv);



        }
        //sending message

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // emptying edit text
                String chat=message.getText().toString();
                chats.add(chat);
                message.setText("");




                FirebaseUser firebaseUser=mauth.getCurrentUser();
                // something tough to explain
               if(which2which.equals("Counselor2client")){
                   addtoclientnew(number, chat, cp);//adding chat to client
                   addtocounselornew(number, chat, cp);
                   addtomessage(number, chat, cp);
                   Log.d("hello","frello");
               }
                else {
                   addtoclient(number, chat, cp);
                   addtocounselor(number, chat, cp);
                   addtomessage(cp, chat, number);
               }
                Log.d("cpp",cp);



            }
        });

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("counselor/");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("numberr",number);
                if(dataSnapshot!=null) {



                        if (which2which.equals("")) {
                            Log.d("cpppp", cp);
                          //  srt = dataSnapshot.child("message").child(number).child("chat").getValue().toString();
                        } else {
                            Log.d("cpppp", cp);
                            srt = dataSnapshot.child("message").child(cp).child("chat").getValue().toString();

                        }



                }



                //adding chats very first time

                TextView tv=new TextView(getApplicationContext());
                tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setText(srt);
                tv.setPadding(5,4,0,0);

                tv.setTextSize(16);
                ll.addView(tv);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(which2which.equals("")) {
                    Log.d("cpppp",cp);
                    srt = dataSnapshot.child("message").child(number).child("chat").getValue().toString();
                }
                else{
                    Log.d("cpppp",cp);
                    srt = dataSnapshot.child("message").child(cp).child("chat").getValue().toString();

                }
                //adding chats after that

                Log.d("srt",srt);
                Log.d("helllo",srt);
                TextView tv=new TextView(getApplicationContext());
                tv.setTextSize(16);
                tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                tv.setText(srt);
                tv.setPadding(5,4,0,0);

                ll.addView(tv);
                chats.add(srt);


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

    @Override
    protected void onStop() {
    // saving data to shared pref
        save_retrive_data.savedata(chats,cp);
        super.onStop();

    }

    void initialize(){
        //ll=findViewById(R.id.ll);
        message=findViewById(R.id.message);
        mauth=FirebaseAuth.getInstance();
        send=findViewById(R.id.send);
        ll=findViewById(R.id.ll);
        chats=save_retrive_data.loadData(cp);
        Intent intent=getIntent();
         which2which=intent.getStringExtra("WhichtoWhich");

         if(which2which.equals("Counselor2client")) {
             cp = intent.getStringExtra("to");
             number = intent.getStringExtra("from");
         }
         else{
             cp = intent.getStringExtra("to");
             number = intent.getStringExtra("from");
         }

    }
    void addtoclient(String number,String chat,String cp){
        Log.d("cppp",cp);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("client/"+number);// manually reffring node in firebase
        databaseReference.child("message").child(cp).setValue(new message(number,cp,chat));



    }
    void addtocounselor(String number,String chat,String cp){

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("counselor/"+cp);
        databaseReference.child("message").child(number).setValue(new message(number,cp,chat));



    }
    void addtoclientnew(String number,String chat,String cp){
        Log.d("cppp",cp);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("client/"+cp);
        databaseReference.child("message").child(number).setValue(new message(number,cp,chat));


    }
    void addtocounselornew(String number,String chat,String cp){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("counselor/"+number);
        databaseReference.child("message").child(cp).setValue(new message(cp,number,chat));


    }
    void addtomessage(String number,String chat,String cp){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("message");
        databaseReference.child(number).setValue(new message(cp,number,chat));




    }

}
