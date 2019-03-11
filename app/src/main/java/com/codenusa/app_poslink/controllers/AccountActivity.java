package com.codenusa.app_poslink.controllers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.codenusa.app_poslink.R;

public class AccountActivity extends AppCompatActivity {

    ImageView back;
    AlertDialog alertdialog;
    TextView txtNama, txtNo_telp, txtAlamat, logout;
    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        txtNama = (TextView) findViewById(R.id.txtnama);
        txtNo_telp = (TextView) findViewById(R.id.txtno_telp);
        txtAlamat = (TextView) findViewById(R.id.txtAlamat);
        back = (ImageView) findViewById(R.id.back);
        logout = (TextView) findViewById(R.id.logout);

        shared = getSharedPreferences("TB_USERS", Context.MODE_PRIVATE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBack();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertdialog = new AlertDialog.Builder(AccountActivity.this).create();

                alertdialog.setTitle("Logout");
                alertdialog.setMessage("Are you sure ! logout ?");
                alertdialog.setCancelable(false);
                alertdialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        alertdialog.dismiss();

                    }
                });

                alertdialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = shared.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent(AccountActivity.this, VLoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        finish();
                        startActivity(intent);

                    }
                });
                alertdialog.show();
            }
        });

        loadData();
    }

    public void setBack(){
        super.onBackPressed();
        Intent i=new Intent(Intent.ACTION_MAIN);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
    }

    @Override
    public void onBackPressed(){
        setBack();
    }

    public void loadData(){
        String nama = shared.getString("namaKey", "");
        String alamat = shared.getString("alamatKey", "");
        String no_telp = shared.getString("telpKey", "");

        txtNama.setText("" + nama);
        txtAlamat.setText("" + alamat);
        txtNo_telp.setText("" + no_telp);
    }


}
