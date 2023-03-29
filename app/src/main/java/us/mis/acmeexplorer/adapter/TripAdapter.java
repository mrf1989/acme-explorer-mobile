package us.mis.acmeexplorer.adapter;

import android.content.Context;
import android.icu.text.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import us.mis.acmeexplorer.R;
import us.mis.acmeexplorer.entity.Trip;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {
    private List<Trip> trips;

    public TripAdapter(List<Trip> trips) {
        this.trips = trips;
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

        TextView tripTextView = holder.tripTextView;
        tripTextView.setText(trip.getFrom() + " - " + trip.getTo());

        TextView tripDateTextView = holder.tripDateTextView;
        Date startDate = trip.getStartDate();
        tripDateTextView.setText(DateFormat.getPatternInstance(DateFormat.YEAR_ABBR_MONTH).format(startDate));
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tripImageView = (ImageView) itemView.findViewById(R.id.tripImageView);
            tripSelectIcon = (ImageView) itemView.findViewById(R.id.tripSelectIcon);
            tripTextView = (TextView) itemView.findViewById(R.id.tripTextView);
            tripDateTextView = (TextView) itemView.findViewById(R.id.tripDateTextView);
        }
    }
}
