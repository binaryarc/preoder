package com.sample.kakao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class restaurant_list extends AppCompatActivity {
    public static int mcdo = 0;
    public static int lotteria = 1;
    public static int momst = 2;
    String[] name;
    String[] addr;
    Double[] latitude;
    Double[] longitude;
    String[] brand ={"맥도날드","맥도날드","롯데리아","롯데리아"};
    Integer[] pic = {R.drawable.mcdo, R.drawable.lotteria,R.drawable.momstouch};
    Intent intent;
    ImageButton back_btn,map_view_btn;
    private static ArrayList<String> name_list;
    private static ArrayList<String> addr_list;
    private static ArrayList<Double> lati_list;
    private static ArrayList<Double> longi_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);
        back_btn = (ImageButton)findViewById(R.id.back_btn);
        map_view_btn=(ImageButton)findViewById(R.id.map_view_btn);



        intent = getIntent();
        if((ArrayList<String>)intent.getSerializableExtra("name_list") !=null)
        {
            name_list = (ArrayList<String>)intent.getSerializableExtra("name_list");
            name = new String[name_list.size()];
            name_list.toArray(name);


            addr_list = (ArrayList<String>)intent.getSerializableExtra("addr_list");
            addr = new String[addr_list.size()];
            addr_list.toArray(addr);

            //ArrayList<Double> temp_lati_list = (ArrayList<Double>)intent.getSerializableExtra("lati_list");
            lati_list = (ArrayList<Double>)intent.getSerializableExtra("lati_list");
            latitude = new Double[lati_list.size()];
            lati_list.toArray(latitude);

            //ArrayList<Double> temp_longi_list = (ArrayList<Double>)intent.getSerializableExtra("longi_list");
            longi_list = (ArrayList<Double>)intent.getSerializableExtra("longi_list");
            longitude = new Double[longi_list.size()];
            longi_list.toArray(longitude);
        }

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Nmaps_test.class);
                startActivity(intent);
                restaurant_list.this.finish();
            }
        });

        map_view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), Nmaps_test.class);

                intent.putExtra("name_list",name_list);
                intent.putExtra("addr_list",addr_list);

                intent.putExtra("lati_list",lati_list);
                intent.putExtra("longi_list",longi_list);
                startActivity(intent);
                restaurant_list.this.finish();
            }
        });

        CustomList adapt = new CustomList(restaurant_list.this);
        ListView list = (ListView) findViewById(R.id.restaurant_listview);
        list.setAdapter(adapt);
        list.setOnItemClickListener((parent, view, position, id) -> {
            intent = new Intent(getApplicationContext(), menu_list.class);
            intent.putExtra("location_name", name[position]);
            intent.putExtra("brand_name",brand[position]);

            intent.putExtra("name_list",name_list);
            intent.putExtra("addr_list",addr_list);

            intent.putExtra("lati_list",lati_list);
            intent.putExtra("longi_list",longi_list);
            startActivity(intent);
            restaurant_list.this.finish();

        });
    }
    public class CustomList extends ArrayAdapter<String> {
        private Activity context;

        public CustomList(Activity context) {
            super(context, R.layout.list_item, name);
            this.context = context;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater=context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.list_item,null,true);
            ImageView restaurant_img=(ImageView)rowView.findViewById(R.id.list_img);
            TextView location_name=(TextView)rowView.findViewById(R.id.name1);
            TextView sell_menu=(TextView)rowView.findViewById(R.id.name2);
            TextView tv3=(TextView)rowView.findViewById(R.id.name3);

            location_name.setText(name[position]);

            String[ ]temp = name[position].split(" ");

            if(name[position].charAt(0) == '맥')
            {
                restaurant_img.setImageResource(pic[mcdo]);
            }else if(name[position].charAt(0) == '롯')
            {
                restaurant_img.setImageResource(pic[lotteria]);
            }else if(name[position].charAt(0) == '맘')
            {
                restaurant_img.setImageResource(pic[momst]);
            }

            sell_menu.setText("햄버거");
            tv3.setText("");
            return rowView;
        }
    }
}