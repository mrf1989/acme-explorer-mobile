package us.mis.acmeexplorer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import us.mis.acmeexplorer.adapter.TripAdapter;
import us.mis.acmeexplorer.entity.Filter;
import us.mis.acmeexplorer.entity.Trip;
import us.mis.acmeexplorer.service.FirebaseDatabaseService;

public class TripsActivity extends AppCompatActivity {
    private Filter filter = null;
    private Button btnFilter;
    private TextView hTrips;
    private RecyclerView recyclerView;
    private HashMap<String, Trip> tripHashMap = new HashMap<>();
    private List<Trip> trips = new ArrayList<>();
    private List<Trip> filteredTrips;
    private TripAdapter adapter = new TripAdapter(trips);
    private FirebaseDatabaseService firebaseDatabaseService = FirebaseDatabaseService.getServiceInstance();
    private boolean filterSearch = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        btnFilter = findViewById(R.id.btnFilter);
        hTrips = findViewById(R.id.hTrips);
        String navigateTo = getIntent().getStringExtra("NAVIGATE_TO");

        firebaseDatabaseService.getTrips().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    for (DataSnapshot tripSnapshot : task.getResult().getChildren()) {
                        String id = tripSnapshot.getKey();
                        Trip trip = tripSnapshot.getValue(Trip.class);
                        trip.setId(id);
                        tripHashMap.put(id, trip);
                    }
                }

                firebaseDatabaseService.getSelectedTrips().addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.exists() && snapshot.getValue() != null) {
                            Trip trip = snapshot.getValue(Trip.class);
                            tripHashMap.get(trip.getId()).setIsSelected(true);
                            updateTripList(navigateTo.equals("SELECTED_TRIPS"));
                        }
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && snapshot.getValue() != null) {
                            Trip trip = snapshot.getValue(Trip.class);
                            tripHashMap.get(trip.getId()).setIsSelected(false);
                            updateTripList(navigateTo.equals("SELECTED_TRIPS"));
                        }
                    }
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
                updateTripList(navigateTo.equals("SELECTED_TRIPS"));
            }
        });

        if (navigateTo.equals("SELECTED_TRIPS")) {
            hTrips.setText("Selected Trips");
            btnFilter.setVisibility(View.GONE);
        }

        ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        Intent data = result.getData();
                        filter = data.getParcelableExtra("SEARCH_FILTER");
                        filterSearch = true;
                        filterTrips(filter);
                    }
                }
        );

        btnFilter.setOnClickListener(view -> {
            Intent intent = new Intent(TripsActivity.this, FilterActivity.class);
            startActivityResult.launch(intent);
        });

        recyclerView = findViewById(R.id.tripsRecyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(TripsActivity.this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        //filter = null;
    }

    private void updateTripList(boolean selectedOnly) {
        trips.clear();
        trips.addAll(tripHashMap.values());

        if (selectedOnly) {
            trips = trips.stream()
                    .filter(Trip::getIsSelected)
                    .collect(Collectors.toList());
        }

        if (filter != null) {
            filterTrips(filter);
        } else {
            adapter.setTripsList(trips);
            adapter.notifyDataSetChanged();
        }
    }

    private void filterTrips(Filter filter) {
        filteredTrips = new ArrayList<>();
        Date startDate = new Date(filter.getStarDate());
        Date endDate = new Date(filter.getEndDate());

        for (int i = 0; i < trips.size(); i++) {
            Trip trip = trips.get(i);

            boolean isAcceptedPrice = trip.getPrice() >= filter.getMinPrice()
                    && (trip.getPrice() <= filter.getMaxPrice() || filter.getMaxPrice() == 0);

            boolean x1 = new Date(trip.getStartDate()).getTime() >= startDate.getTime();
            boolean x2 = new Date(trip.getStartDate()).getTime() < endDate.getTime();
            boolean x3 = new Date(trip.getEndDate()).getTime() > startDate.getTime();
            boolean x4 = new Date(trip.getEndDate()).getTime() <= endDate.getTime();

            boolean isAcceptedDate = ((x1 && x2) || filter.getStarDate() == 0)
                    && ((x3 && x4) || filter.getEndDate() == 0);

            if (isAcceptedPrice && isAcceptedDate) {
                filteredTrips.add(trip);
            }
        }
        if (filterSearch) {
            filterSearch = false;
            Toast.makeText(TripsActivity.this, "" + filteredTrips.size() + " trips found", Toast.LENGTH_LONG).show();
        }
        adapter.setTripsList(filteredTrips);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(TripsActivity.this));
    }
}