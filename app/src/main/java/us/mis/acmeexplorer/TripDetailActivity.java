package us.mis.acmeexplorer;

import androidx.appcompat.app.AppCompatActivity;

import android.icu.text.DateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import us.mis.acmeexplorer.entity.Trip;

public class TripDetailActivity extends AppCompatActivity {
    private ImageView imageViewTripDetail;
    private TextView textViewTitleTripDetail;
    private TextView textViewDatesTripDetail;
    private TextView textViewPriceTripDetail;
    private TextView textViewDescriptionTripDetail;
    private Button btnBuyTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        
        imageViewTripDetail = findViewById(R.id.imageViewTripDetail);
        textViewTitleTripDetail = findViewById(R.id.textViewTitleTripDetail);
        textViewDatesTripDetail = findViewById(R.id.textViewDatesTripDetail);
        textViewPriceTripDetail = findViewById(R.id.textViewPriceTripDetail);
        textViewDescriptionTripDetail = findViewById(R.id.textViewDescriptionTripDetail);
        btnBuyTrip = findViewById(R.id.btnBuyTrip);

        Trip trip = (Trip) getIntent().getSerializableExtra("TRIP");
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.forLanguageTag("es-ES"));
        String startDate = df.format(trip.getStartDate());
        String endDate = df.format(trip.getEndDate());

        Picasso.get().load(trip.getImageURI())
                .placeholder(R.drawable.available_trips)
                .error(R.drawable.error)
                .into(imageViewTripDetail);

        textViewTitleTripDetail.setText(trip.getFrom() + " - " + trip.getTo());
        textViewDatesTripDetail.setText(startDate + " - " + endDate);
        textViewPriceTripDetail.setText(trip.getPrice() + " €");
        textViewDescriptionTripDetail.setText(trip.getDescription());

        if (!trip.isSelected()) {
            btnBuyTrip.setVisibility(View.GONE);
        }

        btnBuyTrip.setOnClickListener(v -> {
            Toast.makeText(this, "The trip has been bought", Toast.LENGTH_LONG).show();
        });
    }
}