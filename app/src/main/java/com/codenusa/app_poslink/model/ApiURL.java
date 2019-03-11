package com.codenusa.app_poslink.model;

public class ApiURL {

    private final String URL = "https://poslink.pmiicyberkom.org/_api/";
//    private final String URL = "http://192.168.43.44/pelik/";
    private final String SIGNIN = "login.php";
    private final String SIGNUP = "regis.php";
    private final String Upload = "upload.php";
    private final String ViewLaporan = "https://poslink.pmiicyberkom.org/android/getlaporan";

    public String getViewLaporan() {
        return ViewLaporan;
    }

    public String getUpload() {
        return Upload;
    }

    public String getSIGNIN() {
        return SIGNIN;
    }

    public String getURL() {
        return URL;
    }

    public String getSIGNUP() {
        return SIGNUP;
    }
}
