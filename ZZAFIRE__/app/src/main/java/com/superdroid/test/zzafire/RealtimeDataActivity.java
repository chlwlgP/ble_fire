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
import java.util.Random;

public class RealtimeDataActivity extends AppCompatActivity {
	public static final String col_1;
	public static final String col_2;
	public static final String col_3;
	public static final int VERSION;
	private static DBManager dbManager;

	static{
		col_1 = "_id";
		col_2 = "Time";
		col_3 = "HR";
		VERSION = 2;
		dbManager = null;
	}

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

		dbManager = DBManager.getDbManager(getApplicationContext());
		ContentValues contentValues = new ContentValues();
		for (int i = 0; i < 10; i++) {
			contentValues.put(col_2, Integer.toString(i));
			contentValues.put(col_3, Integer.toString((new Random()).nextInt(100)));
			dbManager.insert(contentValues);
		}

		graphList = new GraphListViewAdapter(getApplicationContext());
		listView_down = (ListView) findViewById(R.id.listView_graph);
		listView_down.setAdapter(graphList);

		listView_up.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Toast.makeText(getApplicationContext(), "시작", Toast.LENGTH_SHORT).show();

				ArrayList<Point> points = new ArrayList<Point>();
				for (int i = 0; i < 10; i++) {
					points.add(new Point(i, myFind(i)));
				}

				Point[] p = new Point[points.size()];
				p = points.toArray(p);

				addPoint_Test(p);

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

	private static int myFind(int x){
		//Select A from B where C
		//A projection
		//C selection
		//C=? selection 구문
		//? selectionArgs
		String[] projection = new String[]{ col_3  }; //HRdata

		String selection = col_2 + "=?"; //Time

		String[] selectionArgs = new String[]{ Integer.toString(x) };
		String orderBy = col_2; //Time

		Cursor cursor = dbManager.query(projection, selection, selectionArgs, null, null, orderBy);

		if (cursor.getCount() > 0) {
			String result = "";
			while (cursor.moveToNext()) {
				//Log.d("CHAELIN_DBLOG", result);

				result = cursor.getString(0);
			}

			return Integer.parseInt(result);
		} else {
			//Log.d("CHAELIN_DBLOG", "데이터가 존재하지 않습니다.");

			return -1;
		}
	}
}