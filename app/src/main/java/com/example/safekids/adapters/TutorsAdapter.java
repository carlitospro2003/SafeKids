package com.example.safekids.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.safekids.R;
import com.example.safekids.models.Tutor;
import com.example.safekids.storage.ExtraDataManager;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TutorsAdapter extends RecyclerView.Adapter<TutorsAdapter.TutorViewHolder> {

    private List<Tutor> tutorList;
    private final ExtraDataManager extraDataManager;

    public TutorsAdapter(Context context, List<Tutor> tutorList) {
        this.tutorList = tutorList;
        this.extraDataManager = new ExtraDataManager(context);
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
        holder.tvName.setText(tutor.getFullName());
        holder.tvPhone.setText(tutor.getPhone());
        holder.tvEmail.setText(tutor.getEmail());

        // Cargar imagen del tutor
        String photo = tutor.getPhoto();
        int schoolId = extraDataManager.getSchoolId();
        if (schoolId != -1 && photo != null) {
            String imageUrl = "https://apidev.safekids.site/imagenes/" + schoolId + "/GUARDIANS/" + photo;
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.iconosafekids)
                    .error(R.drawable.iconosafekids)
                    .into(holder.imgTutor);
        } else {
            holder.imgTutor.setImageResource(R.drawable.iconosafekids);
        }
        // Usar un recurso local por ahora; más adelante usarás Glide con tutor.getPhoto()
    }

    @Override
    public int getItemCount() {
        return tutorList.size();
    }

    public void updateList(List<Tutor> newList) {
        tutorList = newList;
        notifyDataSetChanged();
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
