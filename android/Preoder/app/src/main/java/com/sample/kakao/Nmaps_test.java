package com.sample.kakao;

import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonIOException;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.util.FusedLocationSource;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class Nmaps_test extends AppCompatActivity {
    public static class ApiSearchLocal {
        public static String getjsontext(String arg) {
            String clientId = "7k0MJ5PlUscGVH5x3Aiv"; //애플리케이션 클라이언트 아이디값"
            String clientSecret = "cxng5Nn0Jc"; //애플리케이션 클라이언트 시크릿값"
            String text = null;
            try {
                text = URLEncoder.encode(arg, "UTF-8");
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

    private MapView mapView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE =1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private Geocoder geocoder;
    private String json_str;

    Button near_list_btn,chk_btn;
    TextView tv,tv2;
    EditText editText;
    Double my_latitude,my_longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nmaps_test);
        chk_btn = (Button)findViewById(R.id.chk_btn);
        near_list_btn = findViewById(R.id.near_list_btn);
        tv = findViewById(R.id.tv);
        tv2 = findViewById(R.id.tv2);
        editText = findViewById(R.id.editText);

        mapView = (MapView)findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this::OnMapReady);



        //지도 사용권한 받기
        locationSource = new FusedLocationSource(this,LOCATION_PERMISSION_REQUEST_CODE);
        chk_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getTitle;
                String titleFilter;
                String title;
                String category;
                String address;
                String roadAddress;
                String mapx,mapy;
                try {
                    JSONObject jsonObject = new JSONObject(json_str);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        getTitle =  jsonObject1.getString("title");
                        titleFilter = getTitle.replaceAll("<b>","");
                        title = titleFilter.replaceAll("</b>","");

                        category = jsonObject1.getString("category");
                        address = jsonObject1.getString("adress");
                        roadAddress = jsonObject1.getString("roadAddress");
                        mapx = jsonObject1.getString("mapx");
                        mapy = jsonObject1.getString("mapy");
                        tv.setText(mapx + " , " + mapy);
                        tv2.setText(title + "," + category + " , " + address);
                    }
                }catch (JsonIOException | JSONException | NullPointerException e){
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        //권한획득 여부 확인
        if(locationSource.onRequestPermissionsResult(requestCode,permissions,grantResults))
        {
            if(!locationSource.isActivated()){
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public void OnMapReady(@NonNull NaverMap naverMap){
        this.naverMap = naverMap;
        geocoder = new Geocoder(this);

        naverMap.setLocationSource(locationSource);
        //위치 추적모드 내위치로 이동 카메라도 자기쪽으로
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        naverMap.setLayerGroupEnabled(naverMap.LAYER_GROUP_BUILDING,true);

        //LatLng init_Position = new LatLng(35.106281,128.9661773);//하단역 2번출구

        //자신의 위치 저장
        //LatLng my_position = new LatLng(my_latitude,my_longitude);

        //자신 위치로 카메라 이동
        //CameraUpdate cu = CameraUpdate.scrollTo(init_Position);
        //naverMap.moveCamera(cu);    //초기위치 하단역

        //NaverMap 객체 받아서 NaverMap객체에 위치 소스 지정
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setCompassEnabled(true);     //나침반
        uiSettings.setScaleBarEnabled(true);    //거리
        uiSettings.setZoomControlEnabled(true); //zoom
        uiSettings.setLocationButtonEnabled(true);//현재위치 버튼

        near_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                Thread thread = new Thread(){
                    public void run(){
                        URL_test.ApiSearchLocal api = new URL_test.ApiSearchLocal();
                        json_str = api.getjsontext(text);
                    }
                };
                thread.start();
                tv.setText(json_str);
            }
        });
        //지도상에 마커찍기
        //Marker maker = new Marker();
        //maker.setPosition(init_Position);
        //maker.setMap(naverMap);
    }



}