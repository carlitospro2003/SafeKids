package com.example.safekids.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safekids.R;
import com.example.safekids.models.Family;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.FamilyViewHolder> {

    private List<Family> familyList;

    public FamilyAdapter(List<Family> familyList) {
        this.familyList = familyList;
    }

    @NonNull
    @Override
    public FamilyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_family, parent, false);
        return new FamilyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FamilyViewHolder holder, int position) {
        Family family = familyList.get(position);

        holder.tvName.setText(family.getName());
        holder.tvPhone.setText(family.getPhone());
        holder.tvRelationship.setText(family.getRelationship());
        holder.img.setImageResource(family.getImageRes());

        // Puedes añadir aquí listeners para editar y eliminar
        holder.btnEdit.setOnClickListener(v -> {
            // Acción para editar
        });

        holder.btnDelete.setOnClickListener(v -> {
            // Acción para eliminar
        });
    }

    @Override
    public int getItemCount() {
        return familyList.size();
    }

    static class FamilyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvPhone, tvRelationship;
        CircleImageView img;
        Button btnEdit, btnDelete;

        public FamilyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameFamily);
            tvPhone = itemView.findViewById(R.id.tvPhoneNumberFamily);
            tvRelationship = itemView.findViewById(R.id.tvRelationshipFamily);
            img = itemView.findViewById(R.id.imgFamily);
            btnEdit = itemView.findViewById(R.id.btnEditFamily);
            btnDelete = itemView.findViewById(R.id.btnDeleteFamily);
        }
    }
}