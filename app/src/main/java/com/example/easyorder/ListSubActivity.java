package com.example.easyorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class ListSubActivity extends AppCompatActivity {

    private ArrayList<BusinessData> bizList;
    public static ArrayList<BusinessData> temp = new ArrayList<BusinessData>();
    private RecyclerView rv;
    private ReListSubAdapter reListAdapter;
    private boolean chk=false;
    public static TextView tv_totPrice;
    public static Button btn_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sub);

        Intent intent = getIntent();
        int martNo = intent.getIntExtra("martNo", 0);
        String martNm = intent.getStringExtra("martNm");
        String crtDate = intent.getStringExtra("srDate");

        TextView tv_martNm = (TextView) findViewById(R.id.tv_martNm);
        tv_martNm.setText(martNm);
        TextView tv_crtDate = (TextView) findViewById(R.id.tv_crtDate);
        tv_crtDate.setText(crtDate);

        String bizMainList = "http://61.105.122.125/android/bizSubList.php";
        String param = "martNo="+martNo+"&crtDate="+crtDate;
        URLConnector task = new URLConnector(bizMainList, param);
        task.start();
        try{
            task.join();
            System.out.println("waiting... fo result");
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        String dResult = task.getResult();
        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        bizList = new ArrayList<BusinessData>();
        reListAdapter = new ReListSubAdapter(bizList);
        rv.setAdapter(reListAdapter);
        try{
            JSONObject srObject = new JSONObject(dResult);
            int rowNum = Integer.parseInt(srObject.getString("rownum"));
            JSONArray ja = srObject.getJSONArray("result");
            if(rowNum > 0) {
                for(int i=0; i<ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    BusinessData bizData = new BusinessData();
                    bizData.setBizNo(Integer.parseInt(jo.getString("bizNo").toString()));
                    bizData.setProdNm(jo.getString("prodNm").toString());
                    bizData.setUPrice(Integer.parseInt(jo.getString("uPrice").toString()));
                    bizData.setAmount(Integer.parseInt(jo.getString("amount").toString()));
                    bizData.setPrice(Integer.parseInt(jo.getString("price").toString()));
                    bizList.add(bizData);
                    reListAdapter.notifyDataSetChanged();
                }

                for(int i=0; i<ja.length(); i++) { //수정버튼 enable 비교를 위한 for문
                    JSONObject jo = ja.getJSONObject(i);
                    BusinessData bizData = new BusinessData();
                    bizData.setBizNo(Integer.parseInt(jo.getString("bizNo").toString()));
                    bizData.setProdNm(jo.getString("prodNm").toString());
                    bizData.setUPrice(Integer.parseInt(jo.getString("uPrice").toString()));
                    bizData.setAmount(Integer.parseInt(jo.getString("amount").toString()));
                    bizData.setPrice(Integer.parseInt(jo.getString("price").toString()));
                    temp.add(bizData);
                }
            } else {
                Toast.makeText(getApplicationContext(), "데이터가 존재하지 않습니다", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        int totPrice = 0;
        for(int i=0; i<bizList.size(); i++) {
            totPrice += bizList.get(i).getPrice();
        }
        tv_totPrice = (TextView) findViewById(R.id.tv_totPrice);
        tv_totPrice.setText(totPrice+"원");

        btn_update = (Button) findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i<bizList.size(); i++) {
                    if(bizList.get(i).getAmount() != temp.get(i).getAmount() || bizList.get(i).getUPrice() != temp.get(i).getUPrice()) {
                        String bizMainList = "http://61.105.122.125/android/bizUpdate.php";
                        String param = "bizNo="+bizList.get(i).getBizNo()+"&amount="+bizList.get(i).getAmount()+"&uPrice="+bizList.get(i).getUPrice();
                        Log.e("paramTest", param);
                        URLConnector task = new URLConnector(bizMainList, param);
                        task.start();
                        try{
                            task.join();
                            System.out.println("waiting... fo result");
                        } catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                        String upResult = task.getResult().trim();
                        Log.e("updateTest", upResult);
                        if(upResult.equals("success")) {
                            chk = true;
                        } else if(upResult.equals("fail")) {
                            chk = false;
                        }
                    }
                }

                if(chk) {
                    Intent intent = new Intent(getApplicationContext(), ListMainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "수정이 실패했습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ListMainActivity.class);
        startActivity(intent);
        finish();
    }
}