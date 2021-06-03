package com.sample.kakao;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class URL_test extends AppCompatActivity {

    public static class ApiSearchLocal {
        public static String getjsontext(String arg) {
            String clientId = "7k0MJ5PlUscGVH5x3Aiv"; //애플리케이션 클라이언트 아이디값"
            String clientSecret = "cxng5Nn0Jc"; //애플리케이션 클라이언트 시크릿값"
            String text = null;
            try {
                text = URLEncoder.encode("하단 맥도날드", "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("검색어 인코딩 실패",e);
            }

            String apiURL = "https://openapi.naver.com/v1/search/local?query=" + text +"&display=10";    // json 결과
            //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과


            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("X-Naver-Client-Id", clientId);
            requestHeaders.put("X-Naver-Client-Secret", clientSecret);
            String responseBody = get(apiURL,requestHeaders);
            System.out.println(responseBody);
            return responseBody;
        }
        private static String get(String apiUrl, Map<String, String> requestHeaders){
            HttpURLConnection con = connect(apiUrl);
            try {
                con.setRequestMethod("GET");
                for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                    con.setRequestProperty(header.getKey(), header.getValue());
                }


                int responseCode = con.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                    return readBody(con.getInputStream());
                } else { // 에러 발생
                    return readBody(con.getErrorStream());
                }
            } catch (IOException e) {
                throw new RuntimeException("API 요청과 응답 실패", e);
            } finally {
                con.disconnect();
            }
        }
        private static HttpURLConnection connect(String apiUrl){
            try {
                URL url = new URL(apiUrl);
                return (HttpURLConnection)url.openConnection();
            } catch (MalformedURLException e) {
                throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
            } catch (IOException e) {
                throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
            }
        }

        private static String readBody(InputStream body){
            InputStreamReader streamReader = new InputStreamReader(body);


            try (BufferedReader lineReader = new BufferedReader(streamReader)) {
                StringBuilder responseBody = new StringBuilder();


                String line;
                while ((line = lineReader.readLine()) != null) {
                    responseBody.append(line);
                }


                return responseBody.toString();
            } catch (IOException e) {
                throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
            }
        }
    }
    TextView tv1;
    Button btn,chk_btn;
    private String json_str;
    private String search_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url_test);
        tv1 = (TextView)findViewById(R.id.tv1);
        btn = (Button)findViewById(R.id.btn);
        chk_btn = (Button)findViewById(R.id.chk_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(){
                    public void run(){
                        ApiSearchLocal api = new ApiSearchLocal();
                        json_str = api.getjsontext(search_text);
                    }
                };
                thread.start();


                tv1.setText(json_str);




            }
        });

    }


}