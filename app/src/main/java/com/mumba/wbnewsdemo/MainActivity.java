package com.mumba.wbnewsdemo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.mumba.wbnewsdemo.ui.main.Functions;
import com.mumba.wbnewsdemo.ui.main.PlaceholderFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView nav;
    ListView navlistview,mListView;
    Context context;
    ArrayList<String> titlelisten;
    ImageView menubutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawerLayout);
        nav = findViewById(R.id.nav_view);
        navlistview = nav.findViewById(R.id.listview);
        mListView = findViewById(R.id.mListView);
        menubutton = findViewById(R.id.openmenu);

        menubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        context = getApplicationContext();
        getPageCountAndPagestitle();
        showAllNews();




    }

    static ArrayList<HashMap<String,String>> allnews = new ArrayList<>();

    void showAllNews(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().get().url("https://news.onlineappupdate.com/api/v1/news/all").build();
        Functions.Show_loader(this, false, false);
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responsestr = response.body().string();
                Functions.cancel_loader();


                JSONObject object;
                JSONArray array;
                try {
                    object = new JSONObject(responsestr);
                    array = object.getJSONArray("data");

                    for(int i = 0;i<array.length();i++){
                        HashMap<String,String> yourHashMap = new Gson().fromJson(array.getJSONObject(i).toString(), HashMap.class);
                        if(!allnews.contains(yourHashMap)){
                            allnews.add(yourHashMap);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("response00",responsestr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListViewAdapter adapter = new mListViewAdapter(MainActivity.this,R.layout.singlenews,allnews);
                        mListView.setAdapter(adapter);

                    }
                });
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error",e.getMessage());
            }
        });

    }






    class mListViewAdapter extends ArrayAdapter<HashMap<String, String>> {
        ArrayList<HashMap<String, String>> mylist;
        public mListViewAdapter(@NonNull Context context, int resource, ArrayList<HashMap<String, String>> list) {
            super(context, resource, list);
            mylist = list;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View vi = getLayoutInflater().inflate(R.layout.singlenews, parent, false);
            TextView titleen, titlebn, descriptionen, descriptionbn;
            ImageView thumbnil;
            titleen = vi.findViewById(R.id.titleen);
            titlebn = vi.findViewById(R.id.titlebn);
            descriptionen = vi.findViewById(R.id.descriptionEn);
            descriptionbn = vi.findViewById(R.id.descriptionBn);
            thumbnil = vi.findViewById(R.id.thumbnil);
            titleen.setText(mylist.get(position).get("titleEn"));
            titlebn.setText(mylist.get(position).get("titleBn"));
            Glide
                    .with(MainActivity.this)
                    .load(mylist.get(position).get("thumbnail"))
                    .centerCrop()
                    .into(thumbnil);
            String descriptionconverteren = Jsoup.parse(mylist.get(position).get("descriptionEn")).text();
            Log.e("eng",descriptionconverteren);
            descriptionen.setText(descriptionconverteren);
            String descriptionconverterbn =Jsoup.parse(mylist.get(position).get("descriptionBn")).text();
            Log.e("bang",descriptionconverterbn);
            descriptionbn.setText(descriptionconverterbn);
            return vi;
        }
    }




    private void getPageCountAndPagestitle(){
        titlelisten = new ArrayList<>();

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().get().url("https://news.onlineappupdate.com/api/v1/category/all").build();
        //Functions.Show_loader(this, false, false);
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responsestr = response.body().string();
                // Functions.cancel_loader();
                JSONObject jsonObject;
                JSONArray array;
                try {
                    jsonObject  = new JSONObject(responsestr);
                    array = jsonObject.getJSONArray("data");
                    for(int i = 0;i<array.length();i++){
                        titlelisten.add(array.getJSONObject(i).getString("nameEn"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("response",responsestr);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       ListViewAdapter adapter = new ListViewAdapter(context,R.layout.singlecategory,titlelisten);
                       navlistview.setAdapter(adapter);
                    }
                });
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error",e.getMessage());
            }
        });

    }
    class ListViewAdapter extends ArrayAdapter<String> {
        ArrayList<String> mylist;
        public ListViewAdapter(@NonNull Context context, int resource, ArrayList<String> list) {
            super(context, resource, list);
            mylist = list;
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View vi = getLayoutInflater().inflate(R.layout.singlecategory, parent, false);
            TextView titleen;
            CardView cardView;
            titleen = vi.findViewById(R.id.category);
            cardView = vi.findViewById(R.id.categoryview);
            titleen.setText(mylist.get(position));
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            return vi;
        }
    }//card on click


}