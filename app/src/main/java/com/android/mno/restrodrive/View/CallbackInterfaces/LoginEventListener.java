package com.android.mno.restrodrive.View.CallbackInterfaces;

/**
 * Callback listener for login events
 */
public interface LoginEventListener {

    void onLoginSuccess(boolean successFlag);
    void onLoginError (String errorMessage);
}
