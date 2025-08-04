package com.example.safekids;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.safekids.models.Family;

public class DeleteDialog {

    private Dialog dialog;

    public interface OnDeleteConfirmListener {
        void onConfirmDelete();
    }

    public void show(Context context, Family family, OnDeleteConfirmListener listener) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.alert_delete, null);
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        // Setear datos del familiar
        TextView tvName = view.findViewById(R.id.textViewNameAuthorized);
        TextView tvPhone = view.findViewById(R.id.textViewPhoneAuthorized);
        TextView tvRelationship = view.findViewById(R.id.textViewAuthorizedRelatioship);

        tvName.setText(family.getFullName());
        tvPhone.setText(family.getPhone());
        tvRelationship.setText(family.getRelationship());

        // Botones
        Button btnConfirm = view.findViewById(R.id.buttonConfirmDelete);
        Button btnCancel = view.findViewById(R.id.buttonCancel);

        btnConfirm.setOnClickListener(v -> {
            if (listener != null) listener.onConfirmDelete();
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
