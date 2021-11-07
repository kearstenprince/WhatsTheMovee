package com.example.whatsthemove.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.example.whatsthemove.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DialogUtil {

    private static Dialog simpleProgressDialog = null;
    public static void showSimpleProgressDialog(Context context) {
        if (simpleProgressDialog != null) {
            closeProgressDialog();
        }

        if (context != null) {
            simpleProgressDialog = new Dialog(context);
            simpleProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            simpleProgressDialog.setContentView(R.layout.dialog_progress_simple);
            setDialogOpacity(simpleProgressDialog, Color.WHITE, 0);
            simpleProgressDialog.setCancelable(false);
            simpleProgressDialog.show();
        }
    }

    public static void closeProgressDialog() {

        if (simpleProgressDialog != null) {
            try {
                simpleProgressDialog.cancel();
                simpleProgressDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isProgressShowing() {
        return (simpleProgressDialog != null && simpleProgressDialog.isShowing());
    }

    public static void setDialogOpacity(Dialog dialog, int bgColor, int alpha) {
        ColorDrawable bgDrawable = new ColorDrawable(bgColor);
        bgDrawable.setAlpha(alpha);
        dialog.getWindow().setBackgroundDrawable(bgDrawable);
    }

    public static void showAlertDialog(@NonNull Context context, @NonNull String title, @NonNull String message, String positiveBtnMessage, DialogInterface.OnClickListener postiveBtnClickListener){
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveBtnMessage, postiveBtnClickListener)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        materialAlertDialogBuilder.create().show();
    }

    public static void showSimpleAlertDialog(Context context, String title, @StringRes  int stringId) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(stringId)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }

    public static void showSimpleAlertDialog(Context context, String title, String message) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create().show();
    }

}
