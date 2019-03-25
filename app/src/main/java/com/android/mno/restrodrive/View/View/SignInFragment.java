package com.android.mno.restrodrive.View.View;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.mno.restrodrive.R;
import com.android.mno.restrodrive.View.Callbacks.ILoginEventListener;
import com.android.mno.restrodrive.View.Utility.Constants;
import com.android.mno.restrodrive.View.Utility.FirebaseLogin;
import com.android.mno.restrodrive.View.Utility.GoogleLogin;
import com.android.mno.restrodrive.View.Utility.Utility;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SignInFragment extends Fragment implements View.OnClickListener{

    private final String TAG = "SignInFragment";
    private View fragmentView;
    private ILoginEventListener loginEventListener;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ILoginEventListener) {
            loginEventListener = (ILoginEventListener) context;
        } else {
            throw new ClassCastException();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentView =  inflater.inflate(R.layout.sign_in_fragment, container, false);

        initGoogleClient(getContext());

        fragmentView.findViewById(R.id.emailSignInButton).setOnClickListener(this);
        fragmentView.findViewById(R.id.facebook_login).setOnClickListener(this);
        fragmentView.findViewById(R.id.google_login).setOnClickListener(this);

        return fragmentView;
    }

    /**
     * Validates Login details for sign in
     */
    private void validateLoginDetails(){

        String email = ((EditText)fragmentView.findViewById(R.id.tvEmail)).getText().toString().trim();

        if(Utility.getInstance().isEmailValid(email)){

            String password = ((EditText)fragmentView.findViewById(R.id.tvPassword)).getText().toString().trim();

            if(Utility.getInstance().isPasswordValid(password)){

                Utility.getInstance().hideSoftKeyboard(getActivity());

                FirebaseLogin firebaseLogin = new FirebaseLogin(getContext(), loginEventListener);
                firebaseLogin.signIn(email, password);

            }else{
                Utility.getInstance().hideSoftKeyboard(getActivity());
                Utility.getInstance().showSnackbar(fragmentView,getResources().getString(R.string.enter_valid_password), getContext());
            }
        }else{
            Utility.getInstance().hideSoftKeyboard(getActivity());
            Utility.getInstance().showSnackbar(fragmentView,getResources().getString(R.string.enter_valid_email), getContext());
        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.emailSignInButton){
            validateLoginDetails();
        }else  if(v.getId() == R.id.facebook_login){

        }else  if(v.getId() == R.id.google_login){
            googleSignIn(getActivity());
        }
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
