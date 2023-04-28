package us.mis.acmeexplorer.service;

import android.location.Location;
import android.os.Handler;
import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRestCalls {
    ApiRestAdapterInterface service;

    public ApiRestCalls() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        service = retrofit.create(ApiRestAdapterInterface.class);
    }

    public void getLocationCity(String city, Callback<Location> customCallback) {
        service.getLocationCity("geometry", city, "textquery", "AIzaSyB0sKPbqh87oiwINQJix4HhDpFFMTMTCTU")
                .enqueue(new Callback<GoogleMapsResponse>() {
                    @Override
                    public void onResponse(Call<GoogleMapsResponse> call, Response<GoogleMapsResponse> response) {
                        if (response.body() == null) {
                            return;
                        }
                        Double latitude = response.body().getCandidates().get(0).getGeometry().getLocation().getLat();
                        Double longitude = response.body().getCandidates().get(0).getGeometry().getLocation().getLng();
                        Location location = new Location(city + " location");
                        location.setLatitude(latitude);
                        location.setLongitude(longitude);
                        customCallback.onResponse(null, Response.success(location));
                    }

                    @Override
                    public void onFailure(Call<GoogleMapsResponse> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }
}
