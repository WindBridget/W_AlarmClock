package com.windbridget.w_alarmclock;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/**
 * Created by Ru on 11/8/2016.
 */

public class AlarmList {
    private String name;
    private Date hour;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Date getHour() {
        return hour;
    }
    public void setHour(Date hour) {
        this.hour = hour;
    }
    public AlarmList(String title,
                     Date hourFinish) {
        super();
        this.name = title;
        this.hour = hourFinish;
    }
    public AlarmList() {
        super();
    }
    /**
     * lấy định dạng giờ phút
     * @param d
     * @return
     */
    public String getHourFormat(Date d)
    {
        SimpleDateFormat dft=new
                SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return dft.format(d);
    }
    @Override
    public String toString() {
        return "Alarm: " + this.name +" was set on " +
                getHourFormat(this.hour) + " !";
    }
}

