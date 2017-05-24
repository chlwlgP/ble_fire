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
	Point[][] points;

	{
		context = null;
		inflater = null;
		points = null;
	}

	public GraphListViewAdapter(Context context, int size) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.points = new Point[size][];

		for (int i = 0; i < size; i++) {
			this.points[i] = null;
		}
	}

	public void setPoint(int position, Point[] point) {
		if (point != null) {
			this.points[position] = new Point[point.length];
			this.points[position] = point;
		} else {
			this.points[position] = null;
		}
	}

	public Point[] getPoint(int position) {
		if (this.points.length > position) {
			return this.points[position];
		} else {
			return null;
		}
	}

	@Override
	public int getCount() {
		return this.points.length;
	}

	@Override
	public Object getItem(int position) {
		return this.points[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View itemLayout = convertView;
		ViewHolder viewHolder = null;

		if (itemLayout == null) {
			itemLayout = this.inflater.inflate(R.layout.listview_graph, null);

			viewHolder = new ViewHolder();
			viewHolder.linearLayout = (LinearLayout) itemLayout.findViewById(R.id.graph);

			itemLayout.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) itemLayout.getTag();
		}

		try {
			viewHolder.linearLayout.removeAllViews();
		} catch (Exception e) {

		}
		final LineGraph line = new LineGraph();
		if (points[position] != null) {

			final GraphicalView graph = line.getView(context);

			DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
			int height = displayMetrics.heightPixels;

			viewHolder.linearLayout.addView(graph, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(height * 0.5)));

			new Thread() {
				public void run() {
					for (int i = 0; i < (points[position]).length; i++) {
						line.addNewPoints((points[position])[i]);
						graph.repaint();
					}
				}
			}.start();
		}

		return itemLayout;
	}

	private class ViewHolder {
		LinearLayout linearLayout;
	}
}
