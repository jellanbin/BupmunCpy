package com.wonbuddism.bupmun.HttpConnection;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.wonbuddism.bupmun.Login.LoginMainActivity;
import com.wonbuddism.bupmun.DataVo.PrefUserInfo;
import com.wonbuddism.bupmun.Common.PrefUserInfoManager;
import com.wonbuddism.bupmun.Dialog.ProgressWaitDaialog;
import com.wonbuddism.bupmun.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnLogout extends AsyncTask<Void,Void,Void> {

    private Activity activity;
    private String http_conection_url = "logout";
    private String http_login_otp="otp";
    private String responseResult;
    private String OTP;
    private String http_host;
    private Dialog dialog;

    public HttpConnLogout(Activity activity) {
        this.activity = activity;
        this.dialog  = new ProgressWaitDaialog(activity);
        this.dialog.show();
        this.OTP= new PrefUserInfoManager(this.activity).getOTP();
        this.http_host = this.activity.getResources().getString(R.string.host_name);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Void doInBackground(Void... params) {
        String postData =  http_login_otp+"="+OTP;

        try {
            URL url = new URL(http_host+http_conection_url);
            HttpURLConnection httpURLconn = (HttpURLConnection) url.openConnection();
            httpURLconn.setRequestMethod("POST");
            httpURLconn.setUseCaches(false);
            httpURLconn.setDoInput(true);
            httpURLconn.setDoOutput(true);

            httpURLconn.addRequestProperty("Accept", "application/json");
            httpURLconn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpURLconn.connect();



            OutputStream outputStream = httpURLconn.getOutputStream();
            outputStream.write(postData.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = httpURLconn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLconn.getInputStream(),"UTF-8"));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                responseResult = response.toString();
            }
        } catch (IOException e) {
            Log.e("HttpConnLogin", "Http Connection Error :: " + e);
        }
        Log.d("HttpConnLogin", "response : " + responseResult);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        dialog.dismiss();
        String responseCode = "";
        String userData = "";
        String msg = "";

        if (responseResult != null) {
            try {

                JSONObject jObj = new JSONObject(responseResult);
                responseCode = jObj.getString("code");
                msg = jObj.getString("message");

            } catch (JSONException e) {
                Log.e("HttpConnLogin", "JSON error :: " + e);
            }
        } else {
            responseCode = null;
        }
        Log.d("HttpConnLogin", "response code :: " + responseCode);


        if (responseCode == null) {
            Toast.makeText(activity,"로그아웃 실패하였습니다",Toast.LENGTH_SHORT).show();

        } else if (responseCode.contains("00")) {
            //  00 : 정상

            PrefUserInfo userInfo =  new PrefUserInfo();
            new PrefUserInfoManager(activity).LogOut();

            activity.startActivity(new Intent(activity, LoginMainActivity.class)); // 로딩이 끝난후 이동할 Activity
            activity.finish(); // 로딩페이지 Activity Stack에서 제거
            Toast.makeText(activity,"로그아웃 되었습니다",Toast.LENGTH_SHORT).show();

        } else if (responseCode.contains("01")) {

            Toast.makeText(activity,"로그아웃 실패하였습니다",Toast.LENGTH_SHORT).show();
            // 01 : Method 오류


        } else if (responseCode.contains("02")) {
            Toast.makeText(activity,"로그아웃 실패하였습니다",Toast.LENGTH_SHORT).show();
            // 02 : 필수항목누락


        }

    }
}

