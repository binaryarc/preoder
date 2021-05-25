package com.example.qrcode_proto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.w3c.dom.Text;

public class QR_code extends AppCompatActivity {
    String menus;
    Intent intent;
    ImageView iv;
    TextView tv;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        tv=(TextView)findViewById(R.id.qr_info);
        intent = getIntent();
        menus = intent.getStringExtra("menus");
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

                startActivityForResult(intent,0);
                finish();
            }
        });

    }
}