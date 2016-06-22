package com.example.user.gachonhelper.activity;

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.gachonhelper.R;
import com.example.user.gachonhelper.app.AppConfig;
import com.example.user.gachonhelper.app.AppController;
import com.example.user.gachonhelper.helper.SQLiteHandler;
import com.example.user.gachonhelper.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangki on 2016-05-26.
 */
public class WriteActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnOK;
    private Button btnCancel;
    private EditText inputTitle;
    private EditText content;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private String myCategory;

    String[] category = {
            "IT대학", "공과대학", "경영대학", "사회과학대학", "법과대학", "인문대학"
            ,"바이오나노대학", "한의과대학", "예술대학", "가천리버럴아츠칼리지", "기타"
    };
    Spinner s1; // spinner

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.write);

        inputTitle = (EditText) findViewById(R.id.content_title);
        content = (EditText) findViewById(R.id.contents);
        btnOK = (Button) findViewById(R.id.write_ok);
        btnCancel = (Button) findViewById(R.id.write_cancel);
        s1 = (Spinner) findViewById(R.id.select_category);
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

        db = new SQLiteHandler(getApplicationContext());

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());


        // 유저 세션 확인
        if (!session.isLoggedIn()) {
            Intent intent = new Intent(WriteActivity.this,
                    LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                HashMap<String, String> user = db.getUserDetails();

                String category = myCategory;
                String name = user.get("name");
                String stu_id = user.get("stu_id");
                String title = inputTitle.getText().toString().trim();
                String contents = content.getText().toString().trim();
                String see = "0";

                if (!title.isEmpty() && !contents.isEmpty()) {
                    writeBoard(category, name, stu_id, title, contents,see);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "제목,내용을 모두 입력해 주세요", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Link to Login Screen
        btnCancel.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        MenuActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
    private void writeBoard(final String category, final String name, final String stu_id, final String title,
                            final String content, final String see) {
        String tag_string_req = "글쓰기 요청중";

        pDialog.setMessage("쓰는 중 ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_BOARDWRITE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(getApplicationContext(), "글 등록이 완료 되었습니다.", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(
                                WriteActivity.this,
                                MenuActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // 글쓰기 중 에러 났을 경우
                        // message
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
                Log.e(TAG, "Write Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() { //서버로 값 보내는 부분
                // 파라미터 전달
                Map<String, String> params = new HashMap<String, String>();
                params.put("category", category);
                params.put("name", name);
                params.put("stu_id", stu_id);
                params.put("title", title);
                params.put("content", content);
                params.put("see",see);

                return params;
            }

        };
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