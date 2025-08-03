package com.example.safekids.adapters;

import android.content.Context;
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
    private Context context;

    public FamilyAdapter(Context context, List<Family> familyList)
    {
        this.context = context;
        this.familyList = familyList;
    }

    @NonNull
    @Override
    public FamilyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_family, parent, false);
        return new FamilyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FamilyViewHolder holder, int position) {
        Family member = familyList.get(position);

        holder.tvFamilyName.setText(member.getFullName());
        holder.tvRelationship.setText(member.getRelationship());
        holder.tvPhone.setText(member.getPhone());

        // Por ahora usamos imagen por defecto
        holder.imgFamily.setImageResource(R.drawable.iconosafekids);
    }

    @Override
    public int getItemCount()
    {
        return familyList.size();
    }

    public void updateData(List<Family> newList) {
        familyList.clear();
        familyList.addAll(newList);
        notifyDataSetChanged();
    }

    static class FamilyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imgFamily;
        TextView tvFamilyName, tvRelationship, tvPhone;

        FamilyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFamily = itemView.findViewById(R.id.imgFamily);
            tvFamilyName = itemView.findViewById(R.id.tvNameFamily);
            tvRelationship = itemView.findViewById(R.id.tvRelationshipFamily);
            tvPhone = itemView.findViewById(R.id.tvPhoneNumberFamily);
        }
    }
}