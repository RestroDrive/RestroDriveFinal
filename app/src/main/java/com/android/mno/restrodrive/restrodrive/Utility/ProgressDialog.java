package com.android.mno.restrodrive.restrodrive.Utility;

import android.content.Context;

/**
 * Common class for showing ProgressDialog
 */
public class ProgressDialog {

    private android.app.ProgressDialog mProgressDialog;
    private static ProgressDialog progressDialogInstance = null;

    public static ProgressDialog getInstance() {
        if (progressDialogInstance == null)
            progressDialogInstance = new ProgressDialog();

        return progressDialogInstance;
    }

    public void showProgressDialog(Context context) {
        if (mProgressDialog == null) {
            mProgressDialog = new android.app.ProgressDialog(context);
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
