package com.android.mno.restrodrive.View.Utility;

import android.app.ProgressDialog;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Common class for showing ProgressDialog
 */
public class RestroProgressDialog {

    private ProgressDialog mProgressDialog;
    private static RestroProgressDialog progressDialogInstance = null;

    public static RestroProgressDialog getInstance() {
        if (progressDialogInstance == null)
            progressDialogInstance = new RestroProgressDialog();

        return progressDialogInstance;
    }

    public void showProgressDialog(Context context) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
