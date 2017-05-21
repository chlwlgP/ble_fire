package com.superdroid.test.zzafire;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class RealtimeDataActivity extends AppCompatActivity {


	ListView listView_up;						//리스트뷰 객체
	BleListViewAdapter bleList = null;		//리스트 어댑터

	ListView listView_down;
	GraphListViewAdapter graphList = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_realtime_data);

		bleList = new BleListViewAdapter(this);
		listView_up = (ListView) findViewById(R.id.listView_realtime);
		listView_up.setAdapter(bleList);

		addDevice_Test("기기명 1");
		addDevice_Test("기기명 2");

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				addDevice_Test("기기명 3");
			}
		}, 500);



		graphList = new GraphListViewAdapter(getApplicationContext());
		listView_down = (ListView) findViewById(R.id.listView_graph);
		listView_down.setAdapter(graphList);



		listView_up.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				//Toast.makeText(getApplicationContext(), "시작", Toast.LENGTH_SHORT).show();

				if (bleList.getIsCheck(position)) {
					//graphList.remove(position);
				} else {

					//DeviceInfo 테이블에 저장된 HRData를 얻어오기 위해서, infos 라는 객체를 생성하여서 접근한다.
					List<DeviceInfo> infos = DeviceInfo.listAll(DeviceInfo.class);

					ArrayList<Point> points = new ArrayList<Point>();
					for (int i = 0; i < 20; i++) {
						points.add(new Point(i, Integer.parseInt(infos.get(i).hrdata))); // infos.get(i).hrdata는 infos 객체를 이용하여서, 테이블에 저장된 hrdata 값을 index 순서대로 y축으로 넘겨준다.
					}

					Point[] p = new Point[points.size()];
					p = points.toArray(p);

					addPoint_Test(p);
				}

				bleList.setIsCheck(position);
				bleList.notifyDataSetChanged();
			}
		});
	}

	public void addDevice_Test(String device) {
		bleList.addDevice(device);
		bleList.notifyDataSetChanged();
	}

	public void addPoint_Test(Point[] point) {
		graphList.addPoint(point);
		graphList.notifyDataSetChanged();
	}

}