package com.superdroid.test.zzafire;

import android.os.Bundle;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //임의로 데이터 넣기

        String address = "00:11:22:AA:BB:CC";
        String year = "2017";
        String month = "5";

        for (int i = 1; i < 17; i++) {
            Random random = new Random();
            String hrdata = Integer.toString(random.nextInt(150));

            DeviceInfo info = new DeviceInfo(
                    address,
                    hrdata,
                    year,
                    month,
                    Integer.toString(i),
                    "0",
                    "0",
                    "0");

            info.save();
        }
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
