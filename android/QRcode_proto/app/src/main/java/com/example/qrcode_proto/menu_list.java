package com.example.qrcode_proto;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class menu_list extends AppCompatActivity {
    Intent intent;
    String brand_name,location_name;

    String[] menu_name={"빅맥 버거","빅맥 버거 세트","상하이 버거","상하이 버거 세트","1955 버거","1955 버거 세트"};
    String[] menu_price={"4,600","5,900","4,600","5,900","5,700","7,200"};
    Integer[] pic={R.drawable.bicmac,R.drawable.bicmac_set,R.drawable.shanghai,R.drawable.shanghai_set,
                        R.drawable.burger1955,R.drawable.burger1955_set};
    String selected_menu = new String("");
    TextView tv_name;
    ImageButton menu_qr_btn;
    Button back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);
        tv_name = (TextView)findViewById(R.id.menu_location_name);
        menu_qr_btn = (ImageButton) findViewById(R.id.menu_qr_btn);
        back_btn = (Button)findViewById(R.id.store_back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(),restaurant_list.class);
                startActivityForResult(intent,0);
                finish();
            }
        });
        intent = getIntent();
        brand_name = intent.getStringExtra("brand_name");
        location_name = intent.getStringExtra("location_name");
        tv_name.setText(location_name);
        menu_qr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(),QR_code.class);
                intent.putExtra("menus",selected_menu);
                startActivityForResult(intent,2);
                finish();
            }
        });
        ListView list = (ListView) findViewById(R.id.menu_list);
        CustomList adapt = new CustomList(menu_list.this);
        list.setAdapter(adapt);
        list.setOnItemClickListener((parent, view, position, id) -> {

            selected_menu += menu_name[position]+", ";
            view.setBackgroundColor(Color.GRAY);
            TextView temp_tv = view.findViewById(R.id.name3);
            Integer temp_i = Integer.parseInt(temp_tv.getText().toString());
            temp_i++;
            temp_tv.setText(temp_i.toString());
        });
    }
    public class CustomList extends ArrayAdapter<String> {
        private Activity context;

        public CustomList(Activity context) {
            super(context, R.layout.list_item, menu_name);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.list_item,null,true);
            ImageView menu_img=(ImageView)rowView.findViewById(R.id.list_img);
            TextView tv_menu_name=(TextView)rowView.findViewById(R.id.name1);
            TextView tv_menu_price=(TextView)rowView.findViewById(R.id.name2);
            TextView tv3=(TextView)rowView.findViewById(R.id.name3);
            menu_img.setImageResource(pic[position]);
            tv_menu_name.setText(menu_name[position]);
            tv_menu_price.setText(menu_price[position]);
            tv3.setText("0");

            return rowView;
        }
    }
}