package com.example.zzafire;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class findWD extends AppCompatActivity {
    boolean scanning = false;

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;


    private class BleList extends BaseAdapter {//리스트뷰 어뎁터 선언
        private ArrayList<BluetoothDevice> devices;
        private ArrayList<Integer> RSSIs;
        private ArrayList<String> connects;
        private LayoutInflater inflater;


        public BleList(){
            super();
            devices = new ArrayList<BluetoothDevice>();
            RSSIs = new ArrayList<Integer>();
            connects = new ArrayList<String>();
            inflater = ((Activity) findWD.this).getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device,int rssi,String connect){
            if(!devices.contains(device)){
                devices.add(device);
                RSSIs.add(rssi);
                connects.add(connect);
            }
            else{
                RSSIs.set(devices.indexOf(device),rssi);
            }
        }

        public void clear(){
            devices.clear();
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

            if(convertView == null){
                viewHolder = new ViewHolder();
                 convertView = inflater.inflate(R.layout.wddevice_listview,null);
                viewHolder.deviceName = (TextView) convertView.findViewById(R.id.text_name);
                viewHolder.deviceRssi = (TextView) convertView.findViewById(R.id.text_rssi);
                viewHolder.deviceConnect = (TextView) convertView.findViewById(R.id.text_connect);

                convertView.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            String deviceName = devices.get(position).getName();
            int rssi = RSSIs.get(position);
            String connect = connects.get(position);

            viewHolder.deviceName.setText(deviceName != null && deviceName.length() > 0 ?deviceName:"알 수 없는 장치");
            viewHolder.deviceRssi.setText(String.valueOf(rssi));
            viewHolder.deviceConnect.setText(connect);

            return convertView;
        }
    }

    static class ViewHolder {
        TextView deviceName;
        TextView deviceRssi;
        TextView deviceConnect;
    }


    Button button;//버튼

    ListView listView;//리스트뷰 객체
    BleList bleList = null;//리스트 어댑터
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_wd);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.BLUETOOTH},1);

        bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();


        if(bluetoothAdapter == null || !bluetoothAdapter.isEnabled()){
            //블루투스를 지원하지 않거나 켜져있지 않으면 장치를끈다.
            Toast.makeText(this, "블루투스를 켜주세요", Toast.LENGTH_SHORT).show();
            finish();
        }

        //리스트뷰 설정
        bleList = new BleList();
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(bleList);

        //버튼설정
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!scanning){
                    bleList.clear();
                    bleList.notifyDataSetChanged();
                    bluetoothAdapter.startLeScan(leScanCallback);
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            bluetoothAdapter.stopLeScan(leScanCallback);
                            Toast.makeText(findWD.this, "기기검색을 완료했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }, 5000);
                }
                else{
              //      bluetoothAdapter.stopLeScan(leScanCallback);
              //      bleList.clear();
              //      bleList.notifyDataSetChanged();
                }
              //  scanning = !scanning;

            }
        });
    }

    // 스켄 이후 장치 발견 이벤트
    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {

        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.d("scan",device.getName() + " RSSI :" + rssi + " Record " + scanRecord);
            String connect = "비연결";
            bleList.addDevice(device,rssi,connect);
            bleList.notifyDataSetChanged();
        }
    };
}

