package com.example.user.gachonhelper.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.gachonhelper.R;
import com.example.user.gachonhelper.app.AppConfig;
import com.example.user.gachonhelper.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangki on 2016-06-22.
 */
public class AlterPasswordActivity extends AppCompatActivity {

        private static final String TAG = AlterPasswordActivity.class.getSimpleName();
        private Button btnRegister;
        private Button btnLinkToLogin;
        private EditText inputStudentId;
        private EditText inputPassword;
        private EditText changePassword;
        private ProgressDialog pDialog;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.alter_password);

            inputStudentId = (EditText) findViewById(R.id.stu_id);
            inputPassword = (EditText) findViewById(R.id.password);
            changePassword = (EditText) findViewById(R.id.alter_password);
            btnRegister = (Button) findViewById(R.id.btnRegister);
            btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

            // Progress dialog
            pDialog = new ProgressDialog(this);
            pDialog.setCancelable(false);

            // 변경버튼
            btnRegister.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String stu_id = inputStudentId.getText().toString().trim();
                    String password = inputPassword.getText().toString().trim();
                    String cPassword = changePassword.getText().toString().trim();

                    if (!stu_id.isEmpty() && !password.isEmpty() && !cPassword.isEmpty()) {
                        alterPassword(stu_id, password, cPassword);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "값을 입력하세요", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });
            //취소버튼
            btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(),
                            LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            });

        }
        private void alterPassword(final String stu_id, final String password, final String cPassword) {
            String tag_string_req = "비밀번호 변경";

            pDialog.setMessage("변경중...");
            showDialog();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_ALTER_PASS, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "변경 리스폰스: " + response.toString());
                    hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        boolean error = jObj.getBoolean("error");
                        if (!error) {

                            Toast.makeText(getApplicationContext(), "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_LONG).show();

                            //로그인창으로.
                            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {//에러
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
                    Log.e(TAG, "비밀번호 변경 에러: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    // 파라미터 전달
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("stu_id", stu_id);
                    params.put("password", password);
                    params.put("c_password",cPassword);
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
