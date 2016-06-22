package com.example.user.gachonhelper.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangki on 2016-05-30.
 */
public class ReadTextActivity extends Activity {
    private static final String TAG = ReadTextActivity.class.getSimpleName();
    TextView _category,_title,_name_id,_created,_see,_content;
    Button confirm, help;
    String intent_category = new String();
    final Context context = this;
    private ProgressDialog pDialog;
    private SQLiteHandler db;
    String category,title,name,stu_id,created_at,see,content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_text);

        Intent i = getIntent();

        category = i.getExtras().getString("category");
        title = i.getExtras().getString("title");
        name = i.getExtras().getString("name");
        stu_id = i.getExtras().getString("stu_id");
        created_at = i.getExtras().getString("created_at");
        see = i.getExtras().getString("see");
        content = i.getExtras().getString("content");

        _title = (TextView)findViewById(R.id.title);
        _name_id = (TextView)findViewById(R.id.name_stu_id);
        _created = (TextView)findViewById(R.id.created_at);
        _see = (TextView)findViewById(R.id.see);
        _content = (TextView)findViewById(R.id.textarea);
        _category = (TextView)findViewById(R.id.read_category);
        confirm = (Button)findViewById(R.id.confirm);
        help = (Button)findViewById(R.id.submit);

        _title.setText(title);
        _name_id.setText(name + "(" + stu_id + ")");
        _created.setText(created_at);
        _see.setText(see);
        _content.setText(content);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        db = new SQLiteHandler(getApplicationContext());

        switch(category)
        {
            case "IT":
                _category.setText("IT대학");
                intent_category = "IT";
                break;
            case "ENGINEER":
                _category.setText("공과대학");
                intent_category = "ENGINEER";
                break;
            case "MANAGEMENT":
                _category.setText("경영대학");
                intent_category = "MANAGEMENT";
                break;
            case "SOCIAL_SCIENCE":
                _category.setText("사회과학대학");
                intent_category = "SOCIAL_SCIENCE";
                break;
            case "LAW":
                _category.setText("법과대학");
                intent_category = "LAW";
                break;
            case "HUMANITIES":
                _category.setText("인문대학");
                intent_category = "HUMANITIES";
                break;
            case "BIO":
                _category.setText("바이오나노대학");
                intent_category = "BIO";
                break;
            case "ORIENTAL_MEDICINE":
                _category.setText("한의과대학");
                intent_category = "ORIENTAL_MEDICINE";
                break;
            case "ART":
                _category.setText("예술대학");
                intent_category = "ART";
                break;
            case "LIBERAL_ART":
                _category.setText("가천리버럴아츠칼리지");
                intent_category = "LIBERAL_ART";
                break;
            case "OTHER":
                _category.setText("기타");
                intent_category = "OTHER";
                break;
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setIcon(R.drawable.dialog_helper);
                alert.setTitle("Gachon Helper");
                alert.setMessage("핸드폰 번호를 입력해 주세요. 그래야 헬피에게 연락을 받을 수 있어요 >.<");

                final EditText phoneNum = new EditText(context);

                phoneNum.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(phoneNum);


                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String phone = phoneNum.getEditableText().toString(); //폰번호 받아오고
                        HashMap<String, String> user = db.getUserDetails();

                        String helpy_stu_id = stu_id; //헬피 학번
                        String helper_name = user.get("name"); //헬퍼 이름
                        String helper_stu_id = user.get("stu_id"); //헬퍼 학번
                        String helper_category = user.get("category"); //헬퍼 단대
                        String helper_tickets = user.get("tickets");
                        String helper_phone = phone; //헬퍼 번호
                        String title_ = title; //글제목

                        if (helper_name.equals(name)) {
                            Toast.makeText(getApplicationContext(), "자기 자신에게 도움을 줄 수 없습니다", Toast.LENGTH_SHORT).show();
                        } else {
                            connectUser(helpy_stu_id, helper_name, helper_stu_id, helper_category,
                                    helper_tickets, helper_phone, title_);
                        }
                    }
                });

                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        });

    }
    private void connectUser(final String helpy_stu_id, final String helper_name, final String helper_stu_id, final String helper_category,
                             final String helper_tickets, final String helper_phone, final String title_) {
        // Tag used to cancel the request
        String tag_string_req = "req_connect";

        pDialog.setMessage("Connecting ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_CONNECTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Connect Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {

                        Toast.makeText(getApplicationContext(), "도움 신청이 완료되었습니다.", Toast.LENGTH_LONG).show();
                        push();

                    } else {
                        //도중에 에러났을 때,
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
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() { //서버로 값 보내는 부분
                // 파라미터 전달
                Map<String, String> params = new HashMap<String, String>();
                params.put("helpy_stu_id", helpy_stu_id);
                params.put("helper_name", helper_name);
                params.put("helper_stu_id", helper_stu_id);
                params.put("helper_category", helper_category);
                params.put("helper_tickets", helper_tickets);
                params.put("helper_phone",helper_phone);
                params.put("title", title_);

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


    private void push() {
        String tag_string_req = "push";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_PUSH, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "GCM Response: " + response.toString());

                finish();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // 파라미터 전달
                Map<String, String> params = new HashMap<String, String>();
                params.put("helpy_stu_id",stu_id);
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
