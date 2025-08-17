package com.example.safekids.fregments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.safekids.MainActivity;
import com.example.safekids.R;
import com.example.safekids.adapters.NotificationsAdapter;
import com.example.safekids.models.Children;
import com.example.safekids.models.Notifications;
import com.example.safekids.network.ApiClient;
import com.example.safekids.network.ApiService;
import com.example.safekids.network.NotificationResponse;
import com.example.safekids.storage.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationsAdapter adapter;
    private List<Notifications> notificationList;
    private AutoCompleteTextView dropdownKids, dropdownDates;
    private SessionManager sessionManager;
    private ApiService apiService;
    private List<Children> students;
    private Map<String, Integer> dateFilterMap;
    private Handler handler;
    private Runnable pollingRunnable;
    private static final long POLLING_INTERVAL = 8000;
    private Set<Integer> shownNotificationIds;

    private static final String CHANNEL_ID = "SafeKidsNotifications";
    private static final int NOTIFICATION_ID_BASE = 1000;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public NotificationsFragment() {
    }

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
        sessionManager = new SessionManager(requireContext());
        apiService = ApiClient.getApiService();
        notificationList = new ArrayList<>();
        shownNotificationIds = new HashSet<>();
        handler = new Handler(Looper.getMainLooper());
        dateFilterMap = new HashMap<>();
        dateFilterMap.put("Hoy", 1);
        dateFilterMap.put("Esta semana", 2);
        dateFilterMap.put("Este mes", 3);
        dateFilterMap.put("Todos", 4);
        createNotificationChannel();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dropdownKids = view.findViewById(R.id.drop_children);
        dropdownDates = view.findViewById(R.id.dropdown_dates);

        students = sessionManager.getStudents();
        List<String> kidsOptions = new ArrayList<>();
        kidsOptions.add("Todos");
        for (Children student : students) {
            kidsOptions.add(student.getFirstName() + " " + student.getLastName());
        }
        ArrayAdapter<String> kidsAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, kidsOptions);
        dropdownKids.setAdapter(kidsAdapter);
        dropdownKids.setText("Todos", false);

        String[] dateOptions = {"Hoy", "Esta semana", "Este mes", "Todos"};
        ArrayAdapter<String> datesAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, dateOptions);
        dropdownDates.setAdapter(datesAdapter);
        dropdownDates.setText("Todos", false);

        adapter = new NotificationsAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        Log.d("NotificationsFragment", "Students loaded: " + students.size());
        for (Children student : students) {
            Log.d("NotificationsFragment", "Student: " + student.getFirstName() + " " + student.getLastName() + ", ID: " + student.getId());
        }

        fetchNotifications("All", 4);

        dropdownKids.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedKid = kidsOptions.get(position);
            String selectedDate = dropdownDates.getText().toString();
            String studentId = selectedKid.equals("Todos") ? "All" : String.valueOf(students.get(position - 1).getId());
            int dayFilter = dateFilterMap.getOrDefault(selectedDate, 4);
            fetchNotifications(studentId, dayFilter);
            Toast.makeText(getContext(), "Filtrando por niño: " + selectedKid, Toast.LENGTH_SHORT).show();
        });

        dropdownDates.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedDate = dateOptions[position];
            String selectedKid = dropdownKids.getText().toString();
            String studentId = selectedKid.equals("Todos") ? "All" : getStudentIdFromName(selectedKid);
            int dayFilter = dateFilterMap.get(selectedDate);
            fetchNotifications(studentId, dayFilter);
            Toast.makeText(getContext(), "Filtrando por fecha: " + selectedDate, Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void fetchNotifications(String studentId, int dayFilter) {
        String token = sessionManager.getToken();
        Log.d("NotificationsFragment", "Token used: " + token);
        if (token == null || token.isEmpty()) {
            Log.e("NotificationsFragment", "Token is null or empty");
            if (isAdded()) {
                Toast.makeText(requireContext(), "Sesión no iniciada", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        token = "Bearer " + token;

        Log.d("NotificationsFragment", "Fetching notifications for studentId: " + studentId + ", dayFilter: " + dayFilter);
        apiService.getNotifications(token, studentId, dayFilter).enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (!isAdded()) {
                    Log.w("NotificationsFragment", "Fragment not attached, ignoring response");
                    return;
                }
                Log.d("NotificationsFragment", "API response code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    NotificationResponse res = response.body();
                    Log.d("NotificationsFragment", "API response body: " + res);
                    if (res.isSuccess()) {
                        List<Notifications> newNotifications = res.getData() != null ? res.getData() : new ArrayList<>();
                        Log.d("NotificationsFragment", "Received " + newNotifications.size() + " notifications");
                        for (Notifications notification : newNotifications) {
                            Log.d("NotificationsFragment", "Notification ID: " + notification.getId() +
                                    ", Description: " + notification.getDescription() +
                                    ", LastMessage: " + notification.getLastMessage() +
                                    ", StudentId: " + notification.getStudentId() +
                                    ", CreatedAt: " + notification.getCreatedAt() +
                                    ", Photo: " + notification.getPhoto());
                            if (!shownNotificationIds.contains(notification.getId())) {
                                showNotification(notification);
                                shownNotificationIds.add(notification.getId());
                            }
                        }
                        notificationList.clear();
                        notificationList.addAll(newNotifications);
                        adapter.updateList(notificationList);
                        Log.d("NotificationsFragment", "Updated adapter with " + newNotifications.size() + " notifications");
                        Toast.makeText(requireContext(), "Notificaciones cargadas: " + res.getCount(), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("NotificationsFragment", "API error: " + res.getMessage());
                        Toast.makeText(requireContext(), res.getMessage(), Toast.LENGTH_SHORT).show();
                        adapter.updateList(new ArrayList<>());
                    }
                } else {
                    String errorBody = null;
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        Log.e("NotificationsFragment", "Error reading errorBody: " + e.getMessage());
                    }
                    Log.e("NotificationsFragment", "Response failed: " + response.code() + " " + response.message() + ", errorBody: " + errorBody);
                    Toast.makeText(requireContext(), "Error al cargar notificaciones: " + response.message() + (errorBody != null ? " - " + errorBody : ""), Toast.LENGTH_LONG).show();
                    adapter.updateList(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                if (!isAdded()) {
                    Log.w("NotificationsFragment", "Fragment not attached, ignoring failure");
                    return;
                }
                Log.e("NotificationsFragment", "Connection error: " + t.getMessage(), t);
                Toast.makeText(requireContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                adapter.updateList(new ArrayList<>());
            }
        });
    }

    private String getStudentIdFromName(String fullName) {
        for (Children student : students) {
            String studentName = student.getFirstName() + " " + student.getLastName();
            if (studentName.equals(fullName)) {
                return String.valueOf(student.getId());
            }
        }
        return "All";
    }

    private void startPolling() {
        pollingRunnable = new Runnable() {
            @Override
            public void run() {
                String selectedKid = dropdownKids.getText().toString();
                String selectedDate = dropdownDates.getText().toString();
                String studentId = selectedKid.equals("Todos") ? "All" : getStudentIdFromName(selectedKid);
                int dayFilter = dateFilterMap.getOrDefault(selectedDate, 4);
                Log.d("NotificationsFragment", "Polling for studentId: " + studentId + ", dayFilter: " + dayFilter);
                fetchNotifications(studentId, dayFilter);
                handler.postDelayed(this, POLLING_INTERVAL);
            }
        };
        handler.postDelayed(pollingRunnable, POLLING_INTERVAL);
    }

    private void stopPolling() {
        if (pollingRunnable != null) {
            handler.removeCallbacks(pollingRunnable);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startPolling();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPolling();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "SafeKids Notifications";
            String description = "Canal para notificaciones de SafeKids";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(Notifications notification) {
        NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(requireContext(), MainActivity.class);
        intent.putExtra("fragment", "NotificationsFragment");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.notifications)
                .setContentTitle("SafeKids")
                .setContentText(notification.getDescription() != null ? notification.getDescription() : "Sin descripción")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        notificationManager.notify(NOTIFICATION_ID_BASE + notification.getId(), builder.build());
    }
}