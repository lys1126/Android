package com.example.easyorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PopupActivity extends AppCompatActivity {

    Activity finishActivity;
    String prodInfo;
    int martNo, prodNo, status=-999;
    private ArrayList<BusinessData> insertList;
    private RecyclerView recyclerView;
    private RecycleAdapter recycleAdapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(intentResult != null) {
            if(intentResult.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                if(insertList.size() == 1) { //insert했는지 안했는지 확인
                    finishActivity.finish();
                    return;
                } else if(insertList.size() > 1) {
                    finish();
                }


            } else { // 팝업 refresh
                String searchURL = "http://61.105.122.125/android/prodSearch.php";
                String parm = "bar_no=" + intentResult.getContents();
                URLConnector task = new URLConnector(searchURL, parm);
                task.start();

                try{
                    task.join();
                    System.out.println("waiting... for result");
                }
                catch(InterruptedException e){

                }
                prodInfo = task.getResult();
                try {
                    JSONObject pInfo = new JSONObject(prodInfo);
                    JSONArray ja = pInfo.getJSONArray("result");
                    for(int i=0; i<ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);
                        prodNo = Integer.parseInt(jo.getString("prod_no"));
                        BusinessData bizData = new BusinessData();
                        bizData.setProdNm(jo.getString("prod_nm"));
                        bizData.setUPrice(Integer.parseInt(jo.getString("u_price")));
                        insertList.add(bizData);
                        Log.e("bizData", bizData.getProdNm());
                        recycleAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
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
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀바 x
        setContentView(R.layout.activity_popup);

        finishActivity = PopupActivity.this;

        Intent intent = getIntent();
        martNo = intent.getIntExtra("martNo", 0);
        String prodInfo = intent.getStringExtra("prodInfo");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        insertList = new ArrayList<>();
        recycleAdapter = new RecycleAdapter(insertList);
        recyclerView.setAdapter(recycleAdapter);

        try {
            JSONObject pInfo = new JSONObject(prodInfo);
            JSONArray ja = pInfo.getJSONArray("result");
            for(int i=0; i<ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                prodNo = Integer.parseInt(jo.getString("prod_no"));
                BusinessData bizData = new BusinessData();
                bizData.setProdNm(jo.getString("prod_nm"));
                bizData.setUPrice(Integer.parseInt(jo.getString("u_price")));
                insertList.add(bizData);
                recycleAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraOpen();
            }
        });

        Button btn_finish = (Button) findViewById(R.id.btn_finish);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String insertURL = "http://61.105.122.125/android/bizInsert.php";
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        cameraOpen();
    }

    public void cameraOpen() {
        IntentIntegrator integrator = new IntentIntegrator(PopupActivity.this);
        integrator.setOrientationLocked(true);
        integrator.setPrompt("바코드를 사각형 안에 비춰주세요");
        integrator.initiateScan();
    }
}