package com.example.user.gachonhelper.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.gachonhelper.R;
import com.example.user.gachonhelper.app.AppConfig;
import com.example.user.gachonhelper.app.AppController;
import com.example.user.gachonhelper.helper.ListViewAdapter;
import com.example.user.gachonhelper.helper.SessionManager;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangki on 2016-05-27.
 */
public class TextListActivity extends Activity {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private SessionManager session;
    private String category_;
    private JSONArray result;

    private ArrayList<String> _category = new ArrayList<String>();
    private ArrayList<String> _title = new ArrayList<String>();
    private ArrayList<String> _writer = new ArrayList<String>();
    private ArrayList<String> _stu_id = new ArrayList<String>();
    private ArrayList<String> _content = new ArrayList<String>();
    private ArrayList<String> _create = new ArrayList<String>();
    private ArrayList<String> _see = new ArrayList<String>();

    private ListView listview;
    private ListViewAdapter adapter;
    TextView layout_category;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_list);

        layout_category = (TextView) findViewById(R.id.category);

        _category = getIntent().getExtras().getStringArrayList("category");
        _title = getIntent().getExtras().getStringArrayList("title");
        _writer = getIntent().getExtras().getStringArrayList("writer");
        _stu_id = getIntent().getExtras().getStringArrayList("stu_id");
        _create = getIntent().getExtras().getStringArrayList("created_at");
        _see = getIntent().getExtras().getStringArrayList("see");
        _content = getIntent().getExtras().getStringArrayList("content");
        category_ = getIntent().getExtras().getString("layoutCategory");

        //커스텀 리스트 뷰 아답터 설정
        adapter = new ListViewAdapter();
        listview = (ListView) findViewById(R.id.mylistview);
        listview.setAdapter(adapter);

        switch(category_)
        {
            case "IT":
                layout_category.setText("IT대학");
                break;
            case "ENGINEER":
                layout_category.setText("공과대학");
                break;
            case "MANAGEMENT":
                layout_category.setText("경영대학");
                break;
            case "SOCIAL_SCIENCE":
                layout_category.setText("사회과학대학");
                break;
            case "LAW":
                layout_category.setText("법과대학");
                break;
            case "HUMANITIES":
                layout_category.setText("인문대학");
                break;
            case "BIO":
                layout_category.setText("바이오나노대학");
                break;
            case "ORIENTAL_MEDICINE":
                layout_category.setText("한의과대학");
                break;
            case "ART":
                layout_category.setText("예술대학");
                break;
            case "LIBERAL_ART":
                layout_category.setText("가천리버럴아츠칼리지");
                break;
            case "OTHER":
                layout_category.setText("기타");
                break;
        }

        for (int i = 0; i < _title.size(); i++) {
            adapter.addItem(_title.get(i).toString(), _writer.get(i).toString(), _create.get(i).toString(), _see.get(i).toString());
        }

        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            // 로그인 안되있을경우. 로그인 액티비티로 보내기
            Intent intent = new Intent(TextListActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                int SEE = Integer.parseInt(_see.get(position).toString()) + 1;

                Intent i = new Intent(getApplicationContext(), ReadTextActivity.class);
                i.putExtra("title", _title.get(position).toString());
                i.putExtra("name", _writer.get(position).toString());
                i.putExtra("stu_id", _stu_id.get(position).toString());
                i.putExtra("created_at", _create.get(position).toString());
                i.putExtra("see", String.valueOf(SEE));
                i.putExtra("content", _content.get(position).toString());
                i.putExtra("category", _category.get(position).toString());

                //조회수 올려주기.
                String tag_string_req = "Increasing see";
                final int tmpPosition = position;
                StringRequest strReq = new StringRequest(Request.Method.POST,
                        AppConfig.URL_SEE, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
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
                        params.put("stu_id", _stu_id.get(tmpPosition).toString());
                        params.put("title", _title.get(tmpPosition).toString());
                        return params;
                    }
                };

                // Adding request to request queue
                AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

                //해당 글 액티비티 실행
                startActivity(i);

            }
        });

    }
}