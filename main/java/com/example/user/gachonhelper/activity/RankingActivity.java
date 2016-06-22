package com.example.user.gachonhelper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.gachonhelper.R;
import com.example.user.gachonhelper.app.AppConfig;
import com.example.user.gachonhelper.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangki on 2016-06-12.
 */
public class RankingActivity extends Activity {
    private static final String TAG = RankingActivity.class.getSimpleName();
    TextView first_name, first_stu_id, first_ticket;
    TextView second_name, second_stu_id, second_ticket;
    TextView third_name, third_stu_id, third_ticket;
    private JSONArray result;
    private ArrayList<String> name = new ArrayList<String>();
    private ArrayList<String> stu_id = new ArrayList<String>();
    private ArrayList<String> tickets = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);

        first_name = (TextView)findViewById(R.id.first_name);
        first_stu_id = (TextView)findViewById(R.id.first_stu_id);
        first_ticket = (TextView)findViewById(R.id.first_ticket);
        second_name = (TextView)findViewById(R.id.second_name);
        second_stu_id = (TextView)findViewById(R.id.second_stu_id);
        second_ticket = (TextView)findViewById(R.id.second_ticket);
        third_name = (TextView)findViewById(R.id.third_name);
        third_stu_id = (TextView)findViewById(R.id.third_stu_id);
        third_ticket = (TextView)findViewById(R.id.third_ticket);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_RANK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Rank Response: " + response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    result = jObj.getJSONArray("result");
                    for(int i=0;i<result.length();i++) {
                        try {
                            JSONObject json = result.getJSONObject(i); //result[0],[1],[2]에 있는 오브젝트를 json에 복사
                            name.add(json.getString("name"));
                            stu_id.add(json.getString("stu_id")); //arrayList에 타이틀들을 계속 저장해나감
                            tickets.add(json.getString("tickets"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    first_name.setText(" 1등 " + name.get(0).toString());
                    first_stu_id.setText(stu_id.get(0).toString());
                    first_ticket.setText(tickets.get(0).toString());
                    second_name.setText(" 2등 " + name.get(1).toString());
                    second_stu_id.setText(stu_id.get(1).toString());
                    second_ticket.setText(tickets.get(1).toString());
                    third_name.setText(" 3등 " + name.get(2).toString());
                    third_stu_id.setText(stu_id.get(2).toString());
                    third_ticket.setText(tickets.get(2).toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {         //POST로 category 변수 전달
                // 파라미터 전달
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, "rank");

    }
}