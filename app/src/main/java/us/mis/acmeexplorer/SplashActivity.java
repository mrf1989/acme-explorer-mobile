package us.mis.acmeexplorer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.List;

import us.mis.acmeexplorer.entity.Trip;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DURATION = 2000;
    public static final List<Trip> trips = Trip.generateTrips(100);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DURATION);
    }
}