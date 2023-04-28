package us.mis.acmeexplorer.service;

import android.location.Location;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiRestAdapterInterface {
    @GET("place/findplacefromtext/json")
    Call<GoogleMapsResponse> getLocationCity(
            @Query("fields") String fields,
            @Query("input") String input,
            @Query("inputtype") String inputtype,
            @Query("key") String key);
}
