package com.android.mno.restrodrive.restrodrive.Utility;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    public PrefManager(Context context, String prefKey) {
        this._context = context;
        pref = _context.getSharedPreferences(prefKey, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(Constants.IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(Constants.IS_FIRST_TIME_LAUNCH, true);
    }

    public void setAuthLogin(boolean authLogin){
        editor.putBoolean(Constants.IS_AUTH_ALREADY_LOGIN, authLogin);
        editor.commit();
    }

    public void removeAuthLogin(){
        editor.putBoolean(Constants.IS_AUTH_ALREADY_LOGIN, false);
        editor.commit();
    }

    public boolean isAuthAlreadyLogin(){
        return pref.getBoolean(Constants.IS_AUTH_ALREADY_LOGIN, false);
    }


}