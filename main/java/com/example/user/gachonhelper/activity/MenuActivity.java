package com.example.user.gachonhelper.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.gachonhelper.R;
import com.example.user.gachonhelper.app.AppConfig;
import com.example.user.gachonhelper.app.AppController;
import com.example.user.gachonhelper.helper.SQLiteHandler;
import com.example.user.gachonhelper.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangki on 2016-05-23.
 */
public class MenuActivity extends Activity {

    private static final String TAG = "MenuActivity";
    private TextView txtName;
    private Button btnLogout;
    private ImageButton btnList;
    private ImageButton btnWrite;
    private ImageButton btnRanking;
    private ImageButton btnAlarmCheck;
    private ImageButton btnGachon;
    private Button btnSetting;
    private String stu_id;
    private String token;
    private SQLiteHandler db;
    private SessionManager session;
    private ProgressDialog pDialog;
    private JSONArray result;

    private ArrayList<String> helper_name = new ArrayList<String>();
    private ArrayList<String> helper_stu_id = new ArrayList<String>();
    private ArrayList<String> title = new ArrayList<String>();
    private ArrayList<String> helper_category = new ArrayList<String>();
    private ArrayList<String> helper_tickets = new ArrayList<String>();
    private ArrayList<String> helper_phone = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        txtName = (TextView) findViewById(R.id.name_check);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnList = (ImageButton) findViewById(R.id.menu_list);
        btnWrite = (ImageButton) findViewById(R.id.menu_write);
        btnRanking = (ImageButton) findViewById(R.id.menu_ranking);
        btnAlarmCheck  = (ImageButton) findViewById(R.id.menu_alarmcheck);
        btnGachon = (ImageButton) findViewById(R.id.menu_gachon);
        btnSetting = (Button) findViewById(R.id.menu_title);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        stu_id = user.get("stu_id");

        // Displaying the user details on the screen
        txtName.setText(name + "(" + stu_id + ")");

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(i);
            }
        });

        btnList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnList.getBackground().setColorFilter(0xaa111111, PorterDuff.Mode.SRC_OVER);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnList.getBackground().clearColorFilter();
                    Intent i = new Intent(getApplicationContext(), CategoryActivity.class);
                    startActivity(i);
                }
                return true;
            }
        });

        btnWrite.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnWrite.getBackground().setColorFilter(0xaa111111, PorterDuff.Mode.SRC_OVER);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnWrite.getBackground().clearColorFilter();
                    Intent i = new Intent(getApplicationContext(), WriteActivity.class);
                    startActivity(i);
                }
                return true;
            }
        });

        btnRanking.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    btnRanking.getBackground().setColorFilter(0xaa111111, PorterDuff.Mode.SRC_OVER);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    btnRanking.getBackground().clearColorFilter();
                    Intent i = new Intent(getApplicationContext(), RankingActivity.class);
                    startActivity(i);
                }
                return true;
            }
        });

        btnAlarmCheck.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnAlarmCheck.getBackground().setColorFilter(0xaa111111, PorterDuff.Mode.SRC_OVER);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnAlarmCheck.getBackground().clearColorFilter();
                    getHelperList(stu_id);
                }
                return true;
            }
        });

        // gachon univ. site
        btnGachon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    btnGachon.getBackground().setColorFilter(0xaa111111, PorterDuff.Mode.SRC_OVER);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    btnGachon.getBackground().clearColorFilter();
                    Intent i = new Intent(getApplicationContext(),GachonActivity.class);
                    startActivity(i);
                }
                return true;
            }
        });
    }
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();
        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private void getHelperList(final String helpy_stu_id) {
        String tag_string_req = "req_get_helper_list";


        pDialog.setMessage("목록을 불러오는 중입니다 ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GETHELPER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Helper Response: " + response);
                hideDialog();
                JSONObject jObject = null;
                try {
                    jObject = new JSONObject(response);
                    result = jObject.getJSONArray("result");

                    //제이슨 어레이에 들어있는 항목들 가져오기
                    for (int i = 0; i < result.length(); i++) {
                        try {
                            //Getting json object
                            JSONObject json = result.getJSONObject(i); //result[0],[1],[2]에 있는 오브젝트를 json에 복사
                            helper_name.add(json.getString("helper_name"));
                            helper_stu_id.add(json.getString("helper_stu_id")); //arrayList에 타이틀들을 계속 저장해나감
                            helper_category.add(json.getString("helper_category"));
                            helper_tickets.add(json.getString("helper_tickets"));
                            helper_phone.add(json.getString("helper_phone"));
                            title.add(json.getString("title"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Intent i = new Intent(getApplicationContext(),AlarmCheckActivity.class);
                    i.putStringArrayListExtra("helper_name",helper_name);
                    i.putStringArrayListExtra("helper_stu_id",helper_stu_id);
                    i.putStringArrayListExtra("helper_category",helper_category);
                    i.putStringArrayListExtra("helper_tickets",helper_tickets);
                    i.putStringArrayListExtra("helper_phone",helper_phone);
                    i.putStringArrayListExtra("title",title);
                    startActivity(i);
                    helper_name.clear();
                    helper_stu_id.clear();
                    helper_category.clear();
                    helper_tickets.clear();
                    helper_phone.clear();
                    title.clear();

                } catch (JSONException e) {
                    // JSON 에러
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {         //POST로 category 변수 전달
                // 파라미터 전달
                Map<String, String> params = new HashMap<String, String>();

                params.put("helpy_stu_id", helpy_stu_id);

                return params;
            }


        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                new AlertDialog.Builder(this).setTitle("종료").setMessage("종료 하시겟습니까?").setIcon(R.drawable.dialog_helper).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                }).setNegativeButton("취소", null).show();
                return false;
            default:
                return false;
        }}
}