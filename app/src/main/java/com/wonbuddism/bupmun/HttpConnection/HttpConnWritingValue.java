package com.wonbuddism.bupmun.HttpConnection;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.wonbuddism.bupmun.Utility.PrefUserInfoManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by csc-pc on 2016. 7. 6..
 */
public class HttpConnWritingValue extends AsyncTask<Void,Void,Void> {

    private Activity activity;
    private String http_conection_url = "http://115.91.201.9/meditation";
    private String http_login_otp = "otp";
    private String responseResult;
    private String OTP;
    private String TAG = "HttpConnWritingValue";
    private Dialog dialog;

    public HttpConnWritingValue(Activity activity) {
        this.activity = activity;
        this.OTP = new PrefUserInfoManager(this.activity).getOTP();
        Log.e("OTP", this.OTP);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Void doInBackground(Void... params) {
        String postData = http_login_otp + "=" + OTP;

        try {
            URL url = new URL(http_conection_url);
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

                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLconn.getInputStream(), "UTF-8"));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                responseResult = response.toString();
            }
        } catch (IOException e) {
            Log.e(TAG, "Http Connection Error :: " + e);
        }
        Log.e(TAG, "response : " + responseResult);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        String responseCode = "";
        JSONArray resultData = null;


        if (responseResult != null) {
            try {
                JSONObject jObj = new JSONObject(responseResult);
                responseCode = jObj.getString("code");


            } catch (JSONException e) {
                Log.e(TAG, "JSON error :: " + e);
            }
        } else {
            responseCode = null;
        }
        Log.d(TAG, "response code :: " + responseCode);

        if (responseCode == null) {

            Log.e(TAG,"실패0");
        } else if (responseCode.contains("00")) {
            //  00 : 정상
            Log.e(TAG,"성공");

        } else if (responseCode.contains("01")) {

            Log.e(TAG,"실패1");
            // 01 : Method 오류


        } else if (responseCode.contains("02")) {
            Log.e(TAG,"실패2");
            // 02 : 필수항목누락

        }

    }

}