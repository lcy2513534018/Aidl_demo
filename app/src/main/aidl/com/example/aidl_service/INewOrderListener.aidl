// INewOrderListener.aidl
package com.example.aidl_service;

// Declare any non-default types here with import statements

interface INewOrderListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void NewOrderListener(in String order);
}
