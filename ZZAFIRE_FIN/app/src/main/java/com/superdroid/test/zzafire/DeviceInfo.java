package com.superdroid.test.zzafire;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

@Table
public class DeviceInfo extends SugarRecord {
    public String address;
    public String hrdata;
    public String year;
    public String month;
    public String day;
    public String hour;
    public String min;
    public String sec;

    public DeviceInfo() {

    }

    public DeviceInfo(String address, String hrdata, String year, String month, String day, String hour, String min, String sec)
    {
        this.address = address;
        this.hrdata = hrdata;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
    }
}