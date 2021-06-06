package com.sample.kakao;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;

public class QR_code extends AppCompatActivity {
    String menus;
    Intent intent;
    ImageView iv;
    TextView tv;
    Button btn;
    private static ArrayList<String> name_list;
    private static ArrayList<String> addr_list;
    private static ArrayList<Double> lati_list;
    private static ArrayList<Double> longi_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        tv=(TextView)findViewById(R.id.qr_info);
        intent = getIntent();

        menus = intent.getStringExtra("menus");
        if((ArrayList<String>)intent.getSerializableExtra("name_list") != null)
        {
            name_list = (ArrayList<String>)intent.getSerializableExtra("name_list");
            addr_list = (ArrayList<String>)intent.getSerializableExtra("addr_list");
            lati_list = (ArrayList<Double>)intent.getSerializableExtra("lati_list");
            longi_list = (ArrayList<Double>)intent.getSerializableExtra("longi_list");
        }


        iv = (ImageView)findViewById(R.id.qrcode);
        btn = (Button)findViewById(R.id.back_btn);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(menus, BarcodeFormat.QR_CODE,400,400);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            iv.setImageBitmap(bitmap);
            tv.setText(menus);
        }catch (Exception e){}
        intent = new Intent(getApplicationContext(),menu_list.class);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), restaurant_list.class);
                intent.putExtra("name_list",name_list);
                intent.putExtra("addr_list",addr_list);
                intent.putExtra("lati_list",lati_list);
                intent.putExtra("longi_list",longi_list);
                startActivity(intent);
                finish();
            }
        });

    }
}