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

import androidx.annotation.NonNull;
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
    int martNo, prodNo;
    private ArrayList<BusinessData> insertList;
    private RecyclerView recyclerView;
    private RecycleAdapter recycleAdapter;
    public static TextView tv_total;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(intentResult != null) {
            if(intentResult.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                if(insertList.size() == 0) { //insert했는지 안했는지 확인
                    finishActivity.finish();
                    return;
                }


            } else { //다음버튼 클릭 후 바코드 읽어오기
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
                    int rowNum = Integer.parseInt(pInfo.getString("rownum"));
                    JSONArray ja = pInfo.getJSONArray("result");
                    if(rowNum > 0) {
                        for(int i=0; i<ja.length(); i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            prodNo = Integer.parseInt(jo.getString("prod_no"));
                            BusinessData bizData = new BusinessData();
                            bizData.setMartNo(martNo);
                            bizData.setProdNo(prodNo);
                            bizData.setProdNm(jo.getString("prod_nm"));
                            bizData.setUPrice(Integer.parseInt(jo.getString("u_price")));
                            insertList.add(bizData);
                            recycleAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "바코드를 다시 인식해주세요", Toast.LENGTH_LONG).show();
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
        tv_total = (TextView) findViewById(R.id.tv_total);

        Intent intent = getIntent();
        martNo = intent.getIntExtra("martNo", 0);
        String prodInfo = intent.getStringExtra("prodInfo");
        String martNm = intent.getStringExtra("martNm");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        insertList = new ArrayList<>();
        recycleAdapter = new RecycleAdapter(insertList);
        recyclerView.setAdapter(recycleAdapter);
        try {
            JSONObject pInfo = new JSONObject(prodInfo);
            int rowNum = Integer.parseInt(pInfo.getString("rownum"));
            JSONArray ja = pInfo.getJSONArray("result");
            if(rowNum > 0) {
                for(int i=0; i<ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    prodNo = Integer.parseInt(jo.getString("prod_no"));
                    BusinessData bizData = new BusinessData();
                    bizData.setMartNo(martNo);
                    bizData.setProdNo(prodNo);
                    bizData.setProdNm(jo.getString("prod_nm"));
                    bizData.setUPrice(Integer.parseInt(jo.getString("u_price")));
                    insertList.add(bizData);
                    recycleAdapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(getApplicationContext(), "바코드를 다시 인식해주세요", Toast.LENGTH_LONG).show();
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
                boolean chk = false;
                for(int i=0; i<insertList.size(); i++) {
                    if(insertList.get(i).getAmount() == 0) {
                        Toast.makeText(getApplicationContext(), "수량을 입력하세요", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        BusinessData bizData = insertList.get(i);
                        String param = "martNo="+bizData.getMartNo()+"&prodNo="+bizData.getProdNo()
                                +"&amount="+bizData.getAmount()+"&price="+bizData.getPrice();
                        URLConnector task = new URLConnector(insertURL, param);
                        task.start();

                        try{
                            task.join();
                            Log.d("connectorStatus", "waiting... for result");
                        } catch(InterruptedException e) {

                        }
                        String result = task.getResult();
                        if(Integer.parseInt(result.trim()) > 0) {
                            chk = true;
                        } else if(Integer.parseInt(result.trim()) < 0) {
                            chk = false;
                        }
                    }
                }

                if(chk) {
                    Log.e("chkTest", "성공이다!!");
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(getApplicationContext(), martNm+" 등록이 완료되었습니다", Toast.LENGTH_LONG).show();
                } else {
                    Log.e("chkTest", "실패다!!");
                    Toast.makeText(getApplicationContext(), martNm+" 등록이 실패했습니다", Toast.LENGTH_LONG).show();
                }
            }
        });
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