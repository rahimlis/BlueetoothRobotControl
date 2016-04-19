package com.tutsplus.matt.bluetoothscanner;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tutsplus.matt.bluetoothscanner.Connecting.ManageConnectThread;

import java.io.IOException;
import java.util.UUID;

import lejos.remote.ev3.RemoteRequestEV3;

public class ControlActivity extends AppCompatActivity implements View.OnTouchListener {
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private ConnectThread connectThread;
    private RemoteRequestEV3 ev3;
    private ManageConnectThread thread;
    private UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private Button up, down, leftUp, leftDown, rightUp, rightDown, connectBT;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        Intent intent = getIntent();
        device = intent.getParcelableExtra("device");

        up = (Button) findViewById(R.id.button_up);
        down = (Button) findViewById(R.id.button_down);
        leftUp = (Button) findViewById(R.id.button_left_up);
        leftDown = (Button) findViewById(R.id.button_left_down);
        rightUp = (Button) findViewById(R.id.button_right_up);
        rightDown = (Button) findViewById(R.id.button_right_down);
        up.setOnTouchListener(this);
        down.setOnTouchListener(this);

        rightUp.setOnTouchListener(this);
        rightDown.setOnTouchListener(this);

        leftDown.setOnTouchListener(this);
        leftUp.setOnTouchListener(this);

        connectBT = (Button) findViewById(R.id.button_connect);

        Log.d("CONTROL", device.getName());
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    public void onConnectClick(View view) {
        connectThread = new ConnectThread(device, mUUID);
        if (connectThread.connect()) {
            Toast.makeText(this, "Connected", Toast.LENGTH_LONG).show();
            connectBT.setBackgroundColor(getResources().getColor(R.color.green));
            connectBT.setTextColor(getResources().getColor(R.color.white));
        }
        socket = connectThread.getbTSocket();
        thread = new ManageConnectThread();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            try {
                switch (v.getId()) {
                    case R.id.button_up:
                        thread.sendData(socket, 'f');
                        Log.d("clicked", "forward");
                        break;
                    case R.id.button_down:
                        thread.sendData(socket, 'b');
                        Log.d("clicked", "back");
                        break;
                    case R.id.button_left_up:
                        thread.sendData(socket, 'q');
                        Log.d("clicked", "up left");
                        break;
                    case R.id.button_left_down:
                        thread.sendData(socket, 'z');
                        Log.d("clicked", "down left");
                        break;
                    case R.id.button_right_up:
                        thread.sendData(socket, 'w');
                        Log.d("clicked", "up right");
                        break;
                    case R.id.button_right_down:
                        thread.sendData(socket, 'x');
                        Log.d("clicked", "down right");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            try {
                thread.sendData(socket, 's');
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("clicked", "stop");
        }
        return false;
    }
}
