package com.example.easyorder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PopupActivity extends Activity {
    EditText et_barNo, et_prodNm, et_sPrice, et_uPrice, et_amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀바 x
        setContentView(R.layout.activity_popup);

        Intent intent = getIntent();
        String martNm = intent.getStringExtra("martNm");
        String prodInfo = intent.getStringExtra("prodInfo");
        et_barNo = (EditText) findViewById(R.id.et_barNo);
        et_prodNm = (EditText) findViewById(R.id.et_prodNm);
        et_sPrice = (EditText) findViewById(R.id.et_sPrice);
        et_uPrice = (EditText) findViewById(R.id.et_uPrice);
        et_amount = (EditText) findViewById(R.id.et_amount);
        try {
            JSONObject pInfo = new JSONObject(prodInfo);
            JSONArray ja = pInfo.getJSONArray("result");
            for(int i=0; i<ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                et_barNo.setText(jo.getString("bar_no"));
                et_prodNm.setText(jo.getString("prod_nm"));
                et_sPrice.setText(jo.getString("s_price"));
                et_uPrice.setText(jo.getString("u_price"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void mOnclose(View v) {//더 채울 예정
        Intent intent = new Intent(getApplicationContext(), InsertActivity.class);
        startActivity(intent);
        finish();
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
        return;
    }
}