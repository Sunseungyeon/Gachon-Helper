package com.example.user.gachonhelper.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.gachonhelper.R;
import com.example.user.gachonhelper.app.AppConfig;
import com.example.user.gachonhelper.app.AppController;
import com.example.user.gachonhelper.helper.ListViewAdapter2;
import com.example.user.gachonhelper.helper.SQLiteHandler;
import com.example.user.gachonhelper.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AlarmCheckActivity extends Activity {
    private static final String TAG = AlarmCheckActivity.class.getSimpleName();
    final Context context = this;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private JSONArray helpers;
    private String helpy_stu_id;
    private String helpy_ticket;
    private String save_title;
    private int count;
    String[] items;

    private ArrayList<String> helper_name = new ArrayList<String>();
    private ArrayList<String> helper_stu_id = new ArrayList<String>();
    private ArrayList<String> title = new ArrayList<String>();
    private ArrayList<String> helper_category = new ArrayList<String>();
    private ArrayList<String> helper_tickets = new ArrayList<String>();
    private ArrayList<String> helper_phone = new ArrayList<String>();

    private ArrayList<String> helper_name2 = new ArrayList<String>();
    private ArrayList<String> helper_stu_id2 = new ArrayList<String>();
    private ArrayList<String> title2 = new ArrayList<String>();
    private ArrayList<String> helper_category2 = new ArrayList<String>();
    private ArrayList<String> helper_tickets2 = new ArrayList<String>();
    private ArrayList<String> helper_phone2 = new ArrayList<String>();

    private ListView listview;
    private ListViewAdapter2 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_check);

        helper_name = getIntent().getExtras().getStringArrayList("helper_name");
        helper_stu_id = getIntent().getExtras().getStringArrayList("helper_stu_id");
        title = getIntent().getExtras().getStringArrayList("title");
        helper_category = getIntent().getExtras().getStringArrayList("helper_category");
        helper_tickets = getIntent().getExtras().getStringArrayList("helper_tickets");
        helper_phone = getIntent().getExtras().getStringArrayList("helper_phone");

        adapter = new ListViewAdapter2();

        listview = (ListView) findViewById(R.id.alarm_listview);
        listview.setAdapter(adapter);

        for (int i = 0; i < title.size(); i++) {
            adapter.addItem(title.get(i).toString(), "");
        }

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        helpy_stu_id = user.get("stu_id"); //helpy stu_id
        helpy_ticket = user.get("tickets");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] items = new String[count];

                save_title = title.get(position);// 선택된 제목 저장

                getHelper(save_title, helpy_stu_id);
            }
        });
    }

    public void sendSMS(String smsNum, String smsTextContext) {

        if (smsNum.length() > 0 && smsTextContext.length() > 0) {
            //sendSMS(smsNum, smsText);
            PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT_ACTION"), 0);
            PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED_ACTION"), 0);

            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            // 전송 성공
                            Toast.makeText(getApplicationContext(), "전송 완료", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            // 전송 실패
                            Toast.makeText(getApplicationContext(), "전송 실패", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            // 서비스 지역 아님
                            Toast.makeText(getApplicationContext(), "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            // 무선 꺼짐
                            Toast.makeText(getApplicationContext(), "무선(Radio)가 꺼져있습니다", Toast.LENGTH_SHORT).show();
                            break;
                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            // PDU 실패
                            Toast.makeText(getApplicationContext(), "PDU Null", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter("SMS_SENT_ACTION"));

            registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            // 도착 완료
                            Toast.makeText(getApplicationContext(), "SMS 도착 완료", Toast.LENGTH_SHORT).show();
                            break;
                        case Activity.RESULT_CANCELED:
                            // 도착 안됨
                            Toast.makeText(getApplicationContext(), "SMS 도착 실패", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }, new IntentFilter("SMS_DELIVERED_ACTION"));

            SmsManager mSmsManager = SmsManager.getDefault();
            mSmsManager.sendTextMessage(smsNum, null, smsTextContext, sentIntent, deliveredIntent);

        } else {
            Toast.makeText(this, "모두 입력해 주세요", Toast.LENGTH_SHORT).show();
        }
    }


    private void getHelper(final String title_, final String helpy_stu_id) {
        String tag_string_req = "req_get_helper_list";

        pDialog.setMessage("목록을 불러오는 중입니다 ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_HELPERS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Helpers Response: " + response);
                hideDialog();
                JSONObject jObject = null;
                try {
                    //Parsing the fetched Json String to JSON Object
                    jObject = new JSONObject(response);
                    //Storing the Array of JSON String to our JSON Array
                    helpers = jObject.getJSONArray("result");

                    //Traversing through all the items in the json array
                    for (int i = 0; i < helpers.length(); i++) {
                        try {
                            //Getting json object
                            JSONObject json = helpers.getJSONObject(i); //result[0],[1],[2]에 있는 오브젝트를 json에 복사
                            helper_name2.add(json.getString("helper_name"));
                            helper_stu_id2.add(json.getString("helper_stu_id")); //arrayList에 타이틀들을 계속 저장해나감
                            helper_category2.add(json.getString("helper_category"));
                            helper_tickets2.add(json.getString("helper_tickets"));
                            helper_phone2.add(json.getString("helper_phone"));
                            title2.add(json.getString("title"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    count = helpers.length();
                    items = new String[count];


                    for (int i = 0; i < count; i++) {
                        String tmpCategory ="";
                        switch(helper_category2.get(i))
                        {
                            case "IT":
                                tmpCategory = "IT대학";
                                break;
                            case "ENGINEER":
                                tmpCategory = "공과대학";
                                break;
                            case "MANAGEMENT":
                                tmpCategory = "경영대학";
                                break;
                            case "SOCIAL_SCIENCE":
                                tmpCategory = "사회과학대학";
                                break;
                            case "LAW":
                                tmpCategory = "법과대학";
                                break;
                            case "HUMANITIES":
                                tmpCategory = "인문대학";
                                break;
                            case "BIO":
                                tmpCategory = "바이오나노대학";
                                break;
                            case "ORIENTAL_MEDICINE":
                                tmpCategory = "한의과대학";
                                break;
                            case "ART":
                                tmpCategory = "예술대학";
                                break;
                            case "LIBERAL_ART":
                                tmpCategory = "가천리버럴아츠칼리지";
                                break;
                            case "OTHER":
                                tmpCategory = "기타";
                                break;
                        }
                        items[i] ="헬퍼 : " + helper_name2.get(i) + ", 단과대학 : " + tmpCategory + ", 티켓 수 : " + helper_tickets2.get(i);
                    }

                    //다이얼 로그
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setIcon(R.drawable.dialog_helper);
                    alert.setTitle("Helper List");


                    alert.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {

                            final int position = item;
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setIcon(R.drawable.dialog_helper);
                            alert.setTitle("헬퍼 선택하기");
                            alert.setMessage("헬퍼 선택을 하시겠습니까? (한번 선택하면 취소할 수 없으니 신중하게 선택해주세요)");


                            final String phoneNumber;
                            final String helperId;
                            final String sendTitle;
                            helperId = helper_stu_id2.get(position);
                            phoneNumber = helper_phone2.get(position);
                            sendTitle = title2.get(position);
                            Log.d("폰번호", phoneNumber);


                            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    // 헬퍼 티켓 수 추가 & 헬피 티켓 수 감소 시켜주기
                                    if (helpy_ticket.equals("0")) {
                                        Toast.makeText(getApplicationContext(), "티켓이 부족합니다. 헬피에게 도움을 주세요!!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        //티켓 교환
                                        StringRequest strReq = new StringRequest(Request.Method.POST,
                                                AppConfig.URL_TICKETS, new Response.Listener<String>() {

                                            @Override
                                            public void onResponse(String response) {
                                                Log.d(TAG, "tickets Response: " + response);

                                                try {
                                                    JSONObject jObj = new JSONObject(response);
                                                    String tmpTickets = jObj.getString("tickets"); //sqlite 업뎃 위해 티켓 수 받음


                                                    // sqlite DB수정
                                                    db = new SQLiteHandler(getApplicationContext());
                                                    HashMap<String, String> user = db.getUserDetails();
                                                    String tmpName = user.get("name");  // Name
                                                    String tmpStu_id = user.get("stu_id"); // Student ID
                                                    String tmpUid = user.get("uid");
                                                    String tmpCategory = user.get("category"); // category
                                                    String tmpCreated_at = user.get("created_at"); // Created At
                                                    db.deleteUsers();
                                                    db.addUser(tmpName, tmpStu_id, tmpUid, tmpCategory, tmpTickets, tmpCreated_at);

                                                    //헬퍼에게 SMS 전송
                                                    sendSMS(phoneNumber, "당신은  '" + tmpStu_id +"'님의 " + "헬퍼로 선택되셨습니다.");
                                                    Toast.makeText(getApplicationContext(), "도움 문자 전송 완료!", Toast.LENGTH_SHORT).show();

                                                    finish();
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

                                            }
                                        }) {

                                            @Override
                                            protected Map<String, String> getParams() {         //POST로 category 변수 전달
                                                // 파라미터 전달
                                                Map<String, String> params = new HashMap<String, String>();

                                                params.put("helpy_stu_id", helpy_stu_id);
                                                params.put("helper_stu_id", helperId);
                                                params.put("title", sendTitle);

                                                return params;
                                            }

                                        };
                                        // Adding request to request queue
                                        AppController.getInstance().addToRequestQueue(strReq, "Check");

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
                    AlertDialog build = alert.create();
                    build.show();


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {         //POST로 category 변수 전달
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("title", title_);
                params.put("helpy_stu_id", helpy_stu_id);


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
