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
import com.example.safekids.adapters.FamilyAdapter;
import com.example.safekids.models.Children;
import com.example.safekids.network.AuthorizedResponse;

import com.example.safekids.models.Tutor;
import com.example.safekids.models.Family;
import com.example.safekids.network.ApiClient;
import com.example.safekids.network.ApiService;
import com.example.safekids.storage.SessionManager;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TutorsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TutorsFragment extends Fragment {

    private RecyclerView recyclerViewFamily;
    private FamilyAdapter familyAdapter;
    private List<Family> familyList = new ArrayList<>();
    private ApiService apiService;
    private SessionManager sessionManager;


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
        recyclerViewFamily = view.findViewById(R.id.recyclerViewFamily);
        recyclerViewFamily.setLayoutManager(new LinearLayoutManager(getContext()));

        familyAdapter = new FamilyAdapter(getContext(), familyList);
        recyclerViewFamily.setAdapter(familyAdapter);

        sessionManager = new SessionManager(getContext());
        apiService = ApiClient.getApiService();

        loadAllAuthorizedPeoples();

        return view;
    }

    private void loadAllAuthorizedPeoples() {

        String token = "Bearer " + sessionManager.getToken();
        List<Children> students = sessionManager.getStudents();

        // Limpiamos lista
        familyList.clear();

        for (Children student : students) {
            int studentId = student.getId();
            apiService.getAuthorizedPeoples(token, studentId)
                    .enqueue(new Callback<AuthorizedResponse>() {
                        @Override
                        public void onResponse(Call<AuthorizedResponse> call, Response<AuthorizedResponse> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                                List<Family> authorized = response.body().getData();
                                familyList.addAll(authorized);
                                familyAdapter.notifyDataSetChanged();
                            } else {
                                Log.e("TutorFragment", "Error: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<AuthorizedResponse> call, Throwable t) {
                            Log.e("TutorFragment", "Failure: " + t.getMessage());
                        }
                    });
        }
    }
}