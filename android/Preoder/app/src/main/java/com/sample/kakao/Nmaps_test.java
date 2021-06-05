package com.sample.kakao;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private Geocoder geocoder ;
    private String json_str;
    private MyAsyncTask MA = new MyAsyncTask();
    private MyAsyncTask2 MA2 = new MyAsyncTask2();
    private ArrayList<String>R_nameList;
    private ArrayList<String>R_locationList;
    private ArrayList<Double>R_latitudeList;
    private ArrayList<Double>R_longitudeList;

    String my_location_str;
    Button near_list_btn,get_json_btn,parse_btn;
    TextView tv,tv2,tv3;
    EditText editText;
    Double my_latitude,my_longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nmaps_test);
        get_json_btn = (Button)findViewById(R.id.get_json_btn);
        near_list_btn = findViewById(R.id.near_list_btn);
        tv = findViewById(R.id.tv);
        tv2 = findViewById(R.id.tv2);
        tv3= findViewById(R.id.tv3);
        editText = findViewById(R.id.editText);
        parse_btn = findViewById(R.id.parse_btn);
        mapView = (MapView)findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this::OnMapReady);

        R_nameList = new ArrayList<>();
        R_locationList = new ArrayList<>();
        R_latitudeList = new ArrayList<>();
        R_longitudeList = new ArrayList<>();
        //지도 사용권한 받기
        locationSource = new FusedLocationSource(this,LOCATION_PERMISSION_REQUEST_CODE);
        geocoder = new Geocoder(mapView.getContext());
        get_json_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                my_latitude = locationSource.getLastLocation().getLatitude();
                my_longitude = locationSource.getLastLocation().getLongitude();
                //tv2.setText(my_latitude.toString()+"\n"+my_longitude.toString());
                //자신의 위치 저장
                try {
                    my_location_str = geocoder.getFromLocation(my_latitude,my_longitude,1).toString();
                    //tv.setText(my_location_str);
                    String[] str_arr = my_location_str.split("=");
                    String[] str_arr_ = str_arr[6].split(",");
                    my_location_str = str_arr_[0];  //xx동
                    //tv2.setText(my_location_str);
                }catch(Exception e)
                {
                    e.printStackTrace();
                }

                Thread thread = new Thread(){
                    public void run(){
                        if(editText.getText().toString().equals("맥도날드") || editText.getText().toString().equals("롯데리아"))
                        {
                            MA.set_input(my_location_str+editText.getText().toString());
                        }else
                        {
                            MA.set_input(editText.getText().toString());
                        }
                        MA.set_input(editText.getText().toString());
                        MA.doInBackground();
                        json_str = MA.get_json();
                        //tv.setText(json_str);
                        JSONParse(json_str);    //String list 배열에 저장
                        //tv.setText(R_nameList.toString() + "\n" + R_locationList.toString());
                        //R_namList, R_locationList 만들어짐
                        if(!R_locationList.isEmpty())
                        {
                            for(int i=0;i<R_locationList.size();i++)
                            {
                                MA2.set_input(R_locationList.get(i));
                                //MA2.set_input(R_locationList.get(i));
                                MA2.doInBackground();
                                json_str = MA2.get_json();
                                JSONParse2(json_str);
                            }
                            //tv2.setText(R_latitudeList.toString() + "\n" + R_longitudeList.toString());
                        }else
                        {
                            System.out.println("isEmpty");
                        }

                        Intent intent = new Intent(getApplicationContext(), restaurant_list.class);
                        intent.putExtra("name_list",R_nameList);
                        intent.putExtra("addr_list",R_locationList);
                        intent.putExtra("lati_list",R_latitudeList);
                        intent.putExtra("longi_list",R_longitudeList);
                        startActivity(intent);
//                        R_nameList.clear();
//                        R_locationList.clear();
//                        R_latitudeList.clear();
//                        R_longitudeList.clear();
                        finish();

                    }
                };
                thread.start();

            }//on click

        });
    }

    public void JSONParse2(String json_str_)    //주소 결과
    {
        try{
            JSONObject jsonObject = new JSONObject(json_str_);
            JSONArray jsonArray = jsonObject.getJSONArray("addresses");
            //tv2.setText(json_str_);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                if(jsonObject1.get("x") != null && jsonObject1.get("y") !=null)
                {

                    R_latitudeList.add(Double.parseDouble(jsonObject1.get("x").toString()));
                    R_longitudeList.add(Double.parseDouble(jsonObject1.get("y").toString()));
                }
            }
            //tv2.setText(R_latitudeList.toString() + "\n" + R_longitudeList.toString());
        }catch(JSONException e)
        {
            e.printStackTrace();
        }
    }

    public void JSONParse(String json_str_) //식당이름 결과
    {

        try{
            JSONObject jsonObject = new JSONObject(json_str_);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                if(jsonObject1.getString("title") != null &&
                        jsonObject1.getString("category").equals("양식>햄버거") &&
                        jsonObject1.getString("address") != null)
                {
                    String getTitle =  jsonObject1.getString("title");
                    String titleFilter = getTitle.replaceAll("<b>","");
                    String title = titleFilter.replaceAll("</b>","");
                    String category = jsonObject1.getString("category");
                    String address = jsonObject1.getString("address");
                    String roadAddress = jsonObject1.getString("roadAddress");
                    R_nameList.add(title);
                    R_locationList.add(roadAddress);
                }
            }
            //tv.setText(R_nameList.toString()+"\n"+R_locationList.toString()+"\n");
        }catch(JSONException e)
        {
            e.printStackTrace();
        }
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
    public void OnMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        naverMap.setLocationSource(locationSource);
        //위치 추적모드 내위치로 이동 카메라도 자기쪽으로
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        naverMap.setLayerGroupEnabled(naverMap.LAYER_GROUP_BUILDING,true);


        //LatLng init_Position = new LatLng(35.106281,128.9661773);//하단역 2번출구
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