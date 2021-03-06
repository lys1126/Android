package com.example.easyorder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class InsertActivity extends AppCompatActivity {

    String prodInfo="", martNm;
    int martNo;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(intentResult != null) {
            if(intentResult.getContents() != null) {
                Log.e("test", "ifif");
                String searchURL = "http://61.105.122.125/android/prodSearch.php";
                String parm = "bar_no=" + intentResult.getContents().trim();
                Log.e("param", parm);
                URLConnector task = new URLConnector(searchURL, parm);
                task.start();

                try{
                    task.join();
                    System.out.println("waiting... for result");
                }
                catch(InterruptedException e){

                }
                prodInfo = task.getResult();
                try{
                    JSONObject pInfo = new JSONObject(prodInfo);
                    int rowNum = Integer.parseInt(pInfo.getString("rownum"));
                    if(rowNum > 0) {
                        Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
                        intent.putExtra("martNo", martNo);
                        intent.putExtra("martNm", martNm);
                        intent.putExtra("prodInfo", prodInfo);
                        startActivityForResult(intent, 1);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "???????????? ?????? ??????????????????", Toast.LENGTH_SHORT).show();
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        String martInfoURL = "http://61.105.122.125/android/martList.php";
        URLConnector task = new URLConnector(martInfoURL, null);
        task.start();

        try{
            task.join();
        }
        catch(InterruptedException e){

        }

        String result = task.getResult();
        String[] nmArr = {};
        Map<Integer, String> map = new HashMap<Integer, String>();
        try {
            JSONObject martList = new JSONObject(result);
            JSONArray ja = martList.getJSONArray("result");
            nmArr = new String[ja.length()];
            for(int i=0; i<ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                nmArr[i] = jo.getString("mart_nm");
                map.put(jo.getInt("mart_no"), jo.getString("mart_nm"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_spinner, nmArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                martNm = spinner.getSelectedItem().toString();
                Set<Map.Entry<Integer, String>> entrySet = map.entrySet();
                for(Map.Entry<Integer, String> entry : entrySet) {
                    if(entry.getValue().equals(martNm)) {
                        martNo = entry.getKey();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button btn_camera = (Button) findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(InsertActivity.this);
                integrator.setOrientationLocked(true);
                integrator.setPrompt("???????????? ????????? ?????? ???????????????");
                integrator.initiateScan();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}