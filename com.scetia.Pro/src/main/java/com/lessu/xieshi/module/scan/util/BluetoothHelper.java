package com.lessu.xieshi.module.scan.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * created by Lollipop
 * on 2021/4/1
 */
public class BluetoothHelper {
    private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private InputStream mInputStream;
    private BluetoothSocket mBluetoothSocket;
    private static BluetoothHelper bluetoothHelper;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mBluetoothDevice;

    private BluetoothHelper() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static BluetoothHelper getInstance() {
        if (bluetoothHelper == null) {
            synchronized (BluetoothHelper.class) {
                if (bluetoothHelper == null) {
                    bluetoothHelper = new BluetoothHelper();
                }
            }
        }
        return bluetoothHelper;
    }

    public InputStream getInputStream() {
        try {
            mInputStream = mBluetoothSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mInputStream;
    }

    public BluetoothDevice getBluetoothDevice(String deviceAddress) {
        if(mBluetoothDevice==null){
            mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(deviceAddress);
        }
        return mBluetoothDevice;
    }

    public String connectDevice(){
        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            mBluetoothSocket.connect();
            return mBluetoothDevice.getName();
        } catch (IOException e) {
            e.printStackTrace();
        }
       return null;
    }

    public void closeSocket(){
        if(mBluetoothSocket!=null){
            try {
                mBluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeInputStream(){
        if(mInputStream!=null){
            try {
                mInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
