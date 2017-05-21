package com.superdroid.test.zzafire;

import android.os.Bundle;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void mOnClick_WD(View v){
        Intent intent = new Intent(this, FindWDActivity.class);
        startActivity(intent);
    }
    public void mOnClick_Data(View v){
        Intent intent = new Intent(this, SeeDataActivity.class);
        startActivity(intent);
    }

}
