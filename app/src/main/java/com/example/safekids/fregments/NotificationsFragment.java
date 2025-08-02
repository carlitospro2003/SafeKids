package com.example.safekids.fregments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safekids.adapters.NotificationsAdapter;
import com.example.safekids.models.Notifications;

import java.util.ArrayList;
import java.util.List;

import com.example.safekids.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationsAdapter adapter;
    private List<Notifications> notificationList;
    private List<Notifications> fullNotificationList;

    private AutoCompleteTextView dropdownKids, dropdownDates;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
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

        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dropdownKids = view.findViewById(R.id.drop_children);
        dropdownDates = view.findViewById(R.id.dropdown_dates);

        // Opciones de dropdowns
        String[] kidsOptions = {"Carlos", "Lucía", "Mateo"};
        String[] dateOptions = {"Semana pasada", "Último mes"};

        ArrayAdapter<String> kidsAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, kidsOptions);
        dropdownKids.setAdapter(kidsAdapter);

        ArrayAdapter<String> datesAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, dateOptions);
        dropdownDates.setAdapter(datesAdapter);

        // Datos harcodeados
        notificationList = new ArrayList<>();
        notificationList.add(new Notifications("Tu hijo a llegado a la escuela", R.drawable.notifications));
        notificationList.add(new Notifications("Victor fue recogido por margarita", R.drawable.notifications));
        notificationList.add(new Notifications("Tu hijo entro a la esciela 8:03 AM", R.drawable.notifications));
        notificationList.add(new Notifications("STu hijo salio  a las 2:15 PM", R.drawable.notifications));
        notificationList.add(new Notifications("Tu hijo a llegado a la escuela", R.drawable.notifications));
        notificationList.add(new Notifications("Victor fue recogido por margarita", R.drawable.notifications));
        notificationList.add(new Notifications("Tu hijo entro a la esciela 8:03 AM", R.drawable.notifications));
        notificationList.add(new Notifications("STu hijo salio  a las 2:15 PM", R.drawable.notifications));
        notificationList.add(new Notifications("Tu hijo a llegado a la escuela", R.drawable.notifications));
        notificationList.add(new Notifications("Victor fue recogido por margarita", R.drawable.notifications));
        notificationList.add(new Notifications("Tu hijo entro a la esciela 8:03 AM", R.drawable.notifications));
        notificationList.add(new Notifications("STu hijo salio  a las 2:15 PM", R.drawable.notifications));
        notificationList.add(new Notifications("Tu hijo a llegado a la escuela", R.drawable.notifications));
        notificationList.add(new Notifications("Victor fue recogido por margarita", R.drawable.notifications));
        notificationList.add(new Notifications("Tu hijo entro a la esciela 8:03 AM", R.drawable.notifications));
        notificationList.add(new Notifications("STu hijo salio  a las 2:15 PM", R.drawable.notifications));
        notificationList.add(new Notifications("Tu hijo a llegado a la escuela", R.drawable.notifications));
        notificationList.add(new Notifications("Victor fue recogido por margarita", R.drawable.notifications));
        notificationList.add(new Notifications("Tu hijo entro a la esciela 8:03 AM", R.drawable.notifications));
        notificationList.add(new Notifications("STu hijo salio  a las 2:15 PM", R.drawable.notifications));
        notificationList.add(new Notifications("Tu hijo a llegado a la escuela", R.drawable.notifications));
        notificationList.add(new Notifications("Victor fue recogido por margarita", R.drawable.notifications));
        notificationList.add(new Notifications("Tu hijo entro a la esciela 8:03 AM", R.drawable.notifications));
        notificationList.add(new Notifications("STu hijo salio  a las 2:15 PM", R.drawable.notifications));
        notificationList.add(new Notifications("Tu hijo a llegado a la escuela", R.drawable.notifications));
        notificationList.add(new Notifications("Victor fue recogido por margarita", R.drawable.notifications));
        notificationList.add(new Notifications("Tu hijo entro a la esciela 8:03 AM", R.drawable.notifications));
        notificationList.add(new Notifications("STu hijo salio  a las 2:15 PM", R.drawable.notifications));

        // Copia de la lista completa para futuros filtros
        fullNotificationList = new ArrayList<>(notificationList);

        adapter = new NotificationsAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        // Listener para Kids
        dropdownKids.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedKid = kidsOptions[position];
            filterNotifications(selectedKid, dropdownDates.getText().toString());
            Toast.makeText(getContext(), "Filtrando por niño: " + selectedKid, Toast.LENGTH_SHORT).show();
        });

        // Listener para Fecha
        dropdownDates.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedDate = dateOptions[position];
            filterNotifications(dropdownKids.getText().toString(), selectedDate);
            Toast.makeText(getContext(), "Filtrando por fecha: " + selectedDate, Toast.LENGTH_SHORT).show();
        });

        adapter = new NotificationsAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        return view;

    }

    private void filterNotifications(String kid, String date) {
        List<Notifications> filteredList = new ArrayList<>();

        for (Notifications notification : fullNotificationList) {
            boolean matchesKid = kid.isEmpty() || notification.getDescription().toLowerCase().contains(kid.toLowerCase());
            boolean matchesDate = true; // Aquí podrías implementar lógica real basada en fecha si tu modelo tiene fecha

            // Simulación de filtro por fecha
            if (date.equals("Semana pasada")) {
                matchesDate = true; // Suponiendo todas las notificaciones son de la semana pasada
            } else if (date.equals("Último mes")) {
                matchesDate = true; // Igual, simulado
            }

            if (matchesKid && matchesDate) {
                filteredList.add(notification);
            }
        }

        adapter.updateList(filteredList);
    }
}