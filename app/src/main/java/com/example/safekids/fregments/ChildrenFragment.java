package com.example.safekids.fregments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.safekids.R;
import com.example.safekids.adapters.ChildrenAdapter;
import com.example.safekids.models.Children;
import com.example.safekids.storage.ExtraDataManager;
import com.example.safekids.storage.SessionManager;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChildrenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChildrenFragment extends Fragment {


    private RecyclerView recyclerView;
    private ChildrenAdapter adapter;
    private List<Children> childrenList;

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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this zfragment
        View view = inflater.inflate(R.layout.fragment_children, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewChildren);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 🔹 Cargar lista real desde SessionManager
        SessionManager sessionManager = new SessionManager(getContext());
        ExtraDataManager extraDataManager = new ExtraDataManager(getContext());

        TextView textViewUser = view.findViewById(R.id.textViewUser);
        CircleImageView imgPerfil = view.findViewById(R.id.imgPerfil);

        if (sessionManager.getGuardian() != null) {
            String nombre = sessionManager.getGuardian().getFirstName();
            textViewUser.setText(nombre != null ? nombre : "Usuario");

            // Cargar la imagen del tutor con Glide usando school_id
            int schoolId = extraDataManager.getSchoolId();
            String guardianPhoto = sessionManager.getGuardian().getPhoto();
            if (schoolId != -1 && guardianPhoto != null) {
                String imageUrl = "https://apidev.safekids.site/imagenes/" + schoolId + "/GUARDIANS/" + guardianPhoto;
                Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.iconosafekids)
                        .error(R.drawable.iconosafekids)
                        .into(imgPerfil);
            } else {
                // Cargar imagen por defecto si no hay datos
                imgPerfil.setImageResource(R.drawable.iconosafekids);
            }
        } else {
            textViewUser.setText("Usuario");
            imgPerfil.setImageResource(R.drawable.iconosafekids);
        }
        childrenList = sessionManager.getStudents();

        adapter = new ChildrenAdapter(getContext(), childrenList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}