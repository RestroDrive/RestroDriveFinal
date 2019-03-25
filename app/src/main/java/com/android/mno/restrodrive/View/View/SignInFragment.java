package com.android.mno.restrodrive.View.View;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.mno.restrodrive.R;
import com.android.mno.restrodrive.View.CallbackInterfaces.LoginEventListener;
import com.android.mno.restrodrive.View.Utility.FirebaseLogin;
import com.android.mno.restrodrive.View.Utility.Utility;
import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SignInFragment extends Fragment {

    private final String TAG = "SignInFragment";
    private View fragmentView;
    private LoginEventListener loginEventListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof LoginEventListener) {
            loginEventListener = (LoginEventListener) context;
        } else {
            throw new ClassCastException();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentView =  inflater.inflate(R.layout.sign_in_fragment, container, false);

        Button emailSignInButton = fragmentView.findViewById(R.id.emailSignInButton);
        emailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateLoginDetails();

            }
        });

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
                Utility.getInstance().showSnackbar(fragmentView,getResources().getString(R.string.enter_valid_password));
            }
        }else{
            Utility.getInstance().hideSoftKeyboard(getActivity());
            Utility.getInstance().showSnackbar(fragmentView,getResources().getString(R.string.enter_valid_email));
        }
    }
}
