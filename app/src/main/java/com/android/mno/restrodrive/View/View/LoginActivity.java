package com.android.mno.restrodrive.View.View;

import com.android.mno.restrodrive.R;
import com.android.mno.restrodrive.View.Adapters.LoginPagerAdapter;
import com.android.mno.restrodrive.View.Callbacks.LoginEventListener;
import com.android.mno.restrodrive.View.MapActivity.MapActivity;
import com.android.mno.restrodrive.View.Utility.FirebaseLogin;
import com.android.mno.restrodrive.View.Utility.Utility;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity implements LoginEventListener {

    private static final String TAG = "LoginActivity";
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initLoginPagerAdapter();
        checkSharedPrefAuth();
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
        Utility.getInstance().showSnackbar(parentLayout,"Success");

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.shared_pref_auth_key), successFlag);
        editor.apply();

        goToMapActivity();
    }

    @Override
    public void onLoginError(String errorMessage) {

        View parentLayout = findViewById(android.R.id.content);
        Utility.getInstance().showSnackbar(parentLayout,errorMessage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        FirebaseLogin firebaseLogin = new FirebaseLogin(this, this);
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

