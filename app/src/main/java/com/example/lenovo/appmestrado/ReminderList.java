package com.example.lenovo.appmestrado;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by lenovo on 12/01/2018.
 */

public class ReminderList extends ArrayAdapter<String> {


    private Activity activity;

    private String[] name;
    private String[] date;
    private String[] message;
    private String[] type;

    public ReminderList(Activity activity, String[] name, String[] date, String[] message, String[] type) {
        super(activity, R.layout.list_reminders, name);

        this.activity = activity;
        this.name = name;
        this.date = date;
        this.message = message;
        this.type = type;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_reminders, null, true);

        TextView tvNameList = (TextView) rowView.findViewById(R.id.tvNameList);
        TextView tvMessageList = (TextView) rowView.findViewById(R.id.tvMessageList);
        TextView tvDateTimeList = (TextView) rowView.findViewById(R.id.tvDateTimeList);
        TextView tvTypeList = (TextView) rowView.findViewById(R.id.tvTypeList);

        tvNameList.setText(name[position]);
        tvMessageList.setText(message[position]);

        if (type[position].equals(Util.TYPE_DAILY)) {
            tvTypeList.setText("Diário");

            Calendar calendar = new GregorianCalendar();
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.ROOT).parse(this.date[position]);

                calendar.setTime(date);

                Integer hour = calendar.get(Calendar.HOUR);
                if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
                    hour += 12;
                }

                Integer minute = calendar.get(Calendar.MINUTE);
                String time = String.format(Locale.ROOT, "%02d", hour) + ":" + String.format(Locale.ROOT, "%02d", minute);

                tvDateTimeList.setText(time);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (type[position].equals(Util.TYPE_UNIQUE)) {
            tvTypeList.setText("Único");

            Calendar calendar = new GregorianCalendar();
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.ROOT).parse(this.date[position]);

                calendar.setTime(date);
                Integer day = calendar.get(Calendar.DAY_OF_MONTH);
                Integer month = calendar.get(Calendar.MONTH) + 1;
                Integer year = calendar.get(Calendar.YEAR);

                Integer hour = calendar.get(Calendar.HOUR);
                if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
                    hour += 12;
                }
                Integer minute = calendar.get(Calendar.MINUTE);
                String time = String.format(Locale.ROOT, "%02d", day) + "/" + String.format(Locale.ROOT, "%02d", month) + "/" + String.format(Locale.ROOT, "%02d", year) + " às " + String.format(Locale.ROOT, "%02d", hour) + ":" + String.format(Locale.ROOT, "%02d", minute);

                tvDateTimeList.setText(time);


            } catch (Exception e) {
                e.printStackTrace();
            }


        } else if (type[position].equals(Util.TYPE_WEEKLY)) {
            tvTypeList.setText("Semanal");

            Calendar calendar = new GregorianCalendar();
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS", Locale.ROOT).parse(this.date[position]);

                calendar.setTime(date);
                Integer day = calendar.get(Calendar.DAY_OF_WEEK);
                String time = "";

                switch (day) {
                    case Calendar.SUNDAY:
                        time += "Domingos";
                        break;
                    case Calendar.MONDAY:
                        time += "Segundas";
                        break;
                    case Calendar.TUESDAY:
                        time += "Terças";
                        break;
                    case Calendar.WEDNESDAY:
                        time += "Quartas";
                        break;
                    case Calendar.THURSDAY:
                        time += "Quintas";
                        break;
                    case Calendar.FRIDAY:
                        time += "Sextas";
                        break;
                    case Calendar.SATURDAY:
                        time += "Sabados";
                        break;
                }

                Integer hour = calendar.get(Calendar.HOUR);
                if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
                    hour += 12;
                }
                Integer minute = calendar.get(Calendar.MINUTE);
                time += " às " + String.format(Locale.ROOT, "%02d", hour) + ":" + String.format(Locale.ROOT, "%02d", minute);

                tvDateTimeList.setText(time);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return rowView;
    }
}
