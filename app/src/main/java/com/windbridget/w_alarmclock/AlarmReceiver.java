package com.windbridget.w_alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String state = intent.getExtras().getString("extra");
        Log.e("MyActivity", "In the receiver with " + state);

        String wind_id = intent.getExtras().getString("quote id");
        Log.e("Wind quote is" , wind_id);

        Intent serviceIntent = new Intent(context,RingtonePlayingService.class);
        serviceIntent.putExtra("extra", state);
        serviceIntent.putExtra("quote id", wind_id);

        context.startService(serviceIntent);
    }

}
