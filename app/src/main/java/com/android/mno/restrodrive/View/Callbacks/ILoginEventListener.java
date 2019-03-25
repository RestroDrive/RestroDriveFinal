package com.android.mno.restrodrive.View.Callbacks;

/**
 * Callback listener for login events
 */
public interface ILoginEventListener {

    void onLoginSuccess(boolean successFlag);
    void onLoginError (String errorMessage);
}
