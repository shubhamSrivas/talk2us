package com.alonemusk.talk2us;

import android.content.Context;
import android.content.SharedPreferences;

import com.alonemusk.talk2us.MainActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class save_retrive_data extends chatting {
    // shared pref class for saving and retriving messages
    ArrayList<String>ss;
    Context ct;

    public save_retrive_data( Context ct) {

        this.ct=ct;
    }

    public ArrayList<String> getCb() {
        return ss;
    }
    void   savedata(ArrayList<String>ss,String string){
        SharedPreferences sharedPreferences=ct.getSharedPreferences(string,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        Gson gson=new Gson();
        String json=gson.toJson(ss);
        editor.putString(string,json);
        editor.apply();





    }
    ArrayList<String> loadData(String string){
        SharedPreferences sharedPreferences=ct.getSharedPreferences(string,MODE_PRIVATE);
        Gson gson=new Gson();
        String json=sharedPreferences.getString(string,null);
        Type type=new TypeToken<ArrayList<String>>(){}.getType();
        ss=gson.fromJson(json,type);
        if(json==null){
            ss=new ArrayList<>();
        }
        return ss;
    }

    public void setCb(ArrayList<String> cb) {
        this.ss = ss;
    }
}
