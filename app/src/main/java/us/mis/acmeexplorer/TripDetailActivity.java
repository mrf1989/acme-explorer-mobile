package us.mis.acmeexplorer;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import us.mis.acmeexplorer.entity.Trip;

public class TripDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ImageView imageViewTripDetail;
    private TextView textViewTitleTripDetail;
    private TextView textViewDatesTripDetail;
    private TextView textViewPriceTripDetail;
    private TextView textViewDescriptionTripDetail;
    private Button btnBuyTrip;

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 0x123;
    private GoogleMap mMap;
    private MapView mapView;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private boolean mRequestingLocationUpdates = true;
    private LocationRequest mLocationRequest;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

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
        mapView = findViewById(R.id.mapView);

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

        if (!trip.getIsSelected()) {
            btnBuyTrip.setVisibility(View.GONE);
        }

        btnBuyTrip.setOnClickListener(v -> {
            Toast.makeText(this, "The trip has been bought", Toast.LENGTH_LONG).show();
        });

        // LOCATION

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        setMarkerLocation(location);
                    }
                }
            }
        };

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if (mRequestingLocationUpdates && requestLocationPermission()) {
            updateMapLocation();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (requestLocationPermission()) {
            updateMapLocation();
        }
        /*mMap.setOnMapClickListener(point -> {
            Location location = new Location("Map Point");
            location.setLatitude(point.latitude);
            location.setLongitude(point.longitude);
            setMarkerLocation(location);
        });*/
    }

    private void updateMapLocation() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    setMarkerLocation(location);
                }
            });
        }
    }

    private boolean requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateMapLocation();
                } else {
                    Snackbar.make(mapView, R.string.location_permission_error, Snackbar.LENGTH_LONG).setAction(R.string.location_permission_retry, v -> requestLocationPermission()).show();
                }
                return;
            }
        }
    }

    private void setMarkerLocation(Location location) {
        Geocoder geoCoder = new Geocoder(getApplicationContext());
        List<Address> matches = null;
        try {
            matches = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Address bestMatch = (matches.isEmpty() ? null : matches.get(0));
            if (bestMatch != null) {
                mRequestingLocationUpdates = false;
                LatLng newLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(newLocation)
                        .title(bestMatch.getLocality()));
                mMap.setMinZoomPreference(8);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
            } else {
                Toast.makeText(getApplicationContext(), R.string.no_location, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}