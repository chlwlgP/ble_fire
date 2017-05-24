package com.superdroid.test.zzafire;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class RealtimeDataActivity extends AppCompatActivity {
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/M/dd/HH/mm/ss");

	ListView listView_up;               //리스트뷰 객체
	BleListViewAdapter bleList = null;      //리스트 어댑터

	ListView listView_down;
	GraphListViewAdapter graphList = null;

	Thread[] threads = null;

	Thread testThread = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_realtime_data);

		this.setListView();

      /*
      // 임의로 실시간 데이터 생성
      testThread = new Thread(new Runnable() {
         @Override
         public void run() {
            String[] address = new String[] { "00:11:22:33:44:55", "AA:BB:CC:DD:EE:FF", "00:11:22:AA:BB:CC" };

            try {
               while (!Thread.currentThread().isInterrupted()) {
                  long now = System.currentTimeMillis();
                  Date date = new Date(now);

                  String[] time = simpleDateFormat.format(date).split("/");

                  for (int i = 0; i < address.length; i++ ) {
                     Random random = new Random();
                     String hrdata = Integer.toString(random.nextInt(150));

                     DeviceInfo info = new DeviceInfo(
                        address[i],
                        hrdata,
                        time[0],
                        time[1],
                        time[2],
                        time[3],
                        time[4],
                        time[5]);
                  info.save();
                  }

                  Thread.sleep(1000);
               }
            } catch (InterruptedException ie) {

            } catch (Exception e) {

            } finally {
               Log.d("CHAELIN", "Test Input Thread Dead");
            }
         }
      });
      testThread.start();
      // 임의로 실시간 데이터 생성 끝
      */
	}

    public void onBackPressed(){
        Intent intent = new Intent(this, SeeDataActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
    }


	@Override
	protected void onDestroy() {
		testThread.interrupt();
		super.onDestroy();
	}

	private void setListView() {
		// 기기 address 가져오기
		//List<DeviceInfo> info = DeviceInfo.find(DeviceInfo.class, null, null, "address", "address asc", null);

		long now = System.currentTimeMillis() - 2000; //현재시간
		Date date = new Date(now);

		String[] currentTime = simpleDateFormat.format(date).split("/"); //시간 나눠서 배열에

		List<DeviceInfo> info = DeviceInfo.find(DeviceInfo.class,
				"year=? and month=? and day=? and hour=? and min=? and sec=?",
				new String[] { currentTime[0], currentTime[1], currentTime[2], currentTime[3], currentTime[4], currentTime[5]},
				"address",
				"address asc",
				null);


		if (info.size() == 0) {
			Toast.makeText(getApplicationContext(), "데이터가 없습니다", Toast.LENGTH_SHORT).show();
			return;
		}

		int size = info.size();

		this.threads = new Thread[size];

		bleList = new BleListViewAdapter(this, size);
		listView_up = (ListView) findViewById(R.id.listView_realtime);
		listView_up.setAdapter(bleList);

		for (int i = 0; i < size; i++) {
			setDevice(i, info.get(i).address.toString());
		}

      /* GraphListView */
		graphList = new GraphListViewAdapter(getApplicationContext(), bleList.getCount());
		listView_down = (ListView) findViewById(R.id.listView_graph);
		listView_down.setAdapter(graphList);

		listView_up.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
				if (bleList.getIsCheck(position)) {
					removeThread(position);

					graphList.setPoint(position, null);
					graphList.notifyDataSetChanged();
				} else {
					addThread(position);
				}

				bleList.setIsCheck(position);
				bleList.notifyDataSetChanged();
			}
		});
	}

	private void addThread(final int position) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while (!Thread.currentThread().isInterrupted()) {
						long now = System.currentTimeMillis();
						Date date = new Date(now);

						String address = bleList.getDevice(position);
						String[] currentTime = simpleDateFormat.format(date).split("/");

						Thread.sleep(1000);

						List<DeviceInfo> info = DeviceInfo.find(
								DeviceInfo.class,
								"address=? and year=? and month=? and day=? and hour=? and min=? and sec=?",
								new String[] { address, currentTime[0], currentTime[1], currentTime[2], currentTime[3], currentTime[4], currentTime[5] },
								null,
								null,
								null);

						if (info.size() > 0) {
							String hrdata = info.get(0).hrdata;

							Point[] point;

							if (graphList.getPoint(position) == null) {
								point = new Point[1];
								Point newPoint = new Point(0, Integer.parseInt(hrdata));
								point[0] = newPoint;
							} else {
								point = new Point[graphList.getPoint(position).length + 1];

								for (int i = 0; i < graphList.getPoint(position).length; i++) {
									point[i] = (graphList.getPoint(position))[i];
								}

								Point newPoint = new Point(point.length - 1, Integer.parseInt(hrdata));
								point[point.length - 1] = newPoint;
							}

							graphList.setPoint(position, point);
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									graphList.notifyDataSetChanged();
								}
							});
						}
					}
				} catch (InterruptedException ie) {

				} catch (Exception e) {

				} finally {
					Log.d("CHAELINE", "GRAPH THREAD DEAD");
				}
			}
		});

		if (threads.length > position) {
			threads[position] = thread;
			(threads[position]).start();
		}
	}

	private void removeThread(int position) {
		if (threads.length > position) {
			(threads[position]).interrupt();
			threads[position] = null;
		}
	}

	private void setDevice(int index, String device) {
		bleList.setDevice(index, device);
		bleList.notifyDataSetChanged();
	}

	private void setPoint(int index, Point[] point) {
		graphList.setPoint(index, point);
		graphList.notifyDataSetChanged();
	}
}