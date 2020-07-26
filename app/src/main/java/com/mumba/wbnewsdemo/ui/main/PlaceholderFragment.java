package com.mumba.wbnewsdemo.ui.main;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.mumba.wbnewsdemo.MainActivity;
import com.mumba.wbnewsdemo.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (map.size() != 0) {
                if (map.containsKey(index)) {
                    ListViewAdapter adapter = new ListViewAdapter(getContext(),R.layout.singlenews,map.get(index));
                    listView.setAdapter(adapter);
                }
            }
        }
    }

    public static PlaceholderFragment newInstance(int index, String value) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        bundle.putString("value", value);
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
            value = getArguments().getString("value");
            getSingleNews();
        }
    }

    int index = 1;
    String value = "";


    ListView listView;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_main, container, false);
        listView = root.findViewById(R.id.listview);


        return root;
    }


    class ListViewAdapter extends ArrayAdapter<HashMap<String, String>> {

        ArrayList<HashMap<String, String>> mylist;

        public ListViewAdapter(@NonNull Context context, int resource, ArrayList<HashMap<String, String>> list) {
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
                    .with(PlaceholderFragment.this)
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


    static HashMap<Integer, ArrayList<HashMap<String, String>>> map = new HashMap<>();
    ArrayList<HashMap<String, String>> list;


    private void getSingleNews() {
        list = new ArrayList<>();

        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().get().url("https://news.onlineappupdate.com/api/v1/category/" + index).build();
        //  Functions.Show_loader(getContext(), false, false);
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responsestr = response.body().string();

                Log.e("response2", responsestr);

                JSONObject firstjsonObject;
                JSONObject secondJSonOBject;
                try {
                    firstjsonObject = new JSONObject(responsestr);
                    secondJSonOBject = firstjsonObject.getJSONObject("data");

                    JSONArray array = secondJSonOBject.getJSONArray("news");
                    HashMap<String, String> yourHashMap = null;
                    for (int i = 0; i < array.length(); i++) {
                        yourHashMap = new Gson().fromJson(array.getJSONObject(i).toString(), HashMap.class);
                        list.add(yourHashMap);
                    }
                    if (!map.containsKey(index)) {
                        map.put(index, list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ListViewAdapter adapter = new ListViewAdapter(getContext(),R.layout.singlenews,map.get(index));
                        listView.setAdapter(adapter);
                    }
                });


            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("error", e.getMessage());
            }
        });

    }


}