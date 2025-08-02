package com.example.safekids.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safekids.R;
import com.example.safekids.models.Tutor;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TutorsAdapter extends RecyclerView.Adapter<TutorsAdapter.TutorViewHolder> {

    private List<Tutor> tutorList;

    public TutorsAdapter(List<Tutor> tutorList) {
        this.tutorList = tutorList;
    }

    @NonNull
    @Override
    public TutorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tutors, parent, false);
        return new TutorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorViewHolder holder, int position) {
        Tutor tutor = tutorList.get(position);

        holder.tvName.setText(tutor.getName());
        holder.tvPhone.setText(tutor.getPhone());
        holder.tvEmail.setText(tutor.getEmail());
        holder.imgTutor.setImageResource(tutor.getImageRes());
    }

    @Override
    public int getItemCount() {
        return tutorList.size();
    }

    public static class TutorViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvPhone, tvEmail;
        CircleImageView imgTutor;

        public TutorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameTutor);
            tvPhone = itemView.findViewById(R.id.tvPhoneTutor);
            tvEmail = itemView.findViewById(R.id.tvEmailTutor);
            imgTutor = itemView.findViewById(R.id.imgTutor);
        }
    }
}
