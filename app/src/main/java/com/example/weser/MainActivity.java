package com.example.weser;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button setAlarm;

    @SuppressLint("ScheduleExactAlarm")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        setAlarm = findViewById(R.id.alarm_button);

        setAlarm.setOnClickListener(v -> {
            MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("выберите время для будильника ")
                    .build();
            materialTimePicker.addOnPositiveButtonClickListener(view ->{
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.MINUTE, materialTimePicker.getMinute());
                calendar.set(Calendar.HOUR_OF_DAY, materialTimePicker.getHour());

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                // Используйте FLAG_IMMUTABLE для PendingIntent, который не должен быть изменен
                AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(
                        calendar.getTimeInMillis(), getAlarmInfoPendingIntent());

                alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingintent());
                Toast.makeText(this, "Будильник установлен для " + sdf.format(calendar.getTime()), Toast.LENGTH_LONG).show();

            });
            materialTimePicker.show(getSupportFragmentManager(), "tag_picker");
        });
    }

    private PendingIntent getAlarmInfoPendingIntent(){
        Intent alarmInfoIntent = new Intent(this, MainActivity.class);
        alarmInfoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        // Используйте FLAG_IMMUTABLE для PendingIntent, который не должен быть изменен
        return PendingIntent.getActivity(this, 0, alarmInfoIntent, PendingIntent.FLAG_IMMUTABLE);
    }

    private PendingIntent getAlarmActionPendingintent(){
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        // Используйте FLAG_IMMUTABLE для PendingIntent, который не должен быть изменен
        return PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_IMMUTABLE);
    }
}
