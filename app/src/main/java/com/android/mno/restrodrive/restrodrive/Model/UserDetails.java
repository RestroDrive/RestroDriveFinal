package com.android.mno.restrodrive.restrodrive.Model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Common class for login user details
 */

@IgnoreExtraProperties
public class UserDetails {

    private String name;
    private String email;

    public UserDetails(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
