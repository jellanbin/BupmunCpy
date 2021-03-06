package com.wonbuddism.bupmun.HttpConnection;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.wonbuddism.bupmun.Board.BoardCommentRecyclerViewAdapter;
import com.wonbuddism.bupmun.DataVo.BoardComment;
import com.wonbuddism.bupmun.Common.PrefUserInfoManager;
import com.wonbuddism.bupmun.R;

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


public class HTTPconnBoardCommList extends AsyncTask<Void,Void,Void> {
    private String http_host;
    private Activity activity;
    private String TAG = "HTTPconnBoardMainAll";
    private String http_conection_url = "board/comment";
    private String http_otp ="otp";
    private String http_boardno = "boardno";
    private String http_writeno = "writeno";
    private String http_page_no = "page_no";
    private String responseResult;
    private String OTP;
    private String BOARDNO;
    private String WRITENO;
    private String PAGE_NO;
    private BoardCommentRecyclerViewAdapter adapter;
    private TextView total;

    public HTTPconnBoardCommList(Activity activity,BoardCommentRecyclerViewAdapter adapter,
                                 TextView total,String boardno, String writeno, String page_no) {
        this.activity = activity;
        this.adapter = adapter;
        this.total = total;
        this.OTP= new PrefUserInfoManager(this.activity).getOTP();
        this.BOARDNO = boardno;
        this.WRITENO = writeno;
        this.PAGE_NO = page_no;
        this.http_host = this.activity.getResources().getString(R.string.host_name);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected Void doInBackground(Void... params) {
        String postData =  http_otp +"="+OTP + "&" + http_boardno +"="+ BOARDNO
                +  "&" + http_writeno +"="+WRITENO+  "&" + http_page_no +"="+PAGE_NO;

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
        JSONArray resultData1 = new JSONArray();
        JSONArray resultData2 = new JSONArray();
        ArrayList<BoardComment> boardComments;

        if (responseResult != null) {
            try {
                JSONObject jObj = new JSONObject(responseResult);
                responseCode = jObj.getString("code");
                resultData1 = jObj.getJSONArray("resultData1");
                resultData2 = jObj.getJSONArray("resultData2");

            } catch (JSONException e) {
                Log.e(TAG, "JSON error :: " + e);
            }
        } else {
            responseCode = null;
        }
        Log.d(TAG, "response code :: " + responseCode);

        if (responseCode == null) {
            Toast.makeText(activity, "덧글 불러오기에 실패하였습니다", Toast.LENGTH_SHORT).show();

        } else if (responseCode.contains("00")) {
            //  00 : 정상
            try {
                String total_cnt = resultData1.getJSONObject(0).getString("TOTAL_CNT");
                total.setText(total_cnt);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            boardComments = getBoardCommList(resultData2);
            ArrayList<BoardComment> comments = adapter.getBoardComments();
            comments.addAll(boardComments);
            adapter.setItems(comments);
          /*  mItems.addAll(newItems);
            mAdapter.setItems(mItems); // No need of this*/
          /*  int i = 0;
            for(BoardComment comment : boardComments)
            {
                Log.e("comment", comment.toString());
                adapter.add(comment);
                Log.e("getValueAt", adapter.getValueAt(i++).toString());
            }*/

            adapter.showLoading(false);
            adapter.notifyDataSetChanged();

            // 로딩페이지 Activity Stack에서 제거
            //Toast.makeText(activity,"성공",Toast.LENGTH_SHORT).show();

        } else if (responseCode.contains("01")) {

            Toast.makeText(activity,"불러오기에 실패하였습니다",Toast.LENGTH_SHORT).show();
            // 01 : Method 오류


        } else if (responseCode.contains("02")) {
            Toast.makeText(activity,"불러오기에 실패하였습니다",Toast.LENGTH_SHORT).show();
            // 02 : 필수항목누락

        }else if (responseCode.contains("03")) {
            Toast.makeText(activity,"로그인이 만료되었습니다",Toast.LENGTH_SHORT).show();
            // 03 : 로그인 만료
            new HttpConnLogout(activity).execute();

        }

    }

    private ArrayList<BoardComment> getBoardCommList(JSONArray jList){
        int count = jList.length();
        ArrayList<BoardComment> list = new ArrayList<>();
        BoardComment info = null;

        for(int i=0; i<count;i++){
            try {
                info = getBoardComment((JSONObject) jList.get(i));
                list.add(info);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    private BoardComment getBoardComment(JSONObject jInfo){
        String SEQNO = ""; // 댓글번호
        String CONTENT = ""; //댓글내용
        String USERID = ""; // 작성자아이디
        String USERNAME = ""; //작성자이름
        String WRITETIME = ""; //작성시간

        try{
            SEQNO = jInfo.getString("SEQNO");
            CONTENT = jInfo.optString("CONTENT","").equals("null")?"":jInfo.optString("CONTENT","");
            USERID = jInfo.getString("USERID");
            USERNAME = jInfo.getString("USERNAME");
            WRITETIME = jInfo.getString("WRITETIME");
        }catch (JSONException e){
            e.printStackTrace();
        }
        BoardComment info=new BoardComment(SEQNO,CONTENT,USERID,USERNAME,WRITETIME);
        return info;
    }
}
