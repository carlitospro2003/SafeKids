package com.example.safekids;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class PhotoErrorDialog {
    private final Dialog dialog;
    private final TextView textViewDescription;
    private final Button buttonTryAgain, buttonCancel;

    public PhotoErrorDialog(Context context, String message, View.OnClickListener retryListener, View.OnClickListener cancelListener) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.photo_error);
        dialog.setCancelable(false); // evita que se cierre al tocar fuera

        textViewDescription = dialog.findViewById(R.id.textViewErrorDescription);
        buttonTryAgain = dialog.findViewById(R.id.buttonTryAgain);
        buttonCancel = dialog.findViewById(R.id.buttonCancelPhoto);

        textViewDescription.setText(message);

        buttonTryAgain.setOnClickListener(v -> {
            dialog.dismiss();
            retryListener.onClick(v);
        });

        buttonCancel.setOnClickListener(v -> {
            dialog.dismiss();
            cancelListener.onClick(v);
        });
    }

    public void show() {
        dialog.show();
    }
}
