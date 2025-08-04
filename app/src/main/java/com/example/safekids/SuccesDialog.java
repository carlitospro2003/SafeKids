package com.example.safekids;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.example.safekids.R;
public class SuccesDialog {

    private Dialog dialog;

    public void show(Context context, String message) {
        // Crear el diálogo
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_success, null);
        dialog.setContentView(view);
        dialog.setCancelable(false); // para que no se cierre al tocar fuera
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Cambiar el mensaje dinámicamente si quieres
        android.widget.TextView tvMessage = view.findViewById(R.id.tvMessageSucces);
        tvMessage.setText(message);

        dialog.show();

        // Cerrar automáticamente después de 2 segundos
        new Handler().postDelayed(() -> {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }, 2000);
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
