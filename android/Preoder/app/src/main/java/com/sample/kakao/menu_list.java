package com.sample.kakao;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class menu_list extends AppCompatActivity {
    Intent intent;
    String brand_name, location_name;

    ExecutorService executorService = Executors.newSingleThreadExecutor();

    //String[] menu_name ;

   // String[] menu_price ;

   // Integer[] pic ;

    ArrayList<McDonaldBurger> selected_menu = new ArrayList<>();
    TextView tv_name;
    ImageButton menu_qr_btn;
    Button back_btn;
    private static ArrayList<String> name_list;
    private static ArrayList<String> addr_list;
    private static ArrayList<Double> lati_list;
    private static ArrayList<Double> longi_list;

    //public static List<McDonaldBurger> burgers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);
        tv_name = (TextView) findViewById(R.id.menu_location_name);
        menu_qr_btn = (ImageButton) findViewById(R.id.menu_qr_btn);
        back_btn = (Button) findViewById(R.id.store_back_btn);

        intent = getIntent();
        brand_name = intent.getStringExtra("brand_name");
        location_name = intent.getStringExtra("location_name");
        if((ArrayList<String>)intent.getSerializableExtra("name_list") != null)
        {
            name_list = (ArrayList<String>)intent.getSerializableExtra("name_list");
            addr_list = (ArrayList<String>)intent.getSerializableExtra("addr_list");
            lati_list = (ArrayList<Double>)intent.getSerializableExtra("lati_list");
            longi_list = (ArrayList<Double>)intent.getSerializableExtra("longi_list");
        }



        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), restaurant_list.class);
                intent.putExtra("name_list",name_list);
                intent.putExtra("addr_list",addr_list);
                intent.putExtra("lati_list",lati_list);
                intent.putExtra("longi_list",longi_list);
                startActivity(intent);
                menu_list.this.finish();
            }
        });

        tv_name.setText(location_name);
        menu_qr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(getApplicationContext(), QR_code.class);
<<<<<<< HEAD
                intent.putParcelableArrayListExtra("burgers", selected_menu);
=======
                intent.putExtra("location_name",location_name);
                intent.putExtra("menus", selected_menu);
>>>>>>> 79653ba578d0a7c9e26ba6532901cb15106cbea7
                intent.putExtra("name_list",name_list);
                intent.putExtra("addr_list",addr_list);
                intent.putExtra("lati_list",lati_list);
                intent.putExtra("longi_list",longi_list);
                startActivity(intent);
                menu_list.this.finish();
            }
        });
        ListView list = (ListView) findViewById(R.id.menu_list);

        CustomList adapt = new CustomList(menu_list.this, new ArrayList<>());
        list.setAdapter(adapt);
        list.setOnItemClickListener((parent, view, position, id) -> {
<<<<<<< HEAD
            McDonaldBurger item = (McDonaldBurger) list.getItemAtPosition(position);
            if (!selected_menu.contains(item)) {
                item.count++;
                selected_menu.add(item);
            } else {
                for (int i = 0; i < selected_menu.size(); i++) {
                    if (selected_menu.get(i).equals(item)) {
                        selected_menu.get(i).count++;
                    }
                }
            }
            adapt.notifyDataSetChanged();
=======

            TextView temp_tv1 = view.findViewById(R.id.name1);
            selected_menu +=temp_tv1.getText().toString() + ", ";
            view.setBackgroundColor(Color.GRAY);
            TextView temp_tv = view.findViewById(R.id.name3);
            Integer temp_i = Integer.parseInt(temp_tv.getText().toString());
            temp_i++;
            temp_tv.setText(temp_i.toString());


>>>>>>> 79653ba578d0a7c9e26ba6532901cb15106cbea7
        });

        executorService.submit(() -> {
            List<McDonaldBurger> burgers = getBurgers();
            adapt.update(burgers);
        });
    }

    public class CustomList extends BaseAdapter {
        public Activity context;
        public List<McDonaldBurger> burgers;

        public CustomList(Activity context, List<McDonaldBurger> burgers) {
            this.context = context;
            this.burgers = burgers;
        }

        @Override
        public int getCount() {
            return burgers.size();
        }

        @Nullable
        @Override
        public McDonaldBurger getItem(int position) {
            return burgers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void update(List<McDonaldBurger> burgers) {
            this.burgers.addAll(burgers);
            this.context.runOnUiThread(this::notifyDataSetChanged);
        }

        private class ViewHolder {
            LinearLayout layout;
            ImageView imageView;
            TextView name;
            TextView price;
            TextView count;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
            }else
            {

            }

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.list_item);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.list_img);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name1);
            viewHolder.price = (TextView) convertView.findViewById(R.id.name2);
            viewHolder.count = (TextView) convertView.findViewById(R.id.name3);
            convertView.setTag(viewHolder);

            McDonaldBurger burger = getItem(position);
            if (burger != null) {

                Glide.with(context).load(burger.imageUrl).into(viewHolder.imageView);
                viewHolder.name.setText(burger.name);
                viewHolder.price.setText(burger.price);
                viewHolder.count.setText(String.valueOf(burger.count));
                if (burger.count > 0) {
                    viewHolder.layout.setBackgroundColor(Color.GRAY);
                } else {
                    viewHolder.layout.setBackgroundColor(0);
                }
            }

            return convertView;
        }
    }

    public List<McDonaldBurger> getBurgers() {
        Document doc;
        List<McDonaldBurger> burgers = new ArrayList<>();

        try {
            doc = Jsoup.connect("https://www.mcdelivery.co.kr/kr/browse/menu.html?daypartId=1&catId=11").get();

            Element content = null;
            for (int i = 0; i < 24; i++) {
                McDonaldBurger burger = new McDonaldBurger();

                content = doc.select("div.row.row-narrow div.product-card.product-card--standard div.panel-body h5").get(i);
                burger.name = content.text();
                content = doc.select("div.product-info div.product-details div.product-cost span").get(i);
                burger.price = content.text();
                content = doc.select("div.row.row-narrow div.product-card.product-card--standard div.panel-body").get(i);
                burger.imageUrl = content.child(0).absUrl("src");

                burgers.add(burger);
            }
        } catch (Exception ignored) {
        }

        return burgers;
    }
}