package com.example.safekids.fregments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.safekids.R;
import com.example.safekids.adapters.ChildrenAdapter;
import com.example.safekids.models.Children;
import com.example.safekids.network.ApiClient;
import com.example.safekids.network.ApiService;
import com.example.safekids.network.MyKidsResponse;
import com.example.safekids.storage.ExtraDataManager;
import com.example.safekids.storage.SessionManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChildrenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChildrenFragment extends Fragment {


    private RecyclerView recyclerView;
    private ChildrenAdapter adapter;
    private List<Children> childrenList = new ArrayList<>();
    private Set<Integer> childrenIds = new HashSet<>();
    private SessionManager sessionManager;
    private ExtraDataManager extraDataManager;
    private ApiService apiService;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChildrenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChildrenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChildrenFragment newInstance(String param1, String param2) {
        ChildrenFragment fragment = new ChildrenFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        sessionManager = new SessionManager(requireContext());
        extraDataManager = new ExtraDataManager(requireContext());
        apiService = ApiClient.getApiService();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this zfragment
        View view = inflater.inflate(R.layout.fragment_children, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewChildren);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        TextView textViewUser = view.findViewById(R.id.textViewUser);
        CircleImageView imgPerfil = view.findViewById(R.id.imgPerfil);

        adapter = new ChildrenAdapter(getContext(), childrenList);
        recyclerView.setAdapter(adapter);
        // 🔹 Cargar lista real desde SessionManager
        SessionManager sessionManager = new SessionManager(getContext());
        ExtraDataManager extraDataManager = new ExtraDataManager(getContext());

        // Load guardian info
        // Cargar información del tutor
        if (sessionManager.getGuardian() != null) {
            String nombre = sessionManager.getGuardian().getFirstName();
            textViewUser.setText(nombre != null ? nombre : "Usuario");

            int schoolId = extraDataManager.getSchoolId();
            String guardianPhoto = sessionManager.getGuardian().getPhoto();
            if (schoolId != -1 && guardianPhoto != null && !guardianPhoto.isEmpty()) {
                String imageUrl = "https://apidev.safekids.site/imagenes/" + schoolId + "/GUARDIANS/" + guardianPhoto;
                Log.d("ChildrenFragment", "Loading guardian image URL: " + imageUrl);
                Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.iconosafekids)
                        .error(R.drawable.iconosafekids)
                        .into(imgPerfil);
            } else {
                Log.d("ChildrenFragment", "Invalid guardian photo or schoolId: photo=" + guardianPhoto + ", schoolId=" + schoolId);
                imgPerfil.setImageResource(R.drawable.iconosafekids);
            }
        } else {
            Log.e("ChildrenFragment", "Guardian is null");
            textViewUser.setText("Usuario");
            imgPerfil.setImageResource(R.drawable.iconosafekids);
        }

        // Load children from API
        loadMyKids();



        return view;
    }

    private void loadMyKids() {
        String token = sessionManager.getToken();
        if (token == null || token.isEmpty()) {
            Log.e("ChildrenFragment", "Token is null or empty");
            if (isAdded()) {
                Toast.makeText(requireContext(), "Sesión no iniciada", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        // Limpiar listas y set para evitar duplicados
        childrenList.clear();
        childrenIds.clear();
        adapter.notifyDataSetChanged();

        apiService.getMyKids("Bearer " + token).enqueue(new Callback<MyKidsResponse>() {
            @Override
            public void onResponse(Call<MyKidsResponse> call, Response<MyKidsResponse> response) {
                if (!isAdded()) {
                    Log.w("ChildrenFragment", "Fragment not attached, ignoring response");
                    return;
                }
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    List<Children> students = response.body().getData();
                    List<String> childrenPhotos = new ArrayList<>();

                    for (Children child : students) {
                        if (child.isStatus() && !childrenIds.contains(child.getId())) {
                            childrenList.add(child);
                            childrenIds.add(child.getId());
                            if (child.getPhoto() != null && !child.getPhoto().isEmpty()) {
                                childrenPhotos.add(child.getPhoto());
                            }
                            Log.d("ChildrenFragment", "Added child ID: " + child.getId() + ", Name: " + child.getFullName());
                        }
                    }

                    // Guardar en SessionManager y ExtraDataManager
                    sessionManager.saveStudents(childrenList);
                    extraDataManager.saveChildrenPhotos(childrenPhotos);

                    // Actualizar adaptador
                    adapter.notifyDataSetChanged();
                    Log.d("ChildrenFragment", "Total children: " + childrenList.size());
                } else {
                    Log.e("ChildrenFragment", "Error fetching students: " + response.message());
                    if (isAdded()) {
                        Toast.makeText(requireContext(), "Error al cargar estudiantes", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyKidsResponse> call, Throwable t) {
                if (!isAdded()) {
                    Log.w("ChildrenFragment", "Fragment not attached, ignoring failure");
                    return;
                }
                Log.e("ChildrenFragment", "API call failed: " + t.getMessage(), t);
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}