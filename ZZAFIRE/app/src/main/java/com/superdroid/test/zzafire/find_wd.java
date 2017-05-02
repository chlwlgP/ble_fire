package com.superdroid.test.zzafire;


import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class find_wd extends AppCompatActivity {

    ListView listView;//리스트뷰 객체
    BleList bleList = null;//리스트 어댑터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_wd);

        //리스트뷰 설정
        bleList = new BleList();
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(bleList);

    }

    public void test_addDevice(){

        if(bleList.addresss.isEmpty()) {

            String device_string = "기기명1";
            int rssi = 1111;
            String connect = "비연결1";
            bleList.addDevice(device_string, rssi, connect);

            String device_string2 = "기기명2";
            int rssi2 = 2222;
            String connect2 = "비연결2";
            bleList.addDevice(device_string2, rssi2, connect2);

            bleList.notifyDataSetChanged();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    String device_string3 = "기기명3";
                    int rssi3 = 3333;
                    String connect3 = "비연결3";
                    bleList.addDevice(device_string3, rssi3, connect3);
                    bleList.notifyDataSetChanged();
                }
            }, 500);// 0.5초 정도 딜레이를 준 후 시작
        }
        else
        {
            bleList.notifyDataSetChanged();
        }
    }
    public void mOnClick_update_device(View v) {


        test_addDevice();

    }

    private class BleList extends BaseAdapter {
        private ArrayList<String> devices;
        private ArrayList<String> addresss;
        private ArrayList<Integer> RSSIs;
        private ArrayList<String> connects;
        private LayoutInflater inflater;


        public BleList() {
            super();
            devices = new ArrayList<String>();
            addresss = new ArrayList<String>();
            RSSIs = new ArrayList<Integer>();
            connects = new ArrayList<String>();
            inflater = ((Activity) find_wd.this).getLayoutInflater();
        }

        public void addDevice(String device, int rssi, String connect) {
            devices.add(device);
            addresss.add(device);
            RSSIs.add(rssi);
            connects.add(connect);
        }

        public void clear() {
            devices.clear();
            addresss.clear();
            RSSIs.clear();
            connects.clear();
        }

        @Override
        public int getCount() {
            return devices.size();
        }

        @Override
        public Object getItem(int position) {
            return devices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.find_wd_listview, null);
                viewHolder.deviceName = (TextView) convertView.findViewById(R.id.text_name);
                viewHolder.deviceAddress = (TextView) convertView.findViewById(R.id.text_address);
                viewHolder.deviceRssi = (TextView) convertView.findViewById(R.id.text_rssi);
                viewHolder.deviceConnect = (TextView) convertView.findViewById(R.id.text_connect);
                viewHolder.deviceButton = (Button)convertView.findViewById(R.id.btn_connect);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            int rssi = RSSIs.get(position);

            viewHolder.deviceName.setText(devices.get(position));
            viewHolder.deviceAddress.setText(addresss.get(position));
            viewHolder.deviceRssi.setText(String.valueOf(rssi));
            viewHolder.deviceConnect.setText(connects.get(position));

            viewHolder.deviceButton.setTag(position);
            viewHolder.deviceButton.setOnClickListener(btnClickListener);

            return convertView;
        }
    }
    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pos =(Integer)v.getTag();
            switch (v.getId()) {

                // 버튼 클릭
                case R.id.btn_connect:
                {
                    String change_connects_state = "연결";
                    bleList.connects.set(pos,change_connects_state);
                    bleList.notifyDataSetChanged();

                }
                break;
            }
        }
    };


    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
        TextView deviceRssi;
        TextView deviceConnect;
        Button deviceButton;

    }


}