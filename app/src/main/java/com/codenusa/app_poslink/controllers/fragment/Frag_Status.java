package com.codenusa.app_poslink.controllers.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codenusa.app_poslink.R;
import com.codenusa.app_poslink.adapter.LaporanAdapter;
import com.codenusa.app_poslink.model.ApiURL;
import com.codenusa.app_poslink.model.UploadLaporan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Frag_Status extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter laporanAdapter;
    private List<UploadLaporan> UploadList;
    SwipeRefreshLayout swipeRefreshLayout;

    @SuppressLint("ResourceAsColor")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_status, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        UploadList = new ArrayList<>();
        laporanAdapter = new LaporanAdapter(UploadList, getContext().getApplicationContext());
        recyclerView.setAdapter(laporanAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                loadData();
            }
        });

        return v;
    }

    String id_users = null;
    public void loadData(){
        swipeRefreshLayout.setRefreshing(true);
        UploadList.clear();
        SharedPreferences preferences = getContext().getSharedPreferences("TB_USERS", Context.MODE_PRIVATE);
        id_users = preferences.getString("idKey", "");
        ApiURL apiURL = new ApiURL();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.getCache().clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiURL.getViewLaporan() + "?id_users=" + id_users, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    //converting the string to json array object
                    JSONArray jsonArray = json.getJSONArray("data");

                    //traversing through all the object
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);

                        //adding the product to product list
                        UploadList.add(new UploadLaporan(
                                data.getInt("id_laporan"),
                                data.getString("judul"),
                                data.getString("deskripsi"),
                                data.getString("foto"),
                                data.getString("create_date"),
                                data.getString("status")
                        ));
                    }

                    swipeRefreshLayout.setRefreshing(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                }

                laporanAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), " Network Disconnected ", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRefresh() {
        loadData();
    }

}
