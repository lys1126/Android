package com.example.easyorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ListMainActivity extends AppCompatActivity {

    String martNm, gubun;
    int martNo;
    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            calendar.set(Calendar.YEAR, i);
            calendar.set(Calendar.MONTH, i1);
            calendar.set(Calendar.DAY_OF_MONTH, i2);
            updateLabel(gubun);
        }
    };
    private ArrayList<BusinessData> bizList;
    private RecyclerView rv;
    private ReListMainAdapter reListAdapter;
    public static Activity finishAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);

        finishAct = ListMainActivity.this;

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
            nmArr = new String[ja.length()+1];
            nmArr[0] = "-----";
            for(int i=0; i<ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                nmArr[i+1] = jo.getString("mart_nm");
                map.put(jo.getInt("mart_no"), jo.getString("mart_nm"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_spinner, nmArr);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner.getSelectedItem().toString().equals("-----")) {
                    martNo = 0;
                } else {
                    martNm = spinner.getSelectedItem().toString();
                    Set<Map.Entry<Integer, String>> entrySet = map.entrySet();
                    for(Map.Entry<Integer, String> entry : entrySet) {
                        if(entry.getValue().equals(martNm)) {
                            martNo = entry.getKey();
                            return;
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = simpleDateFormat.format(mDate);
        Calendar day = Calendar.getInstance();
        day.add(Calendar.DATE, -7);
        String beforeDay = simpleDateFormat.format(day.getTime());
        EditText et_srStDate = (EditText) findViewById(R.id.et_srStDate);
        et_srStDate.setText(beforeDay);
        et_srStDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ListMainActivity.this, datePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                gubun = "start";
            }
        });

        EditText et_srEndDate = (EditText) findViewById(R.id.et_srEndDate);
        et_srEndDate.setText(today);
        et_srEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ListMainActivity.this, datePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                gubun = "end";
            }
        });

        Button btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String srList = "http://61.105.122.125/android/bizMainList.php";
                String parm = "martNo="+martNo+"&stDate="+et_srStDate.getText()+"&endDate="+et_srEndDate.getText();
                URLConnector t = new URLConnector(srList, parm);
                t.start();
                try{
                    t.join();
                    System.out.println("waiting... fo result");
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }

                String srResult = t.getResult();
                bizList = new ArrayList<BusinessData>();
                reListAdapter = new ReListMainAdapter(getApplicationContext(), bizList);
                rv.setAdapter(reListAdapter);
                try{
                    JSONObject srObject = new JSONObject(srResult);
                    int rowNum = Integer.parseInt(srObject.getString("rownum"));
                    JSONArray ja = srObject.getJSONArray("result");
                    if(rowNum > 0) {
                        for(int i=0; i<ja.length(); i++) {
                            JSONObject jo = ja.getJSONObject(i);
                            BusinessData bizData = new BusinessData();
                            bizData.setMartNo(Integer.parseInt(jo.getString("martNo")));
                            Set<Map.Entry<Integer, String>> entrySet = map.entrySet();
                            String srMartNm="";
                            for(Map.Entry<Integer, String> entry : entrySet) {
                                if(entry.getKey() == Integer.parseInt(jo.getString("martNo"))) {
                                    srMartNm = entry.getValue();
                                }
                            }
                            bizData.setMartNm(srMartNm);
                            bizData.setCrtDate(jo.getString("crtDate"));
                            bizData.setTotAmount(Integer.parseInt(jo.getString("totAmount")));
                            bizData.setTotPrice(Integer.parseInt(jo.getString("totPrice")));
                            bizList.add(bizData);
                            reListAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "데이터가 존재하지 않습니다", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        String bizMainList = "http://61.105.122.125/android/bizMainList.php";
        String param = "martNo="+martNo+"&stDate="+et_srStDate.getText()+"&endDate="+et_srEndDate.getText();Log.e("param", param);
        task = new URLConnector(bizMainList, param);
        task.start();
        try{
            task.join();
            System.out.println("waitin... fo result");
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        String dResult = task.getResult();
        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        bizList = new ArrayList<BusinessData>();
        reListAdapter = new ReListMainAdapter(this, bizList);
        rv.setAdapter(reListAdapter);
        try{
            JSONObject srObject = new JSONObject(dResult);
            int rowNum = Integer.parseInt(srObject.getString("rownum"));
            JSONArray ja = srObject.getJSONArray("result");
            if(rowNum > 0) {
                for(int i=0; i<ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    BusinessData bizData = new BusinessData();
                    bizData.setMartNo(Integer.parseInt(jo.getString("martNo")));
                    Set<Map.Entry<Integer, String>> entrySet = map.entrySet();
                    String srMartNm="";
                    for(Map.Entry<Integer, String> entry : entrySet) {
                        if(entry.getKey() == Integer.parseInt(jo.getString("martNo"))) {
                            srMartNm = entry.getValue();
                        }
                    }
                    bizData.setMartNm(srMartNm);
                    bizData.setCrtDate(jo.getString("crtDate"));
                    bizData.setTotAmount(Integer.parseInt(jo.getString("totAmount")));
                    bizData.setTotPrice(Integer.parseInt(jo.getString("totPrice")));
                    bizList.add(bizData);
                    reListAdapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(getApplicationContext(), "데이터가 존재하지 않습니다", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateLabel(String gubun) {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        if(gubun.equals("start")) {
            EditText et_srStDate = (EditText) findViewById(R.id.et_srStDate);
            et_srStDate.setText(sdf.format(calendar.getTime()));
        } else if(gubun.equals("end")) {
            EditText et_srEndDate = (EditText) findViewById(R.id.et_srEndDate);
            et_srEndDate.setText(sdf.format(calendar.getTime()));
        }

    }
}