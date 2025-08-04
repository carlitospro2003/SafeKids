package com.example.safekids.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.safekids.DeleteDialog;
import com.example.safekids.EditFamilyActivity;
import com.example.safekids.R;
import com.example.safekids.network.DeleteFamilyResponse;
import com.example.safekids.models.Family;
import com.example.safekids.network.ApiClient;
import com.example.safekids.network.ApiService;
import com.example.safekids.storage.SessionManager;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.FamilyViewHolder> {

    private final List<Family> familyList;
    private final Context context;

    private final ApiService apiService;
    private final SessionManager sessionManager;

    public FamilyAdapter(Context context, List<Family> familyList)
    {
        this.context = context;
        this.familyList = familyList;
        this.apiService = ApiClient.getApiService();
        this.sessionManager = new SessionManager(context);
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

        // Evento botón Editar
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditFamilyActivity.class);
            intent.putExtra("family", member); // Family implementa Serializable
            context.startActivity(intent);
        });

        // Botón Eliminar (Soft Delete)
        holder.btnDelete.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition == RecyclerView.NO_POSITION) return;

            Family currentMember = familyList.get(currentPosition);

            DeleteDialog deleteDialog = new DeleteDialog();
            deleteDialog.show(context, currentMember, () -> {
                String token = "Bearer " + sessionManager.getToken();

                apiService.deleteFamily(token, currentMember.getId())
                        .enqueue(new Callback<DeleteFamilyResponse>() {
                            @Override
                            public void onResponse(Call<DeleteFamilyResponse> call, Response<DeleteFamilyResponse> response) {
                                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                    // 🔹 Quitar visualmente de la lista
                                    familyList.remove(currentPosition);
                                    notifyItemRemoved(currentPosition);
                                } else {
                                    Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<DeleteFamilyResponse> call, Throwable t) {
                                Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            });
        });

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
        Button btnEdit, btnDelete;


        FamilyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFamily = itemView.findViewById(R.id.imgFamily);
            tvFamilyName = itemView.findViewById(R.id.tvNameFamily);
            tvRelationship = itemView.findViewById(R.id.tvRelationshipFamily);
            tvPhone = itemView.findViewById(R.id.tvPhoneNumberFamily);
            btnEdit = itemView.findViewById(R.id.btnEditFamily);
            btnDelete = itemView.findViewById(R.id.btnDeleteFamily);
        }
    }
}