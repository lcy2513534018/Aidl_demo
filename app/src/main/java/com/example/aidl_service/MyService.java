package com.example.aidl_service;

import android.app.ListActivity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MyService extends Service {

    private List<String> order;
    private RemoteCallbackList<INewOrderListener> mRemoteCallbackList;
    private AtomicBoolean atomicBoolean=new AtomicBoolean(false);

    public MyService() {
        Log.e("MyService","MyService");
    }

    private final ISpeechOrder.Stub mSub=new ISpeechOrder.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void registerListener(INewOrderListener listener) throws RemoteException {

            mRemoteCallbackList.register(listener);
        }

        @Override
        public void unRegisterListener(INewOrderListener listener) throws RemoteException {

            mRemoteCallbackList.unregister(listener);

            Log.e("MyService","unRegister"+mRemoteCallbackList.beginBroadcast());
            mRemoteCallbackList.finishBroadcast();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        mRemoteCallbackList=new RemoteCallbackList<>();
        order=new ArrayList<>();
        startWork();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       // throw new UnsupportedOperationException("Not yet implemented");
        return mSub;
    }

    private void startWork(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //如果服务没有onDestory就继续
                    while (!atomicBoolean.get()) {
                        newOrder();
                        Thread.sleep(5000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void newOrder() {
        final int num = mRemoteCallbackList.beginBroadcast();
        String order="打开";
        for (int i = 0; i < num; i++) {
            try {
                //获取集合里面的观察者
                mRemoteCallbackList.getBroadcastItem(i).NewOrderListener(order);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mRemoteCallbackList.finishBroadcast();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        atomicBoolean.set(true);
    }
}
