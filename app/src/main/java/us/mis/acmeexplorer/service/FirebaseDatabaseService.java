package us.mis.acmeexplorer.service;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import us.mis.acmeexplorer.entity.Trip;

public class FirebaseDatabaseService {
    private static String userId;
    private static FirebaseDatabaseService service;
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabaseService getServiceInstance() {
        if (service == null || mDatabase == null) {
            service = new FirebaseDatabaseService();
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }

        if (userId == null || userId.isEmpty()) {
            userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                    FirebaseAuth.getInstance().getCurrentUser().getUid() : "";
        }

        return service;
    }

    public void removeCollection(DatabaseReference.CompletionListener completionListener) {
        mDatabase.getReference("trips").removeValue(completionListener);
    }

    public void saveTrip(Trip trip, DatabaseReference.CompletionListener completionListener) {
        mDatabase.getReference("trips").push().setValue(trip, completionListener);
    }

    /*public DatabaseReference getTrips() {
        return mDatabase.getReference("trips").getRef();
    }*/

    public Task<DataSnapshot> getTrips() {
        return mDatabase.getReference("trips").get();
    }

    public DatabaseReference getSelectedTrips() {
        return mDatabase.getReference("users/" + userId + "/trips").getRef();
    }

    public void unSelectTrip(Trip trip, DatabaseReference.CompletionListener completionListener) {
        mDatabase.getReference("users/" + userId + "/trips/" + trip.getId()).removeValue(completionListener);
    }

    public void addTrip(Trip trip, DatabaseReference.CompletionListener completionListener) {
        mDatabase.getReference("users/" + userId + "/trips/" + trip.getId()).setValue(trip, completionListener);
    }
}
