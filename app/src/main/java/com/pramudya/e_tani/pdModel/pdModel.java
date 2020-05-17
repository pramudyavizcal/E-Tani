package com.pramudya.e_tani.pdModel;

import android.app.ProgressDialog;
import android.content.Context;

import com.pramudya.e_tani.R;



public class pdModel {

    private static ProgressDialog progressDialog;

    public static void pdData(Context context){
        progressDialog=new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
        progressDialog.setMessage("Mencari Petani Terdekat ...");
        progressDialog.setTitle("Silahkan Tunggu");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

    }

    public static void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
