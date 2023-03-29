package us.mis.acmeexplorer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import us.mis.acmeexplorer.adapter.TripAdapter;

public class TripsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        recyclerView = findViewById(R.id.tripsRecyclerView);
        recyclerView.setAdapter(new TripAdapter(SplashActivity.trips));
        recyclerView.setLayoutManager(new LinearLayoutManager(TripsActivity.this));
    }
}