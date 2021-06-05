package com.sample.kakao;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MyAsyncTask2 extends AsyncTask<Void, Void, Void> {
    String json_text;
    String input_txt;

    public String get_json()
    {
        return this.json_text;
    }
    public void set_input(String input_text)
    {
        this.input_txt = input_text;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        //https://console.ncloud.com/mc/solution/naverService/application 이사이트값
        String clientId = "2nfrpmkgzn";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "DK1l0ngF8qeIbLqs73s1xfJqhSybfUUs5YnKIgME";//애플리케이션 클라이언트 시크릿값";
        try {
            String text = URLEncoder.encode(this.input_txt, "UTF-8");
            String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" +text;
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type","application/json");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                //System.out.println("Geocoding 연결 OK ");
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                //System.out.println("Geocoding 연결 에러 발생 ");
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();

            //System.out.println(response.toString());
            this.json_text = response.toString();

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {

    }
}
