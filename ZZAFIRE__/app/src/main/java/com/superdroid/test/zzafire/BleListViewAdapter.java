package com.superdroid.test.zzafire;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BleListViewAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	ArrayList<String> devices;
	ArrayList<Boolean> isCheck;

	{
		context = null;
		devices = null;
		inflater = null;
	}

	public BleListViewAdapter(Context context) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.devices = new ArrayList<String>();
		this.isCheck = new ArrayList<Boolean>();

		for (int i = 0; i < isCheck.size(); i++) {
			this.isCheck.set(i, Boolean.FALSE);
		}
	}

	public  BleListViewAdapter(Context context, ArrayList<String> devices) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.devices = devices;
		this.isCheck = new ArrayList<>(devices.size());

		for (int i = 0; i < isCheck.size(); i++) {
			this.isCheck.set(i, Boolean.FALSE);
		}
	}

	public void addDevice(String device) {
		this.devices.add(device);
		this.isCheck.add(Boolean.FALSE);
	}

	public String getDevice(int index) {
		return this.devices.get(index);
	}

	public ArrayList<String> getDevies() {
		return this.devices;
	}

	public void clearDevices() {
		devices.clear();
	}

	public void setIsCheck(int index) {
		this.isCheck.set(index, !this.isCheck.get(index));
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
		View itemLayout = convertView;
		ViewHolder viewHolder = null;

		if (itemLayout == null) {
			itemLayout = this.inflater.inflate(R.layout.listview_time, null);

			viewHolder = new ViewHolder();
			viewHolder.checkBox = (CheckBox) itemLayout.findViewById(R.id.checkbox_name);
			viewHolder.textView = (TextView) itemLayout.findViewById(R.id.textView_realtime);

			itemLayout.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) itemLayout.getTag();
		}

		viewHolder.checkBox.setText(this.devices.get(position));
		viewHolder.checkBox.setFocusable(false);
		viewHolder.checkBox.setClickable(false);
		viewHolder.checkBox.setChecked(this.isCheck.get(position));
		viewHolder.textView.setText("Test");

		return itemLayout;
	}

	private class ViewHolder {
		CheckBox checkBox;
		TextView textView;
	}
}
