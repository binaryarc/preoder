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

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Nmaps_test extends AppCompatActivity {

    private MapView mapView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private static NaverMap naverMap;
    private Geocoder geocoder;
    private static String json_str;
    private MyAsyncTask MA = new MyAsyncTask();
    private MyAsyncTask2 MA2 = new MyAsyncTask2();
    private static ArrayList<String> R_nameList;
    private static ArrayList<String> R_locationList;
    private static ArrayList<Double> R_latitudeList;
    private static ArrayList<Double> R_longitudeList;
    Intent intent;
    String my_location_str;
    Button search_btn;
    TextView tv, tv2, tv3;
    EditText editText;
    Double my_latitude, my_longitude;
    private static double[] lati_arr, longi_arr;
    private static String[] name, addr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nmaps_test);

        search_btn = findViewById(R.id.search_btn);
        tv = findViewById(R.id.tv);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        editText = findViewById(R.id.editText);

        //지도 사용권한 받기
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        mapView = (MapView) findViewById(R.id.map_view);
//        Marker test_mk = new Marker();
//        test_mk.setPosition(new LatLng(35.107,128.967));
//        test_mk.setMap(naverMap);


        intent = getIntent();
<<<<<<< HEAD
        R_nameList = (ArrayList<String>)intent.getSerializableExtra("name_list");
        if(R_nameList !=null && getIntent() !=null)
        {
           // tv.setText(name[0]);
=======
        R_nameList = (ArrayList<String>) intent.getSerializableExtra("name_list");
        if (R_nameList != null && getIntent() != null) {
            //tv.setText(name[0]);
>>>>>>> 79653ba578d0a7c9e26ba6532901cb15106cbea7
            R_latitudeList.clear();
            R_longitudeList.clear();

            R_locationList = (ArrayList<String>) intent.getSerializableExtra("addr_list");
            R_latitudeList = (ArrayList<Double>) intent.getSerializableExtra("lati_list");
            R_longitudeList = (ArrayList<Double>) intent.getSerializableExtra("longi_list");


        } else {
            R_nameList = new ArrayList<>();
            R_locationList = new ArrayList<>();
            R_latitudeList = new ArrayList<>();
            R_longitudeList = new ArrayList<>();

        }
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this::OnMapReady);

       search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

<<<<<<< HEAD
                Thread thread = new Thread(){
                    public void run(){
                        /*geocoder = new Geocoder(mapView.getContext());
=======
                Thread thread = new Thread() {
                    public void run() {
                        geocoder = new Geocoder(mapView.getContext());
>>>>>>> 79653ba578d0a7c9e26ba6532901cb15106cbea7
                        my_latitude = locationSource.getLastLocation().getLatitude();
                        my_longitude = locationSource.getLastLocation().getLongitude();
                        //tv2.setText(my_latitude.toString()+"\n"+my_longitude.toString());
                        //자신의 위치 저장
                        try {
                            my_location_str = geocoder.getFromLocation(my_latitude, my_longitude, 1).toString();
                            //tv.setText(my_location_str);
                            String[] str_arr = my_location_str.split("=");
                            String[] str_arr_ = str_arr[6].split(",");
                            my_location_str = str_arr_[0];  //xx동
                            //tv2.setText(my_location_str);
                        } catch (Exception e) {
                            e.printStackTrace();
<<<<<<< HEAD
                        }*/
                        if(editText.getText().toString().equals("맥도날드") || editText.getText().toString().equals("롯데리아")||editText.getText().toString().equals("맘스터치"))
                        {
                            MA.set_input(my_location_str+" "+editText.getText().toString());
                        }else
                        {
=======
                        }
                        if (editText.getText().toString().charAt(0) == '맥' || editText.getText().toString().charAt(0) == '롯' || editText.getText().toString().charAt(0) == '맘') {
                            MA.set_input(my_location_str + " " + editText.getText().toString());
                        } else {
>>>>>>> 79653ba578d0a7c9e26ba6532901cb15106cbea7
                            MA.set_input(editText.getText().toString());
                        }
                        //MA.set_input(editText.getText().toString());
                        MA.doInBackground();

                        json_str = MA.get_json();
                        //tv.setText(json_str);
                        JSONParse(json_str);    //String list 배열에 저장


                        //tv.setText(R_nameList.toString() + "\n" + R_locationList.toString());
                        //R_namList, R_locationList 만들어짐
                        if (!R_locationList.isEmpty()) {
                            R_latitudeList.clear();
                            R_longitudeList.clear();
                            for (int i = 0; i < R_locationList.size(); i++) {
                                MA2.set_input(R_locationList.get(i));
                                //MA2.set_input(R_locationList.get(i));
                                MA2.doInBackground();
                                json_str = MA2.get_json();
                                JSONParse2(json_str);    //String list 배열에 저장
                            }

                            //tv2.setText(R_latitudeList.toString() + "\n" + R_longitudeList.toString());
                        } else {
                            System.out.println("isEmpty");
                        }

                        Intent intent = new Intent(getApplicationContext(), restaurant_list.class);

                        intent.putExtra("name_list", R_nameList);
                        intent.putExtra("addr_list", R_locationList);
                        intent.putExtra("lati_list", R_latitudeList);
                        intent.putExtra("longi_list", R_longitudeList);


                        startActivity(intent);

                        finish();
                    }//run()
                };
                thread.start();
            }//on click
        });

    }

    public static void JSONParse2(String json_str_)    //주소 결과
    {

        try {
            JSONObject jsonObject = new JSONObject(json_str_);
            JSONArray jsonArray = jsonObject.getJSONArray("addresses");
            //tv2.setText(json_str_);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                if (jsonObject1.get("x") != null && jsonObject1.get("y") != null) {

                    R_latitudeList.add(Double.parseDouble(jsonObject1.get("y").toString()));
                    R_longitudeList.add(Double.parseDouble(jsonObject1.get("x").toString()));
                }
            }
            //tv2.setText(R_latitudeList.toString() + "\n" + R_longitudeList.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void JSONParse(String json_str_) //식당이름 결과
    {
        R_nameList.clear();
        R_locationList.clear();
        try {
            JSONObject jsonObject = new JSONObject(json_str_);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                if (jsonObject1.getString("title") != null &&
                        (jsonObject1.getString("category").equals("양식>햄버거") || jsonObject1.getString("category").equals("음식점>치킨,닭강정")) &&
                        jsonObject1.getString("address") != null) {
                    String getTitle = jsonObject1.getString("title");
                    String titleFilter = getTitle.replaceAll("<b>", "");
                    String title = titleFilter.replaceAll("</b>", "");
                    String category = jsonObject1.getString("category");
                    String address = jsonObject1.getString("address");
                    String roadAddress = jsonObject1.getString("roadAddress");
                    R_nameList.add(title);
                    R_locationList.add(roadAddress);
                }
            }
            //tv.setText(R_nameList.toString()+"\n"+R_locationList.toString()+"\n");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        //권한획득 여부 확인
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void OnMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        naverMap.setLocationSource(locationSource);
        //위치 추적모드 내위치로 이동 카메라도 자기쪽으로
<<<<<<< HEAD
        //naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        naverMap.setLayerGroupEnabled(naverMap.LAYER_GROUP_BUILDING,true);
=======
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        naverMap.setLayerGroupEnabled(naverMap.LAYER_GROUP_BUILDING, true);
>>>>>>> 79653ba578d0a7c9e26ba6532901cb15106cbea7


        LatLng init_Position = new LatLng(35.106281,128.9661773);//하단역 2번출구
        //자신 위치로 카메라 이동
        //CameraUpdate cu = CameraUpdate.scrollTo(init_Position);
        //naverMap.moveCamera(cu);    //초기위치 하단역

        //NaverMap 객체 받아서 NaverMap객체에 위치 소스 지정
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setCompassEnabled(true);     //나침반
        uiSettings.setScaleBarEnabled(true);    //거리
        uiSettings.setZoomControlEnabled(true); //zoom
        uiSettings.setLocationButtonEnabled(true);//현재위치 버튼
<<<<<<< HEAD
//여기서 메뉴를 선택하고 스크롤을 내리면 여러개가 선택이 돼요 ㅠ
        if(R_latitudeList.size()>0)
        {
=======

        if (R_latitudeList.size() > 0) {
>>>>>>> 79653ba578d0a7c9e26ba6532901cb15106cbea7
            //tv.setText(R_nameList.toString() + "\n" + R_locationList.toString());
            //tv2.setText(R_latitudeList.toString() + "\n" + R_longitudeList.toString());
            ArrayList<Marker> mk_arr = new ArrayList<>();
            for (int i = 0; i < R_nameList.size(); i++) {
                Marker mk = new Marker();
                if (R_latitudeList.get(i) != null && R_longitudeList.get(i) != null) {
                    //Toast.makeText(Nmaps_test.this,i + " on map불림",Toast.LENGTH_LONG).show();
                    mk.setPosition(new LatLng(R_latitudeList.get(i), R_longitudeList.get(i)));
                    mk.setTag(R_nameList.get(i));
                    //mk.setTag("맥도날드");
                    //mk.setCaptionText(R_nameList.get(i));
                    mk_arr.add(mk);
                    //tv.setText(R_nameList.toString());
                    //tv2.setText(R_latitudeList.toString() + "\n" + R_longitudeList.toString());
                    //Toast.makeText(Nmaps_test.this,R_latitudeList.size()+"개 표시", Toast.LENGTH_LONG).show();

                }
            }
            InfoWindow infoWindow;
            for (int i = 0; i < mk_arr.size(); i++) {
                infoWindow = new InfoWindow();
                infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(this) {
                    @NonNull
                    @NotNull
                    @Override
                    public CharSequence getText(@NonNull @NotNull InfoWindow infoWindow) {
                        return (CharSequence) infoWindow.getMarker().getTag();
                    }
                });
                mk_arr.get(i).setMap(naverMap);
                infoWindow.open(mk_arr.get(i));
            }
//            for (Marker marker : mk_arr) {
//                marker.setMap(naverMap);
//                infoWindow.open(marker);
//
//            }

        }

        //지도상에 마커찍기
//        Marker maker = new Marker();
//        maker.setPosition(init_Position);
//        maker.setMap(naverMap);
//
//        Marker test_mk = new Marker();
//        test_mk.setPosition(new LatLng(35.107,128.967));
//        test_mk.setMap(naverMap);
    }
}