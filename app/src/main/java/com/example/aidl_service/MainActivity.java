package com.example.aidl_service;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    ServiceConnection serviceConnection;
    INewOrderListener iNewOrderListener;
    ISpeechOrder iSpeechOrder;

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == 100) {
                String order = (String) message.obj;
                Log.d("MainActivity", "指令： " + order);
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent();
        intent.setAction("com.example.aidl");
        intent.setPackage("com.example.aidl_service");
        iNewOrderListener=new INewOrderListener.Stub() {
            @Override
            public void NewOrderListener(String order) throws RemoteException {
                Message message = new Message();
                message.what = 100;
                message.obj=order;
                mHandler.sendMessage(message);
            }
        };

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                iSpeechOrder = ISpeechOrder.Stub.asInterface(iBinder);
                try {
                    iSpeechOrder.registerListener(iNewOrderListener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }
}
