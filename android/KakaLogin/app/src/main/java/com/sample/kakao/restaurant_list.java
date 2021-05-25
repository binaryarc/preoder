package com.sample.kakao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class restaurant_list extends AppCompatActivity {
    String[] name = {"맥도날드 동아대점", "맥도날드 부산하단DT점", "롯데리아 동아대점", "롯데리아 하단점"};
    String[] brand ={"맥도날드","맥도날드","롯데리아","롯데리아"};
    Integer[] pic = {R.drawable.mcdo, R.drawable.mcdo, R.drawable.lotteria, R.drawable.lotteria};
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        CustomList adapt = new CustomList(restaurant_list.this);
        ListView list = (ListView) findViewById(R.id.restaurant_listview);
        list.setAdapter(adapt);
        list.setOnItemClickListener((parent, view, position, id) -> {
            intent = new Intent(getApplicationContext(), menu_list.class);
            intent.putExtra("location_name", name[position]);
            intent.putExtra("brand_name",brand[position]);
            startActivityForResult(intent, 1);
            finish();
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
            restaurant_img.setImageResource(pic[position]);
            location_name.setText(name[position]);
            sell_menu.setText("햄버거");
            tv3.setText("");
            return rowView;
        }
    }
}