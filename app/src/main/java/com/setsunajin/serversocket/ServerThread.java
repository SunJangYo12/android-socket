package com.setsunajin.serversocket;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread {
    Context context;
    boolean isResult = false;
    String dataResult = "";

    public ServerThread() {
    }
    public void sendResult(String data) {
        isResult = true;
        dataResult = data;
    }

    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9090);

            boolean mainRun = true;
            while(mainRun) {
                Log.i("serverSetsuna", "Menunggu koneksi diport 9090");

                try (Socket socket = serverSocket.accept())
                {
                    Log.i("serverSetsuna", "Terhubung dengan "+socket.getInetAddress().getHostAddress());

                    try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                        char[] buffer = new char[1000];
                        int index;
                        String pesan = "";

                        while ((index = in.read(buffer, 0, 1000))  != 0) {
                            pesan = new String(buffer, 0, index);
                            String xx = ""+buffer[index -1];

                            if (xx.equals("\n\n"))
                                Log.i("serverSetsuna", "pathc");
                        }
                    }
                }
                Log.i("serverSetsuna", "Koneksi tutup");
            }
        } catch (final IOException e) {
            Log.i("serverSetsuna", "Kesalahan koneksi: "+ e.getMessage());

        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                }catch(IOException e) {
                    Log.i("serverSetsuna", "Tidak dapat menutup socket: "+e.getMessage());
                }
            }
        }
    }
}
