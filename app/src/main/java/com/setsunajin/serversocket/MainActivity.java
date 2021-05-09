package com.setsunajin.serversocket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public void requestFinish() {
        MainActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
        ServerThread serverThread = new ServerThread();

        try {
            if (getIntent().getStringExtra("shun_req").equals("finish")){
                this.finish();
            }
            else {
                serverThread.context = this;
                serverThread.start();
            }
        }catch (Exception e) {
            serverThread.context = this;
            serverThread.start();
        }
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "zzz", Toast.LENGTH_SHORT).show();
                serverThread.sendResult("hallo");
            }
        });

        serverUDP(this);
    }

    private void serverUDP(Context context) {
        try {
            DatagramSocket udpSocket = new DatagramSocket(9999);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    byte[] buffer = new byte[1];
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    while (true) {
                        try {
                            udpSocket.receive(packet);
                            String message = new String(packet.getData()).trim();
                            Log.i("setsuna", message);
                        } catch (IOException e) {}
                    }
                }
            }).start();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}