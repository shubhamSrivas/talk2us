package com.alonemusk.talk2us;

import com.google.firebase.database.DatabaseReference;

public class message {
    public String chat;

   public String to;
   public String from;
    message(String t,String f,String cha){
        this.to=t;
        this.from=f;
        this.chat=cha;


    }






}
