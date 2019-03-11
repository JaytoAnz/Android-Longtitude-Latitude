package com.codenusa.app_poslink.controllers;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class VSignupActivity extends AppCompatActivity implements View.OnClickListener{

    EditText txtUsername, txtEmail, txtPassword, txtAlamat, txtNo_Telp;
    Button linkSignIn, SignUp;

    SpotsDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vsignup);

        //EditText
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtAlamat = (EditText) findViewById(R.id.txtAlamat);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtNo_Telp = (EditText) findViewById(R.id.txtNo_telp);

        //Button
        linkSignIn = (Button) findViewById(R.id.linkSignIn);
        SignUp = (Button) findViewById(R.id.SignUp);

        checkInternet();

        progressDialog = new SpotsDialog(this, R.style.Custom);

        linkSignIn.setOnClickListener(this);
        SignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.SignUp) {
            String nama = txtUsername.getText().toString();
            String password = txtPassword.getText().toString();
            String email = txtEmail.getText().toString();
            String alamat = txtAlamat.getText().toString();
            String no_telp = txtNo_Telp.getText().toString();
            if(nama.isEmpty() || password.isEmpty() || email.isEmpty() || alamat.isEmpty() || no_telp.isEmpty()){
                Toast.makeText(VSignupActivity.this, "Data Masih Kosong", Toast.LENGTH_SHORT).show();
            }else{
                signup(nama, password, email, alamat, no_telp);
            }
        } else if (v.getId() == R.id.linkSignIn){
            Intent intent = new Intent(VSignupActivity.this, VLoginActivity.class);
            finish();
            startActivity(intent);
        }
    }

    public boolean checkInternet(){
        boolean connectStatus = true;
        ConnectivityManager ConnectionManager=(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo= ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true ) {
            connectStatus = true;
        }
        else {
            connectStatus = false;
            Toast.makeText(this, "Network Disconnected", Toast.LENGTH_LONG).show();
        }
        return connectStatus;
    }

    private void signup(final String nama, final String password, final String email, final String alamat, final String no_telp){
        progressDialog.show();
        ApiURL apiReference = new ApiURL();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiReference.getURL() + apiReference.getSIGNUP(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                Log.i("Hitesh", "" + response);
                Toast.makeText(VSignupActivity.this, response, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(VSignupActivity.this, VLoginActivity.class);
                finish();
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(VSignupActivity.this, "Gagal Mendaftar", Toast.LENGTH_SHORT).show();

                progressDialog.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama", nama);
                params.put("email", email);
                params.put("password", password);
                params.put("alamat", alamat);
                params.put("no_telp", no_telp);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
