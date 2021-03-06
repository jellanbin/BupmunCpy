package com.wonbuddism.bupmun.Common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.wonbuddism.bupmun.DataVo.PrefUserInfo;

public class PrefUserInfoManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Activity activity;
    private Gson gson;

    public PrefUserInfoManager(Activity activity) {
        this.activity = activity;
        pref=activity.getSharedPreferences("userinfo",activity.MODE_PRIVATE);
        editor = pref.edit();
        gson = new Gson();
    }

    public PrefUserInfoManager(Context context) {
        pref=context.getSharedPreferences("userinfo",context.MODE_PRIVATE);
        editor = pref.edit();
        gson = new Gson();
    }

    public  void LogIn(PrefUserInfo prefUserInfo){
        setLoginState(true);
       // setModeManager(true);
        setUserInfo(prefUserInfo);
    }

    public void LogOut(){
        setLoginState(false);
     //   setModeManager(false);
        setUserInfo(new PrefUserInfo());
    }

    public String getOTP(){
        String OTP = pref.getString("OTP","");
        return OTP;
    }

    public void setOTP(String OTP){
        editor.putString("OTP",OTP);
        editor.commit();
    }


    public PrefUserInfo getUserInfo(){
        String user = pref.getString("user","");
        return gson.fromJson(user,PrefUserInfo.class);
    }

    public void setUserInfo(PrefUserInfo userinfo){
        String user = gson.toJson(userinfo);
        editor.putString("user",user);
        editor.commit();
    }

    public boolean getLoginState(){
        return pref.getBoolean("state",false);
    }

    public void inLogged(){
        editor.putBoolean("state", true);
        editor.commit();
    }

    public void outLogged(){
        editor.putBoolean("state", false);
        editor.commit();
    }



    public void setLoginState(boolean yn){
        editor.putBoolean("state", yn);
        editor.commit();
    }


}
