package us.mis.acmeexplorer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import us.mis.acmeexplorer.adapter.TripAdapter;
import us.mis.acmeexplorer.entity.Filter;
import us.mis.acmeexplorer.entity.Trip;

public class TripsActivity extends AppCompatActivity {
    private Filter filter = new Filter();
    private Button btnFilter;
    private RecyclerView recyclerView;
    private List<Trip> trips = SplashActivity.trips;
    private List<Trip> filteredTrips;
    private RecyclerView.Adapter adapter = new TripAdapter(trips);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        btnFilter = findViewById(R.id.btnFilter);

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

        boolean selectedTripsList = getIntent()
                .getBooleanExtra("SELECTED_TRIPS", false);

        if (selectedTripsList) {
            TextView hTrips = findViewById(R.id.hTrips);
            hTrips.setText("Selected Trips");
            btnFilter.setVisibility(View.GONE);

            List<Trip> selectedTrips = trips.stream()
                    .filter(trip -> trip.isSelected())
                    .collect(Collectors.toList());

            adapter = new TripAdapter(selectedTrips);
        }

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(TripsActivity.this));
    }

    private void filterTrips(Filter filter) {
        filteredTrips = new ArrayList<>();
        for (int i = 0; i < trips.size(); i++) {
            Trip trip = trips.get(i);
            Date startDate = new Date(filter.getStarDate());
            Date endDate = new Date(filter.getEndDate());

            boolean isAcceptedPrice = trip.getPrice() >= filter.getMinPrice()
                    && (trip.getPrice() <= filter.getMaxPrice() || filter.getMaxPrice() == 0);

            boolean x1 = trip.getStartDate().getTime() >= startDate.getTime();
            boolean x2 = trip.getStartDate().getTime() < endDate.getTime();
            boolean x3 = trip.getEndDate().getTime() > startDate.getTime();
            boolean x4 = trip.getEndDate().getTime() <= endDate.getTime();

            boolean isAcceptedDate = ((x1 && x2) || filter.getStarDate() == 0)
                    && ((x3 && x4) || filter.getEndDate() == 0);

            if (isAcceptedPrice && isAcceptedDate) {
                filteredTrips.add(trip);
            }
        }
        Toast.makeText(TripsActivity.this, "" + filteredTrips.size() + " trips found", Toast.LENGTH_LONG).show();
        adapter = new TripAdapter(filteredTrips);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(TripsActivity.this));
    }
}