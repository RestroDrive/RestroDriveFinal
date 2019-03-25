package com.android.mno.restrodrive.View.View;

import com.android.mno.restrodrive.R;
import com.android.mno.restrodrive.View.Adapters.LoginPagerAdapter;
import com.android.mno.restrodrive.View.Callbacks.ILoginEventListener;
import com.android.mno.restrodrive.View.MapActivity.MapActivity;
import com.android.mno.restrodrive.View.Utility.Constants;
import com.android.mno.restrodrive.View.Utility.FirebaseLogin;
import com.android.mno.restrodrive.View.Utility.Utility;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class LoginActivity extends AppCompatActivity implements ILoginEventListener {

    private static final String TAG = "LoginActivity";
    private SharedPreferences sharedPref;
    private FirebaseLogin firebaseLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseLogin = new FirebaseLogin(this, this);

        initLoginPagerAdapter();
        checkSharedPrefAuth();

    }

    /*@Override
    protected void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            //goToMapActivity();
            Log.d(TAG, "123");
        }
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Constants.GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {

                Log.e(TAG, "1");
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.e(TAG, "2");
                firebaseLogin.firebaseAuthWithGoogle(account);
                Log.e(TAG, "3");
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, getString(R.string.google_sign_in_failed), e);

                View parentView = this.findViewById(android.R.id.content);
                Utility.getInstance().showSnackbar(parentView,getResources().getString(R.string.google_sign_in_failed),
                        this);
            }
        }
    }

    /**
     * Initialize the login pager adapter
     */
    private void initLoginPagerAdapter() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.sign_in_tab)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.sign_up_tab)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.pager);
        final LoginPagerAdapter adapter = new LoginPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /**
     * Check device already has auth token to login or not
     */
    private void checkSharedPrefAuth(){

        sharedPref = this.getSharedPreferences(
                getString(R.string.login_shared_pref_key), Context.MODE_PRIVATE);

        boolean auth = sharedPref.getBoolean(getString(R.string.shared_pref_auth_key), false);

        if(auth){
            goToMapActivity();
        }
    }

    @Override
    public void onLoginSuccess(boolean successFlag) {

        View parentLayout = findViewById(android.R.id.content);
        Utility.getInstance().showSnackbar(parentLayout,"Success", this);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.shared_pref_auth_key), successFlag);
        editor.apply();

        goToMapActivity();
    }

    @Override
    public void onLoginError(String errorMessage) {

        View parentLayout = findViewById(android.R.id.content);
        Utility.getInstance().showSnackbar(parentLayout, errorMessage, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        firebaseLogin = new FirebaseLogin(this, this);
        firebaseLogin.onDestroyCall();
    }

    /**
     * Intent to Map Activity
     */
    private void goToMapActivity(){
        if (Utility.getInstance().isMapServicesOk(this)){
            Intent intent = new Intent(LoginActivity.this, MapActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}

