package com.android.mno.restrodrive.View.Adapters;

import com.android.mno.restrodrive.View.View.SignInFragment;
import com.android.mno.restrodrive.View.View.SignUpFragment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Pager adapter for login screen
 */
public class LoginPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public LoginPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                SignInFragment signInFragment = new SignInFragment();
                return signInFragment;
            case 1:
                SignUpFragment signUpFragment = new SignUpFragment();
                return signUpFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
