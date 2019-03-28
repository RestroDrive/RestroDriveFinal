package com.android.mno.restrodrive.restrodrive.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.android.mno.restrodrive.R;
import com.android.mno.restrodrive.restrodrive.Utility.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class GoogleLogin {

    private static GoogleLogin googleLoginInstance = null;
    private GoogleSignInClient mGoogleSignInClient;

    public static GoogleLogin getInstance(){
        if(googleLoginInstance == null)
            googleLoginInstance = new GoogleLogin();

        return googleLoginInstance;
    }

    public void initGoogleClient(Context context){
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    public void googleSignIn(Activity activity) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, Constants.GOOGLE_SIGN_IN);
    }
}
