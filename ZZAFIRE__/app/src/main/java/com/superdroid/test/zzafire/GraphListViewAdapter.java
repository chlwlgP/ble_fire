package com.superdroid.test.zzafire;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import org.achartengine.GraphicalView;

import java.util.ArrayList;

public class GraphListViewAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	ArrayList<Point[]> points;

	{
		context = null;
		inflater = null;
		points = null;
	}

	public GraphListViewAdapter(Context context) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.points = new ArrayList<Point[]>();
	}

	public GraphListViewAdapter(Context context, ArrayList<Point[]> points) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.points = points;
	}

	public ArrayList<Point[]> getPoints() {
		return this.points;
	}

	public void addPoint(Point[] point) {
		this.points.add(point);
	}

	public Point[] getPoint(int index) {
		return this.points.get(index);
	}

	@Override
	public int getCount() {
		return this.points.size();
	}

	@Override
	public Object getItem(int position) {
		return this.points.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View itemLayout = convertView;

		if (itemLayout == null) {
			itemLayout = this.inflater.inflate(R.layout.listview_graph, null);
		}

		final LineGraph line = new LineGraph();
		final GraphicalView graph = line.getView(context);

		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		int width = displayMetrics.widthPixels;
		int height = displayMetrics.heightPixels;

		((LinearLayout) itemLayout.findViewById(R.id.listView_graph_layout)).addView(graph, new ViewGroup.LayoutParams((int)(width * 0.75), (int)(height * 0.5)));

		new Thread() {
			public void run() {
				for (int i = 0; i < (points.get(position)).length; i++) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					line.addNewPoints((points.get(position))[i]);
					graph.repaint();
				}
			}
		}.start();

		return itemLayout;
	}

	public void remove(int position) {

	}
}