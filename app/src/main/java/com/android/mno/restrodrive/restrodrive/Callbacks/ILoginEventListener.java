package com.android.mno.restrodrive.restrodrive.Callbacks;

/**
 * Callback listener for login events
 */
public interface ILoginEventListener {

    void onLoginSuccess(boolean successFlag);
    void onLoginError (String errorMessage);
}
