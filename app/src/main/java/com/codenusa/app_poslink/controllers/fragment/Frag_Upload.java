package com.codenusa.app_poslink.controllers.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


import dmax.dialog.SpotsDialog;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CONNECTIVITY_SERVICE;

public class Frag_Upload extends Fragment {

    ImageView upload_lap, tampil_lap;
    EditText txtJudul, txtDesk;
    Button btn_laporkan;
    Bitmap bitmap;
    TextView txtlongtitude, txtlatitude, txtviewID;

    private static final int REQUEST_FERM_WRITE_STORAGE = 102;
    private LocationManager locationManager;

    SpotsDialog progressDialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_upload, container, false);

        upload_lap = (ImageView) v.findViewById(R.id.upload_lap);
        tampil_lap = (ImageView) v.findViewById(R.id.tampil_lap);
        txtJudul = (EditText) v.findViewById(R.id.txtJudul);
        txtDesk = (EditText) v.findViewById(R.id.txtDeskripsi);
        btn_laporkan = (Button) v.findViewById(R.id.btn_laporkan);
        txtlongtitude = (TextView) v.findViewById(R.id.txtlongtitude);
        txtlatitude = (TextView) v.findViewById(R.id.txtlatitude);
        txtviewID = (TextView) v.findViewById(R.id.txtID);

        progressDialog = new SpotsDialog(getContext(), R.style.Custom);

        btn_laporkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager ConnectionManager=(ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo= ConnectionManager.getActiveNetworkInfo();

                if(networkInfo !=null) {
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        upload();
                    } else {
                        Toast.makeText(getContext(), "Mohon Menyalakan GPS dahulu", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getContext(), "Mohon Menyalakan Internet dahulu", Toast.LENGTH_LONG).show();
                }
            }
        });

        upload_lap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                    }
                }
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_FERM_WRITE_STORAGE);
                } else {
                    takePicture();
                }
            }
        });

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        getLocation();

        SharedPreferences shared = getContext().getSharedPreferences("TB_USERS", Context.MODE_PRIVATE);
        String id = shared.getString("idKey", "");
        txtviewID.setText("" + id);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        txtDesk.setImeOptions(EditorInfo.IME_ACTION_DONE);
        txtDesk.setRawInputType(InputType.TYPE_CLASS_TEXT);

        return v;
    }

    private void getClean(){
        txtJudul.setText("");
        txtDesk.setText("");
        tampil_lap.setVisibility(View.GONE);
        upload_lap.setVisibility(View.VISIBLE);
    }

    private void getLocation(){
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }else{
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if(CheckNetwork()) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (location != null) {
                        double longtitude = location.getLongitude();
                        double latitude = location.getLatitude();

                        txtlongtitude.setText("" + longtitude);
                        txtlatitude.setText("" + latitude);
                    }
                } else {
                    Toast.makeText(getContext(), "Mohon Menyalakan GPS dahulu", Toast.LENGTH_LONG).show();
                }
            }else if (!CheckNetwork()){
                Toast.makeText(getContext(), "Mohon Menyalakan Internet dahulu", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                getLocation();
                break;
        }
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case 0:
                    Bundle bundle = data.getExtras();
                    bitmap = (Bitmap) bundle.get("data");
                    upload_lap.setVisibility(View.GONE);
                    tampil_lap.setVisibility(View.VISIBLE);
                    tampil_lap.setImageBitmap(bitmap);
                    saveImage(bitmap);
                    break;

                default:
                    break;
            }
        }
    }

    private void saveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        System.out.println(root + " Root value in saveImage Function");
        File myDir = new File(root + "/Laporan_Pelik");

        if (!myDir.exists()) {

            myDir.mkdirs();
        }
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String iname = "Image-" + n + ".jpg";
        File file = new File(myDir, iname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        MediaScannerConnection.scanFile(getContext(), new String[]{file.toString()}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {

                        Log.i("ExternalStorage", "Scanned " + path + ":");

                        Log.i("ExternalStorage", "-> uri=" + uri);

                    }
                });

        String Image_path = Environment.getExternalStorageDirectory() + "/Pictures/Laporan_Pelik/" + iname;
        File[] files = myDir.listFiles();
        int numberOfImages = files.length;
        System.out.println("Total images in Folder " + numberOfImages);
    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void upload(){
        String judul = txtJudul.getText().toString();
        String Deskripsi = txtDesk.getText().toString();
        if( judul.equals("") || Deskripsi.equals("")) {
            Toast.makeText(getContext(), "Data masih kosong", Toast.LENGTH_LONG).show();
        }else{
            progressDialog.show();
            final String txtLongtitude = txtlongtitude.getText().toString();
            final String txtLatitude = txtlatitude.getText().toString();
            ApiURL apiURL = new ApiURL();
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.getCache().clear();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, apiURL.getURL() + apiURL.getUpload(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Berhasil Melaporkan ", Toast.LENGTH_LONG).show();
                    getClean();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> stringMap = new HashMap<>();
                    stringMap.put("id_users", txtviewID.getText().toString());
                    stringMap.put("judul", txtJudul.getText().toString());
                    stringMap.put("deskripsi", txtDesk.getText().toString());
                    stringMap.put("image", getStringImage(bitmap));
                    stringMap.put("longtitude", txtLongtitude);
                    stringMap.put("latitude", txtLatitude);
                    return stringMap;
                }
            };

            requestQueue.add(stringRequest);
        }
    }

    private boolean CheckNetwork(){
        boolean WIFI = false;
        boolean DATA_MOBILE = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(CONNECTIVITY_SERVICE);
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
