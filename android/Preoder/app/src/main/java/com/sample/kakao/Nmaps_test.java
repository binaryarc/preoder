package com.sample.kakao;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.util.FusedLocationSource;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class Nmaps_test extends AppCompatActivity {

    private MapView mapView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE =1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private Geocoder geocoder;

    Button near_list_btn;
    TextView tv,tv2;
    EditText editText;
    Double my_latitude,my_longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nmaps_test);

        near_list_btn = findViewById(R.id.near_list_btn);
        tv = findViewById(R.id.tv);
        tv2 = findViewById(R.id.tv2);
        editText = findViewById(R.id.editText);

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this::OnMapReady);


        //지도 사용권한 받기
        locationSource = new FusedLocationSource(this,LOCATION_PERMISSION_REQUEST_CODE);


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

        //LatLng init_Position = new LatLng(35.106281,128.9661773);//하단역 2번출구

        //자신의 위치 저장
        //LatLng my_position = new LatLng(my_latitude,my_longitude);

        //자신 위치로 카메라 이동
        //CameraUpdate cu = CameraUpdate.scrollTo(my_position);
        //naverMap.moveCamera(cu);    //초기위치 하단역

        //NaverMap 객체 받아서 NaverMap객체에 위치 소스 지정
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setCompassEnabled(true);     //나침반
        uiSettings.setScaleBarEnabled(true);    //거리
        uiSettings.setZoomControlEnabled(true); //zoom
        uiSettings.setLocationButtonEnabled(true);//현재위치 버튼

        //지도상에 마커찍기
//        Marker maker = new Marker();
//        maker.setPosition(my_position);
//        maker.setMap(naverMap);

        near_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = editText.getText().toString();
                List<Address> addressList = null;
                try{
                    addressList = geocoder.getFromLocationName(str,10);
                }catch(IOException ioException){
                    //e인자로 받을때
                    //e.printStackTrace();
                    Toast.makeText(Nmaps_test.this,"검색 에러",Toast.LENGTH_LONG).show();
                }
                String str_info = (addressList.get(0)).toString();
                //tv.setText( str_info.split(",")[14] );
                //tv2.setText( str_info.split(",")[16]);
                //최대 10개 까지만 가져와서 for문으로 마커 찍고 뿌리기
                //tv.setText((addressList.get(0).toString()) );
                //System.out.println( (addressList.get(0).toString()) );

                locationSource.getLastLocation().getAccuracy();

                //my_latitude = locationSource.getLastLocation().getLatitude();
                //my_longitude =locationSource.getLastLocation().getLongitude();
                //tv.setText(my_latitude.toString());
                //tv2.setText(my_longitude.toString());
            }
        });

    }


}