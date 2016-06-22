package com.example.user.gachonhelper.activity;

/**
 * Created by wangki on 2016-05-07.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.example.user.gachonhelper.R;
import com.example.user.gachonhelper.app.AppConfig;
import com.example.user.gachonhelper.app.AppController;
import com.example.user.gachonhelper.helper.SessionManager;

public class RegisterActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputStudentId;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private String myCategory;

    String[] category = {
            "IT대학", "공과대학", "경영대학", "사회과학대학", "법과대학", "인문대학"
            ,"바이오나노대학", "한의과대학", "예술대학", "가천리버럴아츠칼리지", "기타"
    };
    Spinner s1; // spinner

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        s1 = (Spinner) findViewById(R.id.register_category);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, category);
        s1.setAdapter(adapter); //단대 선택하는 스피너 정의.


        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        inputFullName = (EditText) findViewById(R.id.name);
        inputStudentId = (EditText) findViewById(R.id.stu_id);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Session 체크
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this,
                    MenuActivity.class);
            startActivity(intent);
            finish();
        }

        // 버튼 이벤트
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String stu_id = inputStudentId.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                //입력란 확인 및 학번 9자리 확인
                if (!name.isEmpty() && !stu_id.isEmpty() && !password.isEmpty() && stu_id.length() == 9) {
                    registerUser(name, stu_id, password, myCategory);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "값을 모두 또는 정확히 입력하세요", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // 취소 이벤트
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * 데이터베이스로 유저 정보 저장(회원가입)
     *
     * */
    private void registerUser(final String name, final String stu_id,
                              final String password, final String myCategory) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("등록 중...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "회원가입 요청 " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(getApplicationContext(), "회원가입이 완료되었습니다. 다시 로그인 해주세요!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(
                                RegisterActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        //회원가입 중 에러
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // 파라미터 전달
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("stu_id", stu_id);
                params.put("password", password);
                params.put("category",myCategory);

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
}