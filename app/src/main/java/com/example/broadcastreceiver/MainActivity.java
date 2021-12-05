package com.example.broadcastreceiver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView tvmensaje, tvBatteryPct;
    private ImageView imImagen;
    private IntentFilter chargingFilter;
    private MediaPlayer mp;
    private BroadCastReciverCargando broadCastReciverCargando;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init(){
        tvmensaje = (TextView) findViewById(R.id.tvMensaje);
        chargingFilter = new IntentFilter();
        tvBatteryPct = (TextView) findViewById(R.id.tvBatteryPct);
        chargingFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        chargingFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        chargingFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        broadCastReciverCargando = new BroadCastReciverCargando();

    }

    private class BroadCastReciverCargando extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = level * 100 / (float)scale;
            tvBatteryPct.setText((int)batteryPct + "%");
            if (action.equals(Intent.ACTION_POWER_CONNECTED)){
                tvmensaje.setText(R.string.cargando);
                tvmensaje.setTextColor(Color.GREEN);
            } else if(action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
                tvmensaje.setText(R.string.noCargando);
                tvmensaje.setTextColor(Color.BLUE);
            } else if (action.equals(Intent.ACTION_BATTERY_LOW)) {
                tvmensaje.setText(R.string.bateriaBaja);
                tvmensaje.setTextColor(Color.RED);
            }
            if (batteryPct <= 10) {
                tvBatteryPct.setTextColor(Color.RED);
            } else if(batteryPct >= 11 && batteryPct <= 69) {
                tvBatteryPct.setTextColor(Color.parseColor("#FFA500"));
            } else if(batteryPct >= 70 && batteryPct <= 100) {
                tvBatteryPct.setTextColor(Color.GREEN);
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadCastReciverCargando, chargingFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadCastReciverCargando);
    }
}