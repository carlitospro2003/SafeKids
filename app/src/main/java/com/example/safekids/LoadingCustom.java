package com.example.safekids;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.content.Context;

public class LoadingCustom {
    private Activity activity;
    private AlertDialog dialog;

    LoadingCustom(Activity myActivity) {
        this.activity = myActivity;
    }

    void startLoadingCustom() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_progessbar, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }

    void dismissLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
