package com.superdroid.test.zzafire;


        import android.app.Activity;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.os.Handler;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.CompoundButton.OnCheckedChangeListener;
        import android.widget.ExpandableListView;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import org.w3c.dom.Text;

        import java.util.ArrayList;

public class realtime_data extends AppCompatActivity {

    ListView listView;//리스트뷰 객체
    BleList bleList = null;//리스트 어댑터
    buttonInfo buttonInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_data);

        bleList = new BleList();
        listView = (ListView) findViewById(R.id.listView_realtime);
        listView.setAdapter(bleList);

        test_addDevice();
    }


    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                }
            else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                displayData(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    public void displayData(String data){
           if (data != null) {
                buttonInfo.viewHolder.deviceText.setText(data);
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    public void test_addDevice(){

        if(bleList.devices.isEmpty()) {

            String device_string = "기기명1";
            bleList.addDevice(device_string);

            String device_string2 = "기기명2";
            bleList.addDevice(device_string2);

            bleList.notifyDataSetChanged();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    String device_string3 = "기기명3";
                    bleList.addDevice(device_string3);
                    bleList.notifyDataSetChanged();
                }
            }, 500);// 0.5초 정도 딜레이를 준 후 시작
        }
        else
        {
            bleList.notifyDataSetChanged();
        }
    }

    private class BleList extends BaseAdapter {
        private ArrayList<String> devices;
        private LayoutInflater inflater;


        public BleList() {
            super();
            devices = new ArrayList<String>();
            inflater = ((Activity) realtime_data.this).getLayoutInflater();
        }

        public void addDevice(String device) {
            devices.add(device);
        }

        public void clear() {
            devices.clear();
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
            final ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.time_listview, null);
                viewHolder.devicebox= (CheckBox) convertView.findViewById(R.id.cb_name);
                viewHolder.deviceText= (TextView) convertView.findViewById(R.id.textView_realtime);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            buttonInfo = new buttonInfo();
            buttonInfo.viewHolder=viewHolder;
            buttonInfo.position=position;
            viewHolder.devicebox.setTag(buttonInfo);
            viewHolder.devicebox.setText(bleList.devices.get(position));
            viewHolder.devicebox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        Toast.makeText(realtime_data.this,viewHolder.devicebox.getText().toString()+"시작", Toast.LENGTH_SHORT).show();
                    } else {
                    }

                }
            });




            return convertView;
        }
    }


    private static class buttonInfo{
        ViewHolder viewHolder;
        int position;
    }

    static class ViewHolder {
        CheckBox devicebox;
        TextView deviceText;
    }
}
