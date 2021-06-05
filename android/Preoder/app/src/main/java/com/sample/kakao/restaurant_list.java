package com.sample.kakao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
    String[] name;
    String[] addr;
    Double[] latitude;
    Double[] longitude;
    String[] brand ={"맥도날드","맥도날드","롯데리아","롯데리아"};
    Integer[] pic = {R.drawable.mcdo, R.drawable.mcdo, R.drawable.lotteria, R.drawable.lotteria};
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        intent = getIntent();

        ArrayList<String> name_list = (ArrayList<String>)intent.getSerializableExtra("name_list");
        int size = name_list.size();
        name = new String[size];
        name_list.toArray(name);

        ArrayList<String> addr_list = (ArrayList<String>)intent.getSerializableExtra("addr_list");
        addr = new String[size];
        addr_list.toArray(addr);

        ArrayList<Double> lati_list = (ArrayList<Double>)intent.getSerializableExtra("lati_list");
        latitude = new Double[size];
        lati_list.toArray(latitude);

        ArrayList<Double> longi_list = (ArrayList<Double>)intent.getSerializableExtra("longi_list");
        longitude = new Double[size];
        longi_list.toArray(longitude);

        CustomList adapt = new CustomList(restaurant_list.this);
        ListView list = (ListView) findViewById(R.id.restaurant_listview);
        list.setAdapter(adapt);
        list.setOnItemClickListener((parent, view, position, id) -> {
            intent = new Intent(getApplicationContext(), menu_list.class);
            intent.putExtra("location_name", name[position]);
            intent.putExtra("brand_name",brand[position]);
            startActivity(intent);

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
            if(temp[0].equals("맥도날드"))
            {
                restaurant_img.setImageResource(pic[mcdo]);
            }else if(temp[0].equals("롯데리아"))
            {
                restaurant_img.setImageResource(pic[lotteria]);
            }

            sell_menu.setText("햄버거");
            tv3.setText("");
            return rowView;
        }
    }
}