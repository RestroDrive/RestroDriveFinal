package com.android.mno.restrodrive.View.Utility;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.mno.restrodrive.View.Callbacks.LoginEventListener;
import com.android.mno.restrodrive.View.Model.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;

/**
 * Class for firebase login methods
 */
public class FirebaseLogin {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private final String TAG = "FirebaseLogin";
    private Context context;
    private LoginEventListener loginEventListener;

    public FirebaseLogin(Context context, LoginEventListener loginEventListener) {
        this.context = context;
        this.loginEventListener = loginEventListener;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Firebase sign in method
     * @param email
     * @param password
     */
    public void signIn(String email, String password) {
        Log.d(TAG, "signIn "+ email);

        ProgressDialog.getInstance().showProgressDialog(context);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                        ProgressDialog.getInstance().hideProgressDialog();

                        if (task.isSuccessful()) {
                            //onAuthSuccess(task.getResult().getUser());

                            Log.e(TAG, "Success");
                            loginEventListener.onLoginSuccess(task.isSuccessful());
                        } else {
                            Toast.makeText(context, "Sign In Failed",
                                    Toast.LENGTH_SHORT).show();

                            Log.e(TAG, task.getException().getMessage());
                            loginEventListener.onLoginError(task.getException().getMessage());
                        }
                    }
                });
    }

    /**
     * Firebase sign up method
     * @param email
     * @param password
     * @param userName
     */
    public void signUp(String email, String password, final String userName) {
        Log.d(TAG, "signUp "+email);

        ProgressDialog.getInstance().showProgressDialog(context);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                        ProgressDialog.getInstance().hideProgressDialog();

                        if (task.isSuccessful()) {
                            onSignUpSuccess(task.getResult().getUser(), userName);

                            Log.e(TAG, "Success");
                            loginEventListener.onLoginSuccess(true);
                        } else {
                            Toast.makeText(context, "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();

                            Log.e(TAG, task.getException().getMessage());
                            loginEventListener.onLoginError(task.getException().getMessage());
                        }
                    }
                });
    }

    /**
     * Method call on sing up success for storing user details
     * @param user
     * @param userName
     */
    private void onSignUpSuccess(FirebaseUser user, String userName) {

        UserDetails userDetails = new UserDetails(userName, user.getEmail());
        mDatabase.child("users").child(user.getUid()).setValue(userDetails);
    }

    /**
     * Call method on Activity destroyed
     * initiates firebase objects to null
     */
    public void onDestroyCall() {

        if (mAuth != null) {
            mAuth = null;
        }

        if (mDatabase != null) {
            mDatabase = null;
        }
    }

}
