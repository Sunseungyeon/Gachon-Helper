package com.example.user.gachonhelper.activity;

/**
 * Created by wangki on 2016-06-08.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
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

import java.util.HashMap;
import java.util.Map;

public class SettingActivity extends Activity {

    private static final String TAG = RegisterActivity.class.getSimpleName();

    private SQLiteHandler db;

    TextView stu_id_field, ticket, version;
    Spinner spinnerCategory;
    Switch alarm;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    String myCategory;
    private String stu_id;
    private String[] category = {
            "IT대학", "공과대학", "경영대학", "사회과학대학", "법과대학", "인문대학"
            ,"바이오나노대학", "한의과대학", "예술대학", "가천리버럴아츠칼리지", "기타"
    };
    Button alterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        stu_id_field = (TextView) findViewById(R.id.stu_num_set);
        spinnerCategory = (Spinner) findViewById(R.id.set_category);

        ticket = (TextView) findViewById(R.id.ticket_num_set);
        version = (TextView) findViewById(R.id.version_set);
        alarm = (Switch)findViewById(R.id.alarm_set);
        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();
        editor.putBoolean("Alarm_On_Off", true);
        alterBtn = (Button)findViewById(R.id.change_info_set);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, category);
        spinnerCategory.setAdapter(adapter);

        //스피너 선택 리스너
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        myCategory = "IT";
                        break;
                    case 1:
                        myCategory = "ENGINEER";
                        break;
                    case 2:
                        myCategory = "MANAGEMENT";
                        break;
                    case 3:
                        myCategory = "SOCIAL_SCIENCE";
                        break;
                    case 4:
                        myCategory = "LAW";
                        break;
                    case 5:
                        myCategory = "HUMANITIES";
                        break;
                    case 6:
                        myCategory = "BIO";
                        break;
                    case 7:
                        myCategory = "ORIENTAL_MEDICINE";
                        break;
                    case 8:
                        myCategory = "ART";
                        break;
                    case 9:
                        myCategory = "LIBERAL_ART";
                        break;
                    case 10:
                        myCategory = "OTHER";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });




        version.setText("현재버전 : v1.0.00");

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();


        ticket.setText("티켓 수: " + user.get("tickets"));
        stu_id_field.setText("학번: " + user.get("stu_id"));
        stu_id = user.get("stu_id");
        if(setting.getBoolean("Alarm_On_Off", false)){
            alarm.setChecked(true);
        }

        alarm.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton cb, boolean isChecking) {
                editor.putBoolean("Alarm_On_Off", isChecking);
                editor.commit();
            }
        });
        //버튼 클릭시
        alterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterReq(stu_id,myCategory);
            }
        });

    }

    private void alterReq(final String stu_id,final String select_category) {
        String tag_string_req = "정보 변경 요청";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ALTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(),"변경이 완료되었습니다.",Toast.LENGTH_SHORT).show();
            }


        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "에러: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {         //POST로 category 변수 전달
                // 파라미터 전달
                Map<String, String> params = new HashMap<String, String>();
                params.put("stu_id", stu_id);
                params.put("category",select_category);
                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

        Intent i = new Intent(getApplicationContext(),MenuActivity.class);
        startActivity(i);
        finish();
    }




}