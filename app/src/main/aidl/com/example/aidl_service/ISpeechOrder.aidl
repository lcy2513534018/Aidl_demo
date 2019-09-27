// ISpeechOrder.aidl
package com.example.aidl_service;

import com.example.aidl_service.INewOrderListener;

// Declare any non-default types here with import statements

interface ISpeechOrder {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    //注册监听
     void registerListener(INewOrderListener listener);

     //注销监听
     void unRegisterListener(INewOrderListener listener);

}
