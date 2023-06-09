package us.mis.acmeexplorer.adapter;

import android.content.Context;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import us.mis.acmeexplorer.R;
import us.mis.acmeexplorer.TripDetailActivity;
import us.mis.acmeexplorer.entity.Trip;
import us.mis.acmeexplorer.service.FirebaseDatabaseService;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {
    private List<Trip> trips;
    private FirebaseDatabaseService firebaseDatabaseService = FirebaseDatabaseService.getServiceInstance();

    public TripAdapter(List<Trip> trips) {
        this.trips = trips;
    }

    public void setTripsList(List<Trip> tripsList) {
        this.trips = tripsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View tripView = layoutInflater.inflate(R.layout.item_trip, parent, false);
        ViewHolder viewHolder = new ViewHolder(tripView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = trips.get(position);

        ImageView tripImage = holder.tripImageView;

        Picasso.get().load(trip.getImageURI())
                .placeholder(R.drawable.available_trips)
                .error(R.drawable.error)
                .into(tripImage);

        TextView tripTextView = holder.tripTextView;
        tripTextView.setText(trip.getFrom() + " - " + trip.getTo());

        TextView tripDateTextView = holder.tripDateTextView;
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.forLanguageTag("es-ES"));
        String startDate = df.format(trip.getStartDate());
        String endDate = df.format(trip.getEndDate());
        tripDateTextView.setText(startDate + " - " + endDate);

        ImageView tripSelectIcon = holder.tripSelectIcon;

        if (trip.getIsSelected()) {
            tripSelectIcon.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            tripSelectIcon.setImageResource(android.R.drawable.btn_star);
        }

        tripSelectIcon.setOnClickListener(v -> {
            if (!trip.getIsSelected()) {
                tripSelectIcon.setImageResource(android.R.drawable.btn_star_big_on);
                trip.setIsSelected((true));
                firebaseDatabaseService.addTrip(trip, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(v.getContext(), "Add to selected trips", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                tripSelectIcon.setImageResource(android.R.drawable.btn_star);
                trip.setIsSelected((false));
                firebaseDatabaseService.unSelectTrip(trip, (error, ref) -> {
                    Toast.makeText(v.getContext(), "Remove to selected trips", Toast.LENGTH_SHORT).show();
                });
            }
        });

        LinearLayout tripItem = holder.tripItem;
        tripItem.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), TripDetailActivity.class);
            intent.putExtra("TRIP", trip);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView tripImageView;
        private ImageView tripSelectIcon;
        private TextView tripTextView;
        private TextView tripDateTextView;
        private LinearLayout tripItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tripImageView = (ImageView) itemView.findViewById(R.id.tripImageView);
            tripSelectIcon = (ImageView) itemView.findViewById(R.id.tripSelectIcon);
            tripTextView = (TextView) itemView.findViewById(R.id.tripTextView);
            tripDateTextView = (TextView) itemView.findViewById(R.id.tripDateTextView);
            tripItem = (LinearLayout) itemView.findViewById(R.id.tripItem);
        }
    }
}
