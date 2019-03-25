package com.android.mno.restrodrive.View.Utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.mno.restrodrive.View.View.LoginActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Pattern;

/**
 * Utility class for common methods
 */
public class Utility {

    private static final String TAG = "Utility";
    private static Utility utilityInstance = null;

    public static Utility getInstance(){
        if(utilityInstance == null)
            utilityInstance = new Utility();

        return utilityInstance;
    }

    public boolean isEmailValid(String email) {

        if(email != null){
            if(!email.equals("")){
                String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
                Pattern pat = Pattern.compile(emailRegex);

                return pat.matcher(email).matches();
            }
        }

        return false;
    }

    public boolean isPasswordValid(String password) {

        if(password != null){
            if(!password.equals("")){
                String passwordRegex = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
                Pattern pat = Pattern.compile(passwordRegex);

                return pat.matcher(password).matches();
            }
        }

        return false;

    }

    public boolean isValidUserName(String userName){

        if(userName != null) {
            if (!userName.equals("")) {

                String userNameRegex = "[a-zA-Z0-9\\._\\-]{3,}";
                Pattern pat = Pattern.compile(userNameRegex);

                return pat.matcher(userName).matches();
            }
        }

        return false;
    }

    /**
     * Method to display SnackBar
     * @param parentView
     * @param message
     */
    public void showSnackbar(View parentView, String message){
        Snackbar.make(parentView, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Method to hide soft keyboard
     * @param activity
     */
    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * For checking if google services are enabled or not
    */
    public boolean isMapServicesOk(Activity activity) {
        Log.d(TAG, "isServicesOk: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);

        if (available == ConnectionResult.SUCCESS) {
            //user can make map requests
            Log.d(TAG, "isServicesOk: Google play services are working fine");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured and we can resolve it
            Log.d(TAG, "isServicesOk: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(activity, available, Constants.ERROR_DIALOGUE_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(activity, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}