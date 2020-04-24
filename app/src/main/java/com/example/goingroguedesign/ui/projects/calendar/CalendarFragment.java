package com.example.goingroguedesign.ui.projects.calendar;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.goingroguedesign.R;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CalendarFragment extends Fragment {

    private static final OkHttpClient mHTTPClient = new OkHttpClient();
    TextView tvCalendar;
    String url = "https://cs493-a3-yuansh.wl.r.appspot.com/slips";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);

        tvCalendar = root.findViewById(R.id.tvCalendar);
        String results = null;

        new apiGetTask().execute(url);

        //tvCalendar.setText(results);




        // Inflate the layout for this fragment
        return root;

    }

    private static String doHTTPGet(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = mHTTPClient.newCall(request).execute();
        try {
            return response.body().string();
        } finally {
            response.close();
        }
    }

    public class apiGetTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String searchResults = null;
            try {
                searchResults = doHTTPGet(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return searchResults;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                tvCalendar.setText(s);
            }
        }


    }


}
