package com.example.safekids.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safekids.ChildrenDetailActivity;
import com.example.safekids.R;
import com.example.safekids.models.Children;
import com.example.safekids.storage.SessionManager;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChildrenAdapter extends RecyclerView.Adapter<ChildrenAdapter.ChildrenViewHolder> {

    private List<Children> childrenList;
    private Context context;

    public ChildrenAdapter(Context context, List<Children> childrenList) {
        this.context = context;
        this.childrenList = childrenList;
    }

    @NonNull
    @Override
    public ChildrenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_children, parent, false);
        return new ChildrenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChildrenViewHolder holder, int position) {
        Children child = childrenList.get(position);

        // 🔹 Mostrar nombre completo
        holder.tvChildrenName.setText(child.getFullName());
        holder.tvDateOfBirth.setText(child.getBirthDate());

        // 🔹 Foto por defecto
        holder.imgChildren.setImageResource(R.drawable.iconosafekids);

        // 🔹 School ya no existe, dejamos vacío o un placeholder
        //holder.tvSchool.setText("Escuela no disponible");
        SessionManager sessionManager = new SessionManager(context);
        if (sessionManager.getSchool() != null) {
            holder.tvSchool.setText(sessionManager.getSchool().getName());
        } else {
            holder.tvSchool.setText("Escuela no disponible");
        }

        holder.btnShow.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChildrenDetailActivity.class);
            intent.putExtra("name", child.getFullName());
            intent.putExtra("birthDate", child.getBirthDate());
            intent.putExtra("photo", child.getPhoto());
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return childrenList.size();
    }

    public static class ChildrenViewHolder extends RecyclerView.ViewHolder {
        TextView tvChildrenName, tvDateOfBirth, tvSchool;
        CircleImageView imgChildren;
        Button btnShow;


        public ChildrenViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChildrenName = itemView.findViewById(R.id.tvNameChildren);
            tvDateOfBirth = itemView.findViewById(R.id.tvDatOfBirth);
            tvSchool = itemView.findViewById(R.id.tvSchoolChildren);
            imgChildren = itemView.findViewById(R.id.imgChildren);
            btnShow = itemView.findViewById(R.id.btnShowChildren);
        }
    }

}
