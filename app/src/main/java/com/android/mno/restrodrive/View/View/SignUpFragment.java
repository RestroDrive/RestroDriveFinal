package com.android.mno.restrodrive.View.View;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.mno.restrodrive.R;
import com.android.mno.restrodrive.View.Callbacks.LoginEventListener;
import com.android.mno.restrodrive.View.Utility.FirebaseLogin;
import com.android.mno.restrodrive.View.Utility.Utility;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SignUpFragment extends Fragment {

    private final String TAG = "SignUpFragment";
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

        fragmentView = inflater.inflate(R.layout.sign_up_fragment, container, false);

        Button emailSignUpButton = fragmentView.findViewById(R.id.emailSignUpButton);
        emailSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateLogUpDetails();

            }
        });

        return fragmentView;
    }

    /**
     * Validates Login details for sign up
     */
    private void validateLogUpDetails(){

        String userName = ((EditText)fragmentView.findViewById(R.id.tvUserName)).getText().toString().trim();

        if(Utility.getInstance().isValidUserName(userName)) {

            String email = ((EditText)fragmentView.findViewById(R.id.tvEmail)).getText().toString().trim();

            if (Utility.getInstance().isEmailValid(email)) {

                String password = ((EditText)fragmentView.findViewById(R.id.tvPassword)).getText().toString().trim();

                if (Utility.getInstance().isPasswordValid(password)) {

                    Utility.getInstance().hideSoftKeyboard(getActivity());

                    FirebaseLogin firebaseLogin = new FirebaseLogin(getContext(), loginEventListener);
                    firebaseLogin.signUp(email, password, userName);

                } else {
                    Utility.getInstance().hideSoftKeyboard(getActivity());
                    Utility.getInstance().showSnackbar(fragmentView, getResources().getString(R.string.enter_valid_password), getContext());
                }
            } else {
                Utility.getInstance().hideSoftKeyboard(getActivity());
                Utility.getInstance().showSnackbar(fragmentView, getResources().getString(R.string.enter_valid_email), getContext());
            }
        }else{
            Utility.getInstance().hideSoftKeyboard(getActivity());
            Utility.getInstance().showSnackbar(fragmentView, getResources().getString(R.string.enter_valid_user_name), getContext());
        }
    }
}
