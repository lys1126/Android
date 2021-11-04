package com.example.easyorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    String martNm;
    int martNo;
    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            calendar.set(Calendar.YEAR, i);
            calendar.set(Calendar.MONTH, i1);
            calendar.set(Calendar.DAY_OF_MONTH, i2);
            updateLabel();
        }
    };
    private ArrayList<BusinessData> bizList;
    private RecyclerView rv;
    private ReListMainAdapter reListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main);

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
        EditText et_srDate = (EditText) findViewById(R.id.et_srDate);
        et_srDate.setText(today);
        et_srDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ListMainActivity.this, datePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String srList = "http://61.105.122.125/android/bizMainList.php";
                String parm = "martNo="+martNo+"&crtDate="+et_srDate.getText();
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
        String param = "martNo="+martNo+"&crtDate="+et_srDate.getText();
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

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText et_srDate = (EditText) findViewById(R.id.et_srDate);
        et_srDate.setText(sdf.format(calendar.getTime()));
    }
}