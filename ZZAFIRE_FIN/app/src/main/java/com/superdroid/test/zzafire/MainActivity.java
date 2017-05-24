package com.superdroid.test.zzafire;

import android.os.Bundle;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_csv).setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        File exportDir = new File(Environment.getExternalStorageDirectory(),"");
                        if (!exportDir.exists()) {
                            exportDir.mkdirs();
                        }

                        Log.d("HaHa",String.valueOf(exportDir));
                        List<DeviceInfo> infos = DeviceInfo.listAll(DeviceInfo.class);
                        File file = new File(exportDir, "csvfile.csv");
                        Log.d("HaHa","여기까지는 됨!");

                        try {
                            file.createNewFile();
                            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

                            for(int i=0;i<infos.size();i++) {

                                String arrStr[] = {infos.get(i).address, infos.get(i).hrdata,
                                        infos.get(i).year,
                                        infos.get(i).month,
                                        infos.get(i).day,
                                        infos.get(i).hour,
                                        infos.get(i).min,
                                        infos.get(i).sec
                                };
                                csvWrite.writeNext(arrStr);
                            }
                            csvWrite.close();
                        } catch (Exception sqlEx) {
                            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
                        }

                    }
                }
        );

        //임의로 데이터 넣기
        /*String[] address = new String[] { "00:11:22:33:44:55", "AA:BB:CC:DD:EE:FF", "00:11:22:AA:BB:CC" };
        String year = "2017";
        String month = "5";
        String day = "3";

        for (int i = 0; i < address.length; i++) {
            for (int j = 0; j <= 23; j++) {
                Random random = new Random();
                String hrdata = Integer.toString(random.nextInt(150));

                String hour = "";
                if (j < 10) {
                    hour = "0" + Integer.toString(j);
                } else {
                    hour = Integer.toString(j);
                }

                DeviceInfo info = new DeviceInfo(
                        address[i],
                        hrdata,
                        year,
                        month,
                        day,
                        hour,
                        "01",
                        "00");
                info.save();
            }
        }
        //임의로 데이터 넣기 끝*/
    }

    public void mOnClick_WD(View v){
        Intent intent = new Intent(this, FindWDActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void mOnClick_Data(View v){
        Intent intent = new Intent(this, SeeDataActivity.class);
        startActivity(intent);
    }

    public void onBackPressed(){
        moveTaskToBack(true);
        super.onBackPressed();
    }

}