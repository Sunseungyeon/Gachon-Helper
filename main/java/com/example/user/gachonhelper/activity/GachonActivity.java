package com.example.user.gachonhelper.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;


import com.example.user.gachonhelper.R;

/**
 * Created by wangki on 2016-05-30.
 */
public class GachonActivity extends Activity {
    ImageButton gachonHP, gachonHelper, gachonBamboo, gachonCouncil, gachonDeliver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gachon);

        gachonHP = (ImageButton)findViewById(R.id.gachonHP);
        gachonHelper = (ImageButton)findViewById(R.id.gachonHelper);
        gachonBamboo = (ImageButton)findViewById(R.id.gachonBamboo);
        gachonCouncil = (ImageButton)findViewById(R.id.gachonCouncil);
        gachonDeliver = (ImageButton)findViewById(R.id.gachonDeliver);

        // 가천대 홈페이지
        gachonHP.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gachonHP.getBackground().setColorFilter(0xaa111111, PorterDuff.Mode.SRC_OVER);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    gachonHP.getBackground().clearColorFilter();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.gachon.ac.kr"));
                    startActivity(intent);
                }
                return true;
            }
        });
        // 가천대 헬퍼 공식 블로그
        gachonHelper.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gachonHelper.getBackground().setColorFilter(0xaa111111, PorterDuff.Mode.SRC_OVER);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    gachonHelper.getBackground().clearColorFilter();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://blog.naver.com/gachon_helper"));
                    startActivity(intent);
                }
                return true;
            }
        });
        // 가천대 대나무숲 페이스북
        gachonBamboo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gachonBamboo.getBackground().setColorFilter(0xaa111111, PorterDuff.Mode.SRC_OVER);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    gachonBamboo.getBackground().clearColorFilter();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/gcubamboo"));
                    startActivity(intent);
                }
                return true;
            }
        });
        // 가천대 총학생회 페이스북
        gachonCouncil.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gachonCouncil.getBackground().setColorFilter(0xaa111111, PorterDuff.Mode.SRC_OVER);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    gachonCouncil.getBackground().clearColorFilter();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/gachongeneralstudentcouncil"));
                    startActivity(intent);
                    ;
                }
                return true;
            }
        });
        // 가천대 대신전해드립니다 페이스북
        gachonDeliver.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    gachonDeliver.getBackground().setColorFilter(0xaa111111, PorterDuff.Mode.SRC_OVER);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    gachonDeliver.getBackground().clearColorFilter();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/가천대학교-대신전해드립니다-242316859294488/?ref=py_c"));
                    startActivity(intent);
                    ;
                }
                return true;
            }
        });
    }
}