package com.mumba.wbnewsdemo.ui.main;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.mumba.wbnewsdemo.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {


    private final Context mContext;
    ArrayList<String> titlelisten;

    public SectionsPagerAdapter(Context context, FragmentManager fm,ArrayList<String> titlelist) {
        super(fm);
        mContext = context;

        titlelisten = titlelist;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position+1,titlelisten.get(position));
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titlelisten.get(position);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return titlelisten.size()-1;
    }






}