package com.example.whatsthemove.utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class SnackBarUtils {

    public static void showSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
                .setAction("DISMISS", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
    }
}
