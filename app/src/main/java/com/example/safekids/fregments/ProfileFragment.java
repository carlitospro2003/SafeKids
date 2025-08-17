package com.example.safekids.fregments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.safekids.EditProfileActivity;
import com.example.safekids.LoginActivity;
import com.example.safekids.R;
import com.example.safekids.network.ApiClient;
import com.example.safekids.network.ApiService;
import com.example.safekids.network.GuardianResponse;
import com.example.safekids.storage.ExtraDataManager;
import com.example.safekids.storage.SessionManager;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private TextView nameGuardian, lastnameGuardian, emailGuardian, passwordGuardian, phoneGuardian;
    private Button buttonLogout, btnEditProfile;
    private CircleImageView imgProfile;
    private SessionManager sessionManager;
    private ExtraDataManager extraDataManager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        sessionManager = new SessionManager(requireContext());
        extraDataManager = new ExtraDataManager(requireContext());
        //Referencias a los textView
        nameGuardian = view.findViewById(R.id.nameGuardian);
        lastnameGuardian = view.findViewById(R.id.lastnameGuardian);
        emailGuardian = view.findViewById(R.id.emailGuardian);
        passwordGuardian = view.findViewById(R.id.passwordGuardian);
        phoneGuardian = view.findViewById(R.id.phoneGuardian);
        buttonLogout = view.findViewById(R.id.buttomLogoutProfile);
        btnEditProfile = view.findViewById(R.id.buttomEditProfile);
        imgProfile = view.findViewById(R.id.imgProfile);

        btnEditProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EditProfileActivity.class);
            startActivity(intent);
        });


        // Obtener los datos del usuario
        GuardianResponse.Guardian guardian = sessionManager.getGuardian();

        if (guardian != null) {
            nameGuardian.setText(guardian.getFirstName());
            lastnameGuardian.setText(guardian.getLastName());
            emailGuardian.setText(guardian.getEmail());
            phoneGuardian.setText(guardian.getPhone());

            // Cargar imagen del guardián
            String photo = guardian.getPhoto();
            int schoolId = extraDataManager.getSchoolId();
            if (photo != null && !photo.isEmpty() && schoolId != -1) {
                String imageUrl = "https://apidev.safekids.site/imagenes/" + schoolId + "/GUARDIANS/" + photo;
                Log.d("ProfileFragment", "Loading image URL: " + imageUrl);
                Glide.with(requireContext())
                        .load(imageUrl)
                        .placeholder(R.drawable.iconosafekids)
                        .error(R.drawable.iconosafekids)
                        .into(imgProfile);
            } else {
                Log.d("ProfileFragment", "Photo or schoolId invalid: photo=" + photo + ", schoolId=" + schoolId);
                imgProfile.setImageResource(R.drawable.iconosafekids);
            }
        } else {
            Log.e("ProfileFragment", "Guardian is null");
        }

        // Acción de cerrar sesión
        buttonLogout.setOnClickListener(v -> {
            String token = sessionManager.getToken();

            if (token != null && !token.isEmpty()) {
                ApiService apiService = ApiClient.getApiService();

                Call<GuardianResponse> call = apiService.logoutGuardian("Bearer " + token);
                call.enqueue(new Callback<GuardianResponse>() {
                    @Override
                    public void onResponse(Call<GuardianResponse> call, Response<GuardianResponse> response) {
                        sessionManager.clearSession();
                        goToLogin();
                    }
                    @Override
                    public void onFailure(Call<GuardianResponse> call,Throwable t) {
                        sessionManager.clearSession();
                        goToLogin();
                    }
                });
            }else {
                sessionManager.clearSession();
                goToLogin();
            }
        });
        return view;

    }
    private void goToLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}