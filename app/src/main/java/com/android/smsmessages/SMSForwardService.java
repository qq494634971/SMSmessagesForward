package com.android.smsmessages;

import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;


public class SMSForwardService extends Service {
    Uri uri = Uri.parse("content://sms");
    private String lastMsg;
    private DBHelper mHelper;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHelper = new DBHelper(this);
        mHelper.initData();
        System.out.println("创建服务");
        lastMsg = "";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "启动服务...", Toast.LENGTH_SHORT).show();
        getContentResolver().registerContentObserver(uri, true, new MyObserver(new Handler()));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        System.out.println("停止服务");
        super.onDestroy();
    }

    final Handler mHandler = new Handler();
    Runnable r = new Runnable() {

        @Override
        public void run() {
            //每隔1s循环执行run方法
            mHandler.postDelayed(this, 1000);
        }
    };

    private class MyObserver extends ContentObserver {

        public MyObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange);
            Toast.makeText(getApplicationContext(), "短信数据库发生变化了。。。 ", Toast.LENGTH_SHORT).show();

            if (uri.toString().equals("content://sms/raw")) { ////onChange会执行二次,第二次短信才会入库
                Toast.makeText(getApplicationContext(), "onChange one time", Toast.LENGTH_SHORT).show();
                return;
            }
            Uri inboxUri = Uri.parse("content://sms/inbox");
            Cursor cursor = getContentResolver().query(inboxUri,
                    new String[]{"body", "address", "date", "type", "_id"}, null,
                    null, "_id desc");
            cursor.moveToFirst();
            String body = cursor.getString(0);
            String address = cursor.getString(1);
//            String date = cursor.getString(2);
//            String type = cursor.getString(3);
//            int id = cursor.getInt(4);

            final String msgString = String.format("[新短信][%s][%s]%s", address, getBatteryScale(), body);
            new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void run() {
                    SMSForwardHttpClient.SendToDingding(mHelper.queryData(), msgString);
                }
            }).start();
        }
    }

    private String getBatteryScale() {
        BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
        int battery = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            battery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        }
        return battery + "%";
    }
}
