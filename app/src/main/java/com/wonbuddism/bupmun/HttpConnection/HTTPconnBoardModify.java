package com.wonbuddism.bupmun.HttpConnection;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.wonbuddism.bupmun.Common.PrefUserInfoManager;
import com.wonbuddism.bupmun.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPconnBoardModify extends AsyncTask<Void,Void,Void>{
    private Activity activity;
    private String TAG = "HTTPconnBoardModify";
    private String http_conection_url = "board/file/modify";
    private String http_otp ="otp";
    private String http_boardno ="boardno";
    private String http_writeno ="writeno";
    private String http_title = "title";
    private String http_content = "content";
    private String responseResult;
    private String OTP;
    private String BOARDNO;
    private String WRITENO;
    private String TITLE;
    private String CONTENT;
    private String http_host;


    public HTTPconnBoardModify(Activity activity,String boardno,String writeno, String title, String content) {
        this.activity = activity;
        this.OTP= new PrefUserInfoManager(this.activity).getOTP();
        this.BOARDNO = boardno;
        this.TITLE = title;
        this.CONTENT = content;
        this.WRITENO = writeno;
        this.http_host = this.activity.getResources().getString(R.string.host_name);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Void doInBackground(Void... params) {
        String postData =  http_otp +"="+OTP + "&" +http_boardno +"="+BOARDNO
                + "&" +http_writeno +"="+WRITENO + "&" +http_title +"="+TITLE
                + "&" + http_content +"="+CONTENT;

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
            Log.e(TAG, "Http Connection Error :: " + e);
        }
        Log.d(TAG, "response : " + responseResult);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        String responseCode = "";
        String resultData = null;

        if (responseResult != null) {
            try {
                JSONObject jObj = new JSONObject(responseResult);
                responseCode = jObj.getString("code");
                resultData = jObj.getString("resultData");

            } catch (JSONException e) {
                Log.e(TAG, "JSON error :: " + e);
            }
        } else {
            responseCode = null;
        }
        Log.d(TAG, "response code :: " + responseCode);

        if (responseCode == null) {
            Toast.makeText(activity, "수정에 실패하였습니다", Toast.LENGTH_SHORT).show();

        } else if (responseCode.contains("00")) {
            //  00 : 정상

            // 로딩페이지 Activity Stack에서 제거
            Toast.makeText(activity, "정상적으로 글이 수정 되었습니다", Toast.LENGTH_SHORT).show();

        } else if (responseCode.contains("01")) {

            Toast.makeText(activity,"수정에 실패하였습니다",Toast.LENGTH_SHORT).show();
            // 01 : Method 오류


        } else if (responseCode.contains("02")) {
            Toast.makeText(activity,"수정에 실패하였습니다",Toast.LENGTH_SHORT).show();
            // 02 : 필수항목누락

        }else if (responseCode.contains("03")) {
            Toast.makeText(activity,"로그인이 만료되었습니다",Toast.LENGTH_SHORT).show();
            // 03 : 로그인 만료
            new HttpConnLogout(activity).execute();

        }

    }

}
