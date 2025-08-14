package com.example.safekids.fregments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.example.safekids.AddFamilyActivity;
import com.example.safekids.R;
import com.example.safekids.adapters.TutorsAdapter;
import com.example.safekids.models.Children;
import com.example.safekids.models.Tutor;
import com.example.safekids.adapters.FamilyAdapter;
import com.example.safekids.network.AuthorizedResponse;

import com.example.safekids.models.Family;
import com.example.safekids.network.ApiClient;
import com.example.safekids.network.ApiService;
import com.example.safekids.network.GuardiansListResponse;
import com.example.safekids.network.GuardiansResponse;
import com.example.safekids.storage.ExtraDataManager;
import com.example.safekids.storage.SessionManager;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TutorsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TutorsFragment extends Fragment {

    private RecyclerView recyclerViewTutor;

    private RecyclerView recyclerViewFamily;
    private TutorsAdapter tutorsAdapter;

    private FamilyAdapter familyAdapter;
    private List<Tutor> tutorList = new ArrayList<>();

    private List<Family> familyList = new ArrayList<>();
    private ApiService apiService;
    private SessionManager sessionManager;
    private ExtraDataManager extraDataManager;
    private Set<Integer> tutorIds = new HashSet<>(); // Mover fuera del bucle
    private Set<Integer> familyIds = new HashSet<>(); // Mover fuera del bucle



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TutorsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TutorsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TutorsFragment newInstance(String param1, String param2) {
        TutorsFragment fragment = new TutorsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tutors, container, false);

        ImageView addNewFamilyBtn = view.findViewById(R.id.addNewFamily);
        addNewFamilyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddFamilyActivity.class);
                startActivity(intent);
            }
        });

        // Configurar RecyclerView para tutores
        recyclerViewTutor = view.findViewById(R.id.recyclerViewTutor);
        recyclerViewTutor.setLayoutManager(new LinearLayoutManager(getContext()));
        tutorsAdapter = new TutorsAdapter(getContext(), tutorList);
        recyclerViewTutor.setAdapter(tutorsAdapter);

        recyclerViewFamily = view.findViewById(R.id.recyclerViewFamily);
        recyclerViewFamily.setLayoutManager(new LinearLayoutManager(getContext()));

        familyAdapter = new FamilyAdapter(getContext(), familyList);
        recyclerViewFamily.setAdapter(familyAdapter);


        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Limpiar listas y sets para evitar duplicados
        tutorList.clear();
        familyList.clear();
        tutorIds.clear();
        familyIds.clear();
        loadAllGuardians();
        loadAllAuthorizedPeoples();
    }

    private void loadAllGuardians() {
        String token = sessionManager.getToken();
        if (token == null) {
            Log.e("TutorsFragment", "Token is null");
            if (isAdded()) {
                // Toast.makeText(requireContext(), "Sesión no iniciada", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        token = "Bearer " + token;
        List<Children> students = sessionManager.getStudents();
        List<String> tutorImgRoutes = new ArrayList<>();

        if (students == null || students.isEmpty()) {
            Log.w("TutorsFragment", "No students found");
            return;
        }

        for (Children student : students) {
            int studentId = student.getId();
            apiService.getGuardians(token, studentId).enqueue(new Callback<GuardiansResponse>() {
                @Override
                public void onResponse(Call<GuardiansResponse> call, Response<GuardiansResponse> response) {
                    if (!isAdded()) {
                        Log.w("TutorsFragment", "Fragment not attached, ignoring response");
                        return;
                    }
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        List<Tutor> guardians = response.body().getData();
                        for (Tutor tutor : guardians) {
                            if (tutor.isStatus() && !tutorIds.contains(tutor.getId())) {
                                tutorList.add(tutor);
                                tutorIds.add(tutor.getId());
                                tutorImgRoutes.add(tutor.getImgRoute());
                                Log.d("TutorsFragment", "Added tutor ID: " + tutor.getId() + ", Name: " + tutor.getFirstName() + ", Total tutors: " + tutorList.size());
                            }
                        }
                        tutorsAdapter.updateList(tutorList);
                        extraDataManager.saveTutorImgRoutes(tutorImgRoutes);
                    } else {
                        Log.e("TutorsFragment", "Error al cargar tutores: " + response.message());
                        if (isAdded()) {
                            // Toast.makeText(requireContext(), "Error al cargar tutores", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GuardiansResponse> call, Throwable t) {
                    if (!isAdded()) {
                        Log.w("TutorsFragment", "Fragment not attached, ignoring failure");
                        return;
                    }
                    Log.e("TutorsFragment", "Fallo en la conexión: " + t.getMessage(), t);
                    if (isAdded()) {
                        // Toast.makeText(requireContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void loadAllAuthorizedPeoples() {

        String token = sessionManager.getToken();
        if (token == null) {
            Log.e("TutorsFragment", "Token is null");
            if (isAdded()) {
                // Toast.makeText(requireContext(), "Sesión no iniciada", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        token = "Bearer " + token;
        List<Children> students = sessionManager.getStudents();

        if (students == null || students.isEmpty()) {
            Log.w("TutorsFragment", "No students found");
            return;
        }

        for (Children student : students) {
            int studentId = student.getId();
            apiService.getAuthorizedPeoples(token, studentId).enqueue(new Callback<AuthorizedResponse>() {
                @Override
                public void onResponse(Call<AuthorizedResponse> call, Response<AuthorizedResponse> response) {
                    if (!isAdded()) {
                        Log.w("TutorsFragment", "Fragment not attached, ignoring response");
                        return;
                    }
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        List<Family> authorized = response.body().getData();
                        for (Family fam : authorized) {
                            if (fam.isStatus() && !familyIds.contains(fam.getId())) {
                                familyList.add(fam);
                                familyIds.add(fam.getId());
                                Log.d("TutorsFragment", "Added family ID: " + fam.getId() + ", Name: " + fam.getFirstName() + ", Total family: " + familyList.size());
                            }
                        }
                        familyAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("TutorsFragment", "Error: " + response.message());
                        if (isAdded()) {
                            // Toast.makeText(requireContext(), "Error al cargar responsables", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AuthorizedResponse> call, Throwable t) {
                    if (!isAdded()) {
                        Log.w("TutorsFragment", "Fragment not attached, ignoring failure");
                        return;
                    }
                    Log.e("TutorsFragment", "Failure: " + t.getMessage(), t);
                    if (isAdded()) {
                        // Toast.makeText(requireContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tutorList.clear();
        familyList.clear();
        tutorIds.clear();
        familyIds.clear();
    }


}