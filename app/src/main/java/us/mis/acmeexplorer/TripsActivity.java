package us.mis.acmeexplorer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import us.mis.acmeexplorer.adapter.TripAdapter;
import us.mis.acmeexplorer.entity.Filter;
import us.mis.acmeexplorer.entity.Trip;
import us.mis.acmeexplorer.service.FirebaseDatabaseService;

public class TripsActivity extends AppCompatActivity {
    private Filter filter = new Filter();
    private Button btnFilter;
    private TextView hTrips;
    private RecyclerView recyclerView;
    private List<Trip> trips = new ArrayList<>();
    private List<Trip> filteredTrips;
    private TripAdapter adapter = new TripAdapter(trips);
    private FirebaseDatabaseService firebaseDatabaseService = FirebaseDatabaseService.getServiceInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        btnFilter = findViewById(R.id.btnFilter);
        hTrips = findViewById(R.id.hTrips);

        String navigateTo = getIntent().getStringExtra("NAVIGATE_TO");
        if (navigateTo.equals("SELECTED_TRIPS")) {
            hTrips.setText("Selected Trips");
            btnFilter.setVisibility(View.GONE);
        }

        firebaseDatabaseService.getTrips().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    trips.clear();
                    for (DataSnapshot tripSnapshot : task.getResult().getChildren()) {
                        Trip trip = tripSnapshot.getValue(Trip.class);
                        switch (navigateTo) {
                            case "SELECTED_TRIPS":
                                if (trip.getIsSelected()) {
                                    trip.setId(tripSnapshot.getKey());
                                    trips.add(trip);
                                }
                                break;
                            case "LIST_TRIPS":
                                trip.setId(tripSnapshot.getKey());
                                trips.add(trip);
                                break;
                        }
                    }
                    adapter.setTripsList(trips);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                        Intent data = result.getData();
                        filter = data.getParcelableExtra("SEARCH_FILTER");
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
        Toast.makeText(TripsActivity.this, "" + filteredTrips.size() + " trips found", Toast.LENGTH_LONG).show();
        adapter.setTripsList(filteredTrips);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(TripsActivity.this));
    }
}