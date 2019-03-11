package com.codenusa.app_poslink.controllers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.codenusa.app_poslink.R;
import com.codenusa.app_poslink.model.ApiURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class VLoginActivity extends AppCompatActivity implements View.OnClickListener{

    RelativeLayout rellay1, rellay2;
    EditText txtEmail, txtPassword;
    Button linkSignUp, Login;

    SpotsDialog progressDialog;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vlogin);

        rellay1 = (RelativeLayout) findViewById(R.id.rellay1);
        rellay2 = (RelativeLayout) findViewById(R.id.rellay2);

        //Edit Text
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        //Button
        linkSignUp = (Button) findViewById(R.id.linkSignUp);
        Login = (Button) findViewById(R.id.SignIn);

        progressDialog = new SpotsDialog(this, R.style.Custom);

        handler.postDelayed(runnable, 2000); //2000 is the timeout for the splash

        SharedPreferences shared = getSharedPreferences("TB_USERS", Context.MODE_PRIVATE);
        String val = shared.getString("idKey", "");
        if (val.length() != 0) {
            Intent intent = new Intent(this, MainContainer.class);
            finish();
            startActivity(intent);
        }else {

        }

        if (CheckNetwork()) {

        }else if (!CheckNetwork()){
            Toast.makeText(this, "Network Disconnected", Toast.LENGTH_SHORT).show();
        }

        //Eksekusi
        linkSignUp.setOnClickListener(this);
        Login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.SignIn) {
            String email = txtEmail.getText().toString();
            String password = txtPassword.getText().toString();
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(VLoginActivity.this, "Data Masih Kosong", Toast.LENGTH_SHORT).show();
            }else{
                login(email, password);
            }
        } else if (v.getId() == R.id.linkSignUp){
            Intent intent = new Intent(VLoginActivity.this, VSignupActivity.class);
            finish();
            startActivity(intent);
        }
    }

    private void login(final String email, final String password){
        progressDialog.show();
        ApiURL apiReference = new ApiURL();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiReference.getURL() + apiReference.getSIGNIN(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    Log.i("" + jsonObject, "hase");
                    boolean responsestatus = jsonObject.getBoolean("success");

                    if(responsestatus){
                        String message = jsonObject.getString("message");
                        Toast.makeText(VLoginActivity.this, message, Toast.LENGTH_SHORT).show();

                        String id = jsonObject.getString("id");
                        String nama = jsonObject.getString("nama");
                        String alamat = jsonObject.getString("alamat");
                        String no_telp = jsonObject.getString("no_telp");

                        SharedPreferences preferences = getSharedPreferences("TB_USERS", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.putString("idKey", id);
                        editor.putString("namaKey", nama);
                        editor.putString("alamatKey", alamat);
                        editor.putString("telpKey", no_telp);
                        editor.commit();

                        Intent intent = new Intent(VLoginActivity.this, MainContainer.class);
                        finish();
                        startActivity(intent);
                    }else {
                        String message = jsonObject.getString("message");
                        Toast.makeText(VLoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VLoginActivity.this, "Network Disconnect", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    private boolean CheckNetwork(){
        boolean WIFI = false;
        boolean DATA_MOBILE = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

        for (NetworkInfo info : networkInfos){
            if(info.getTypeName().equalsIgnoreCase("WIFI"))
                if (info.isConnected())
                    WIFI = true;

            if(info.getTypeName().equalsIgnoreCase("MOBILE"))
                if (info.isConnected())
                    DATA_MOBILE = true;
        }

        return WIFI||DATA_MOBILE;
    }

}
