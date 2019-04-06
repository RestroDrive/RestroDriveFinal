package com.android.mno.restrodrive.restrodrive.Helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.android.mno.restrodrive.R;
import com.android.mno.restrodrive.restrodrive.Callbacks.ILoginEventListener;
import com.android.mno.restrodrive.restrodrive.Model.UserDetails;
import com.android.mno.restrodrive.restrodrive.Utility.Constants;
import com.android.mno.restrodrive.restrodrive.Utility.PrefManager;
import com.android.mno.restrodrive.restrodrive.Utility.ProgressDialog;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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
    private ILoginEventListener loginEventListener;

    public FirebaseLogin(Context context) {
        this.context = context;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    public void setLoginEventListener(ILoginEventListener loginEventListener){
        this.loginEventListener = loginEventListener;
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Sign Up Success")
                                    .setMessage("Sign up successful. Please click ok to continue")
                                    .setCancelable(false)
                                    .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            loginEventListener.onLoginSuccess(task.isSuccessful());
                                        }
                                    });
                            builder.create().show();
                        } else {
                            Toast.makeText(context, "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();

                            Log.e(TAG, task.getException().getMessage());
                            loginEventListener.onLoginError(task.getException().getMessage());
                        }
                    }
                });
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            UserDetails userDetails = new UserDetails(user.getDisplayName(),user.getEmail());
                            onUpdateUserData(user.getUid(), userDetails);
                            loginEventListener.onLoginSuccess(task.isSuccessful());
                        } else {
                            Log.e(TAG, task.getException().getMessage());
                            loginEventListener.onLoginError(task.getException().getMessage());
                        }
                    }
                });
    }

    private void onSignUpSuccess(FirebaseUser user, String userName) {

        UserDetails userDetails = new UserDetails(userName, user.getEmail());
        onUpdateUserData(user.getUid(), userDetails);
    }

    /**
     * Updates user data in firebase
     * @param Uid
     * @param userDetails
     */
    public void onUpdateUserData(String Uid, UserDetails userDetails){
        mDatabase.child("users").child(Uid).setValue(userDetails);
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();

        PrefManager prefManager = new PrefManager(context, Constants.LOGIN_SHARED_PREF_KEY);
        prefManager.removeAuthLogin();
        onDestroyCall();
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
