package com.example.user.gachonhelper.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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
 * Created by wangki on 2016-05-29.
 */
public class CategoryActivity extends Activity {
    private static final String TAG = CategoryActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private JSONArray result;


    private ArrayList<String> _category = new ArrayList<String>();
    private ArrayList<String> _title = new ArrayList<String>();
    private ArrayList<String> _writer = new ArrayList<String>();
    private ArrayList<String> _stu_id = new ArrayList<String>();
    private ArrayList<String> _content = new ArrayList<String>();
    private ArrayList<String> _create = new ArrayList<String>();
    private ArrayList<String> _see = new ArrayList<String>();
    ImageButton IT,ENGINEER,MANAGEMENT,SOCIAL_SCIENCE,LAW,HUMANITIES,BIO,ORIENTAL_MEDICINE,ART,LIBERAL_ART,OTHER;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_category);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        IT = (ImageButton)findViewById(R.id.it);
        ENGINEER = (ImageButton)findViewById(R.id.engineer);
        MANAGEMENT = (ImageButton)findViewById(R.id.management);
        SOCIAL_SCIENCE = (ImageButton)findViewById(R.id.socialScience);
        LAW = (ImageButton)findViewById(R.id.law);
        HUMANITIES = (ImageButton)findViewById(R.id.humanities);
        BIO = (ImageButton)findViewById(R.id.bio);
        ORIENTAL_MEDICINE = (ImageButton)findViewById(R.id.oriental_medicine);
        ART = (ImageButton)findViewById(R.id.art);
        LIBERAL_ART = (ImageButton)findViewById(R.id.liberal_arts);
        OTHER = (ImageButton)findViewById(R.id.other);

        IT.setOnTouchListener(this.btnTouch);
        ENGINEER.setOnTouchListener(this.btnTouch);
        MANAGEMENT.setOnTouchListener(this.btnTouch);
        SOCIAL_SCIENCE.setOnTouchListener(this.btnTouch);
        LAW.setOnTouchListener(this.btnTouch);
        HUMANITIES.setOnTouchListener(this.btnTouch);
        BIO.setOnTouchListener(this.btnTouch);
        ORIENTAL_MEDICINE.setOnTouchListener(this.btnTouch);
        ART.setOnTouchListener(this.btnTouch);
        LIBERAL_ART.setOnTouchListener(this.btnTouch);
        OTHER.setOnTouchListener(this.btnTouch);

    }

    protected View.OnTouchListener btnTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.getBackground().setColorFilter(0xaa111111, PorterDuff.Mode.SRC_OVER);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.getBackground().clearColorFilter();
                if(v.getId() == R.id.it){
                    getTextList("IT");
                }else if(v.getId() == R.id.engineer){
                    getTextList("ENGINEER");
                }else if(v.getId() == R.id.management){
                    getTextList("MANAGEMENT");
                }else if(v.getId() == R.id.socialScience){
                    getTextList("SOCIAL_SCIENCE");
                }else if(v.getId() == R.id.law){
                    getTextList("LAW");
                }else if(v.getId() == R.id.humanities){
                    getTextList("HUMANITIES");
                }else if(v.getId() == R.id.bio){
                    getTextList("BIO");
                }else if(v.getId() == R.id.oriental_medicine){
                    getTextList("ORIENTAL_MEDICINE");
                }else if(v.getId() == R.id.art){
                    getTextList("ART");
                }else if(v.getId() == R.id.liberal_arts){
                    getTextList("LIBERAL_ARTS");
                }else if(v.getId() == R.id.other){
                    getTextList("OTHER");
                }
            }
            return true;
        }
    };
    private void getTextList(final String category) {
        String tag_string_req = "req_get_text_list";

        pDialog.setMessage("목록을 불러오는 중입니다 ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GETTEXT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "READ Response: " + response);
                hideDialog();
                JSONObject jObject = null;
                try {
                    //JSON 오브젝트를 JSON스트링형식으로 파싱
                    jObject = new JSONObject(response);
                    result = jObject.getJSONArray("result");

                    //Traversing through all the items in the json array
                    for(int i=0;i<result.length();i++) {
                        try {
                            //JOSN 오브젝트 가져오기
                            JSONObject json = result.getJSONObject(i); //result[0],[1],[2]에 있는 오브젝트를 json에 복사
                            _category.add(json.getString("category"));
                            _title.add(json.getString("title")); //arrayList에 타이틀들을 계속 저장해나감
                            _writer.add(json.getString("name"));
                            _stu_id.add(json.getString("stu_id"));
                            _content.add(json.getString("content"));
                            _create.add(json.getString("created_at"));
                            _see.add(json.getString("see"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    Intent i = new Intent(getApplicationContext(),TextListActivity.class);
                    i.putStringArrayListExtra("category",_category);
                    i.putStringArrayListExtra("title",_title);
                    i.putStringArrayListExtra("writer",_writer);
                    i.putStringArrayListExtra("stu_id",_stu_id);
                    i.putStringArrayListExtra("created_at",_create);
                    i.putStringArrayListExtra("see",_see);
                    i.putStringArrayListExtra("content",_content);
                    i.putExtra("layoutCategory",category);
                    startActivity(i);
                    finish();


                }catch (JSONException e) {
                    // JSON 에러
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "READ Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {         //POST로 category 변수 전달
                // 파라미터 전송
                Map<String, String> params = new HashMap<String, String>();

                params.put("category", category);

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