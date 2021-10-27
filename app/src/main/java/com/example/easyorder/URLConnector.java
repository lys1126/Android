package com.example.easyorder;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLConnector extends Thread{
    private String result;
    private String URL;
    private String parm;

    public URLConnector(String url, String pam) {
        URL = url;
        parm = pam;
    }

    @Override
    public void run() {
        final String output = request(URL, parm);
        result = output;
    }

    public String getResult() {
        return result;
    }

    public String request(String urlStr, String parmStr) {
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                if(parmStr != null) {
                    OutputStream outputStream = conn.getOutputStream();
                    outputStream.write(parmStr.getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();
                }

                int resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())) ;
                    String line = null;
                    while(true) {
                        line = reader.readLine();
                        if (line == null) {
                            break;
                        }
                        output.append(line + "\n");
                    }

                    reader.close();
                    conn.disconnect();
                }
            }
        } catch(Exception ex) {
            Log.e("SampleHTTP", "Exception in processing response.", ex);
            ex.printStackTrace();
        }

        return output.toString();
    }
}
