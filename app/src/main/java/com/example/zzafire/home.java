package com.example.zzafire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void mOnClick_WD(View v){
        Intent intent = new Intent(this, findWD.class);
        startActivity(intent);
    }
    public void mOnClick_Data(View v){
        Intent intent = new Intent(this, seeData.class);
        startActivity(intent);
    }
    public void mOnClick_Profile(View v){
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
}
