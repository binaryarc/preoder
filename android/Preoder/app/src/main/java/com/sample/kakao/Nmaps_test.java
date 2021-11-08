package com.sample.kakao;

import android.Manifest;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

    private static MapView mapView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static FusedLocationSource locationSource;
    private static NaverMap naverMap;
    private static Geocoder geocoder;
    private static String json_str;
    public static MyAsyncTask MA = new MyAsyncTask();
    public static MyAsyncTask2 MA2 = new MyAsyncTask2();
    private static ArrayList<String> R_nameList;
    private static ArrayList<String> R_locationList;
    private static ArrayList<Double> R_latitudeList;
    private static ArrayList<Double> R_longitudeList;
    static Intent intent,voice_intent;
    static String my_location_str,res_name;
    Button search_btn;
    ImageButton voice_btn,back_btn;
    TextView tv, tv2, tv3;
    EditText editText;
    static Double my_latitude, my_longitude;
    private static double[] lati_arr, longi_arr;
    private static String[] name, addr;
    final int PERMISSION = 1;
    SpeechRecognizer mRecognizer;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nmaps_test);

        search_btn = findViewById(R.id.search_btn);
        back_btn = findViewById(R.id.nmap_back_btn);
        tv = findViewById(R.id.tv);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        editText = findViewById(R.id.editText);

        //지도 사용권한 받기
        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        mapView = (MapView) findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this::OnMapReady);




        voice_btn = findViewById(R.id.voice_btn);
        intent = getIntent();


        R_nameList = (ArrayList<String>)intent.getSerializableExtra("name_list");
        if(R_nameList !=null && getIntent() !=null)
            {
           // tv.setText(name[0]);
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


        // 안드로이드 6.0 버전 이상인지 체크해서 퍼미션 체크
        if(Build.VERSION.SDK_INT >= 23){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }
        // RecognizerIntent 생성
        voice_intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voice_intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName()); // 여분의 키
        voice_intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR"); // 언어 설정


        voice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(Nmaps_test.this); // 새 SpeechRecognizer 를 만드는 팩토리 메서드
                mRecognizer.setRecognitionListener(listener); // 리스너 설정
                mRecognizer.startListening(intent); // 듣기 시작

            }
        }); //음성 입력 버튼

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if(intent.getBooleanExtra("recommend_menu_click",false))
        {
            editText.setText(intent.getStringExtra("recommend_brand"));
            search_and_view();
        }//메뉴 추천에서 넘어올 경우



        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_and_view();
            }//on click
        }); // 검색 버튼
    }





    public void search_and_view()
    {
        final EditText editText = findViewById(R.id.editText);
        Thread thread = new Thread(){

            public void run(){
//                if(locationSource.isActivated())get_location();
                get_location();
                if (editText.getText().toString().charAt(0) == '맥' || editText.getText().toString().charAt(0) == '롯'
                        || editText.getText().toString().charAt(0) == '맘' ||editText.getText().toString().charAt(0) == '버') {

                    MA.set_input(my_location_str + " " + editText.getText().toString());
                }else
                {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions,
                                           @NonNull @NotNull int[] grantResults) {
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

        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
        naverMap.setLayerGroupEnabled(naverMap.LAYER_GROUP_BUILDING, true);

        //NaverMap 객체 받아서 NaverMap객체에 위치 소스 지정
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setCompassEnabled(true);     //나침반
        uiSettings.setScaleBarEnabled(true);    //거리
        uiSettings.setZoomControlEnabled(true); //zoom
        uiSettings.setLocationButtonEnabled(true);//현재위치 버튼

        if (R_latitudeList.size() > 0) {
            //tv.setText(R_nameList.toString() + "\n" + R_locationList.toString());
            //tv2.setText(R_latitudeList.toString() + "\n" + R_longitudeList.toString());
            ArrayList<Marker> mk_arr = new ArrayList<>();
            for (int i = 0; i < R_nameList.size(); i++) {
                Marker mk = new Marker();
                if (R_latitudeList.get(i) != null && R_longitudeList.get(i) != null) {
                    mk.setPosition(new LatLng(R_latitudeList.get(i), R_longitudeList.get(i)));
                    mk.setTag(R_nameList.get(i));
                    mk_arr.add(mk);
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

        }

    }//onMapReady

    public void get_location()
    {
        geocoder = new Geocoder(mapView.getContext());
        if(locationSource.getLastLocation()!=null)
        {
            my_latitude = locationSource.getLastLocation().getLatitude();
            my_longitude = locationSource.getLastLocation().getLongitude();
        }else
        {
            get_location();
        }

        try{
            my_location_str = geocoder.getFromLocation(my_latitude,my_longitude,1).toString();
            String[] str_arr = my_location_str.split("=");
            String[] str_arr_ = str_arr[6].split(",");
            my_location_str = str_arr_[0];  //xx동
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            // 말하기 시작할 준비가되면 호출
            Toast.makeText(getApplicationContext(),"음성인식 시작",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {
            // 말하기 시작했을 때 호출
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            // 입력받는 소리의 크기를 알려줌
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            // 말을 시작하고 인식이 된 단어를 buffer에 담음
        }

        @Override
        public void onEndOfSpeech() {
            // 말하기를 중지하면 호출
        }

        @Override
        public void onError(int error) {
            // 네트워크 또는 인식 오류가 발생했을 때 호출
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER 가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    break;
                default:
                    message = "알 수 없는 오류임";
                    break;
            }

            Toast.makeText(getApplicationContext(), "에러 발생 : " + message,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle results) {
            // 인식 결과가 준비되면 호출
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줌
            ArrayList<String> matches =
                    results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            for(int i = 0; i < matches.size() ; i++){
                editText.setText(matches.get(i));
            }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
            // 부분 인식 결과를 사용할 수 있을 때 호출
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
            // 향후 이벤트를 추가하기 위해 예약
        }
    }; //listener
}