package com.superdroid.test.zzafire;


import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import java.util.ArrayList;

public class find_wd extends AppCompatActivity {

    ListView listView;//리스트뷰 객체
    BleList bleList = null;//리스트 어댑터
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;

    private static final int REQUEST_ENABLE_BT = 1;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_wd);

        //리스트뷰 설정
        bleList = new BleList();
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(bleList);
        mHandler = new Handler();


        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "ble지원이 되지 않는 기기입니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "bluetooth가 지원이 되지 않는 기기입니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }


    public void mOnClick_update_device(View v) {

        scanLeDevice(true);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            bleList.clear();
            bleList.notifyDataSetChanged();
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    Toast.makeText(find_wd.this, "기기검색을 완료했습니다.", Toast.LENGTH_SHORT).show();
                }
            }, SCAN_PERIOD);
            mScanning = true;

        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            bleList.clear();
            bleList.notifyDataSetChanged();
        }
    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {

                            String connect ="비연결";
                            bleList.addDevice(device,rssi,connect);
                            bleList.notifyDataSetChanged();

                }
            };

    private class BleList extends BaseAdapter {
        private ArrayList<BluetoothDevice> devices;
        private ArrayList<BluetoothDevice> addresss;
        private ArrayList<Integer> RSSIs;
        private ArrayList<String> connects;
        private LayoutInflater inflater;


        public BleList() {
            super();
            devices = new ArrayList<BluetoothDevice>();
            addresss = new ArrayList<BluetoothDevice>();
            RSSIs = new ArrayList<Integer>();
            connects = new ArrayList<String>();
            inflater = ((Activity) find_wd.this).getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device, int rssi, String connect) {
            if(!devices.contains(device)){
                devices.add(device);
                addresss.add(device);
                RSSIs.add(rssi);
                connects.add(connect);
            }
            else{
                RSSIs.set(devices.indexOf(device),rssi);
            }
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

            String deviceName = devices.get(position).getName();
            String deviceAddress = addresss.get(position).getAddress();
            int rssi = RSSIs.get(position);
            String connect = connects.get(position);

            viewHolder.deviceName.setText(deviceName != null && deviceName.length() > 0 ?deviceName:"알 수 없는 장치");
            viewHolder.deviceAddress.setText(deviceAddress);
            viewHolder.deviceRssi.setText(String.valueOf(rssi));
            viewHolder.deviceConnect.setText(connect);

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