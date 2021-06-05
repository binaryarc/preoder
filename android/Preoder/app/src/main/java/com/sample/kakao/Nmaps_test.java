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

import java.util.ArrayList;

public class Nmaps_test extends AppCompatActivity {


    private MapView mapView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE =1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private Geocoder geocoder;
    private String json_str;
    private MyAsyncTask MA = new MyAsyncTask();

    private ArrayList<String>R_nameList;
    private ArrayList<String>R_locationList;

    public static String total_List = null;
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

        R_nameList = new ArrayList<>();
        R_locationList = new ArrayList<>();

        //지도 사용권한 받기
        locationSource = new FusedLocationSource(this,LOCATION_PERMISSION_REQUEST_CODE);

        chk_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread(){
                    public void run(){
                        MA.onPreExecute();
                        MA.set_input(editText.getText().toString());
                        MA.doInBackground();

                        json_str = MA.get_json();
                        tv.setText(json_str);

                        String getTitle;
                        String titleFilter;
                        String title=null;
                        String category;
                        String address;
                        String roadAddress;
                        String mapx,mapy;

                        try {
                            JSONObject jsonObject = new JSONObject(json_str);
                            JSONArray jsonArray = jsonObject.getJSONArray("items");
                            tv.setText(jsonArray.toString());
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                getTitle =  jsonObject1.getString("title");
                                titleFilter = getTitle.replaceAll("<b>","");
                                title = titleFilter.replaceAll("</b>","") +", ";
                                category = jsonObject1.getString("category");
                                address = jsonObject1.getString("adress");
                                roadAddress = jsonObject1.getString("roadAddress");
                                mapx = jsonObject1.getString("mapx");
                                mapy = jsonObject1.getString("mapy");
                                R_nameList.add(title);
                                R_locationList.add(address);
                                tv2.setText(R_nameList.toString());
                            }

                        }catch (JsonIOException | JSONException | NullPointerException e){
                            e.printStackTrace();
                        }
                        //tv2.setText(total_List);
                        //tv2.setText(R_nameList.toString() +"\n" + R_locationList.toString() +"\n");
                    }
                };
                thread.start();
                tv2.setText(R_nameList.toString() +"\n" + R_locationList.toString() +"\n");
                MA.onPostExecute(null);
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

        //지도상에 마커찍기
        //Marker maker = new Marker();
        //maker.setPosition(init_Position);
        //maker.setMap(naverMap);
    }



}