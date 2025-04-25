package com.sample.kakao;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class Menu_recommend extends AppCompatActivity {

    ImageButton back_btn,voice_btn;
    Button search_btn;
    EditText search_edt_text;
    Intent intent,voice_intent;
    final int PERMISSION = 1;
    SpeechRecognizer mRecognizer;
    TextView read_text_view,send_text_view;
    ListView list;

    private Socket client;

    private static String SERVER_IP = "[MASKED_SERVER_IP]";
    private static String CONNECT_MSG = null;

    static ArrayList<Recommend_data> recommends = new ArrayList<Recommend_data>(); //json 파싱한 데이터들 저장할 곳

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_recommend);
        list = (ListView) findViewById(R.id.rec_menu_listview);
        back_btn =findViewById(R.id.menu_rec_back_btn);     //뒤로가기 버튼
        voice_btn = findViewById(R.id.menu_rec_voice_btn);  //음성 입력버튼
        search_btn = findViewById(R.id.menu_rec_search_btn);   //추천받기 버튼
        search_edt_text = findViewById(R.id.menu_rec_editText); //추천받을 검색어
        read_text_view = findViewById(R.id.menu_rec_text);   //서버에서 받은 텍스트 보여주는 텍스트뷰
        send_text_view = findViewById(R.id.menu_rec_text2);


        // 안드로이드 6.0 버전 이상인지 체크해서 퍼미션 체크
        if(Build.VERSION.SDK_INT >= 23){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO},PERMISSION);
        }
        // RecognizerIntent 생성
        voice_intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voice_intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName()); // 여분의 키
        voice_intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR"); // 언어 설정

        //onCreate문 안 코드
        search_btn.setOnClickListener(new View.OnClickListener() { //서버로 전송 버튼
            @Override
            public void onClick(View v) {

                CONNECT_MSG = search_edt_text.getText().toString();
                if(CONNECT_MSG != null)
                {
                    //send_text_view.setText("보낸 메세지 : "+CONNECT_MSG);
                    Connect connect = new Connect();
                    connect.execute(CONNECT_MSG);


                }


            }
        });



        voice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mRecognizer = SpeechRecognizer.createSpeechRecognizer(Menu_recommend.this); // 새 SpeechRecognizer 를 만드는 팩토리 메서드
                mRecognizer.setRecognitionListener(listener); // 리스너 설정
                mRecognizer.startListening(voice_intent); // 듣기 시작

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

    }


    private class Connect extends AsyncTask< String , String,Void > {

        ProgressDialog dialog;
        String message =null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Menu_recommend.this);
            dialog.show();

        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            dialog.dismiss();
            Toast.makeText(Menu_recommend.this,"완료 되었습니다.",Toast.LENGTH_SHORT).show();

            if(!recommends.isEmpty())recommends.clear();
            try{    //json 파싱
                JSONArray jarray = new JSONArray(message);

                for(int i=0;i<jarray.length();i++)
                {
                    JSONObject jsonObject = jarray.getJSONObject(i);
                    String name = jsonObject.getString("name");
                    String img = jsonObject.getString("img");
                    String price = jsonObject.getString("price");
                    String inside = jsonObject.getString("inside");
                    String flavour = jsonObject.getString("flavour");
                    String brand = jsonObject.getString("brand");
                    Recommend_data rec_data = new Recommend_data(name,img,price,inside,flavour,brand);
                    recommends.add(rec_data);
                }

            }catch (JSONException e)
            {
                e.printStackTrace();
            }
            //read_text_view.setText(recommends.get(0).name + recommends.get(0).price + recommends.get(0).flavour);

            Menu_recommend.CustomList adapt = new Menu_recommend.CustomList(Menu_recommend.this);
            list.setAdapter(adapt);
            list.setOnItemClickListener((parent, view, position, id) -> {
                intent = new Intent(getApplicationContext(), Nmaps_test.class);
                intent.putExtra("recommend_menu_click", true);
                intent.putExtra("recommend_brand",recommends.get(position).brand);

                startActivity(intent);
                Menu_recommend.this.finish();

            });





        }   //onPostExecute

        @Override
        protected Void doInBackground(String... strings) {

            try{
                client = new Socket();
                client.connect(new InetSocketAddress(SERVER_IP,8080));


                byte[] bytes = null;


                OutputStream os = client.getOutputStream();

                message = CONNECT_MSG;
                bytes=message.getBytes("UTF-8");
                os.write(bytes);
                os.flush();

                InputStream is = client.getInputStream();

                bytes = new byte[10000];    //인풋 스트림이 작으면 오는 메시지가 잘림

                int readByteConut = is.read(bytes);

                message = new String(bytes,0,readByteConut,"UTF-8");



                os.close();
                is.close();
            }catch (Exception e){}

            if(!client.isClosed()){
                try{
                    client.close();
                }catch (IOException e){

                }
            }
            return null;
        }




    }//서버 통신 클래스

    public class Recommend_data{
        private String name;
        private String img;
        private String price;
        private String inside;
        private String flavour;
        private String brand;
        public Recommend_data(String name,String img,String price,String inside,String flavour,String brand)
        {
            this.name = name;
            this.img = img;
            this.price = price;
            this.inside = inside;
            this.flavour = flavour;
            this.brand =brand;
        }
        public String getName()
        {
            return name;
        }
        public String getImg()
        {
            return img;
        }
        public String getPrice()
        {
            return price;
        }
        public String getInside()
        {
            return inside;
        }
        public String getFlavour()
        {
            return flavour;
        }
        public String getBrand()
        {
            return brand;
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
                search_edt_text.setText(matches.get(i));
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

    public class CustomList extends ArrayAdapter<Recommend_data> {
        private Activity context;

        public CustomList(Activity context) {
            super(context, R.layout.list_item3, recommends);
            this.context = context;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.list_item3,null,true);
            ImageView menu_img=(ImageView)rowView.findViewById(R.id.list_img);
            TextView menu_name=(TextView)rowView.findViewById(R.id.name1);
            TextView menu_price=(TextView)rowView.findViewById(R.id.name2);
            TextView menu_brand=(TextView)rowView.findViewById(R.id.name3);
            menu_name.setText(recommends.get(position).name);
            menu_price.setText(recommends.get(position).price);
            menu_brand.setText(recommends.get(position).brand);
            //Glide.with(this.context).load(recommends.get(position).img).into(menu_img);
            Glide.with(this.context).load(recommends.get(position).img).override(400,400).into(menu_img);

            return rowView;
        }
    }





}