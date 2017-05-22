package com.superdroid.test.zzafire;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class BleListViewAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	String[] devices;
	Boolean[] isCheck;

	{
		context = null;
		inflater = null;
		devices = null;
		isCheck = null;
	}

	public BleListViewAdapter(Context context, int size) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.devices = new String[size];
		this.isCheck = new Boolean[size];

		for (int i = 0; i < size; i++) {
			this.devices[i] = "";
			this.isCheck[i] = Boolean.FALSE;
		}
	}

	public String getDevice(int position) {
		if (this.devices.length > position) {
			return this.devices[position];
		} else {
			return null;
		}
	}

	public void setDevice(int position, String device) {
		if (this.devices.length > position) {
			this.devices[position] = device;
			this.isCheck[position] = Boolean.FALSE;
		}
	}

	public Boolean getIsCheck(int position) {
		if (this.isCheck.length > position) {
			return this.isCheck[position];
		} else {
			return null;
		}
	}

	public void setIsCheck(int position) {
		if (this.isCheck.length > position) {
			this.isCheck[position] = !this.isCheck[position];
		}
	}

	@Override
	public int getCount() {
		return devices.length;
	}

	@Override
	public Object getItem(int position) {
		return devices[position];
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

		viewHolder.checkBox.setText(this.devices[position]);
		viewHolder.checkBox.setFocusable(false);
		viewHolder.checkBox.setClickable(false);
		viewHolder.checkBox.setChecked(this.isCheck[position]);
		viewHolder.textView.setText("Test");

		return itemLayout;
	}

	private class ViewHolder {
		CheckBox checkBox;
		TextView textView;
	}
}
