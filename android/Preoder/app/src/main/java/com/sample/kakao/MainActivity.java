package com.sample.kakao;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.kakao.usermgmt.LoginButton;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

public class MainActivity extends AppCompatActivity {
    private Button mKakaoLoginBtn,mNaverLoginBtn,mGoogleLoginBtn,pass_btn2,menu_rec_btn;
    private LoginButton mKakaoLoginBtnBasic;
    private OAuthLoginButton mNaverLoginBtnBasic;
//    private KakaoLogin.KakaoSessionCallback sessionCallback;
    private OAuthLogin mNaverLoginModule;
    private NaverLogin mNaverLoginAuth;
    final String NAVER_CLIENT_ID="N0t8KpY5XGYQ8hPZj1cK";
    final String NAVER_CLIENT_SECRET="NfqCtzLZed";
    private FirebaseAuth mGoogleLoginModule;


    @SuppressLint("HandlerLeak")

    private OAuthLoginHandler mNaverLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                Intent intent = new Intent(MainActivity.this, NaverLogin.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(getApplicationContext(), "Naver Login Failed!", Toast.LENGTH_LONG);
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        final ImageButton menu_rec_btn = findViewById(R.id.menu_recommend_btn);
        final ImageButton res_search_btn = findViewById(R.id.restaurant_search_btn);


        res_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Nmaps_test.class);
                startActivity(intent);
                finish();
            }
        }); //네이버 지도로

        menu_rec_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Menu_recommend.class);
                startActivity(intent);
                finish();

            }
        }); //메뉴 추천받기

    }//onCreate문
}
