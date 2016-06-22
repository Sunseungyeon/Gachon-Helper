package com.example.user.gachonhelper.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.user.gachonhelper.R;

/**
 * Created by wangki on 2016-05-23.
 */
public class SplashActivity extends Activity {
    private static String TAG = SplashActivity.class.getName();
    private static long SLEEP_TIME = 2; // Sleep for some time
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// 타이틀바 제거
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
// notification바 제거
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.loading);
// 타이머 설정 및 런처 실행
        IntentLauncher launcher = new IntentLauncher();
        launcher.start();
    }
    private class IntentLauncher extends Thread {
        @Override
/**
 * 몇초 쉰 후 , 새로운 액티비티 실행
 */
        public void run() {
            try {
                Thread.sleep(SLEEP_TIME*1000);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            Intent intent = new Intent(SplashActivity.this,
                    LoginActivity.class);
            SplashActivity.this.startActivity(intent);
            SplashActivity.this.finish();
        }
    }
}
