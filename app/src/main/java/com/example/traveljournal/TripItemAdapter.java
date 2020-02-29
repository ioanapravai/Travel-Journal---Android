package com.example.traveljournal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TripItemAdapter extends RecyclerView.Adapter<TripItemViewHolder> {

    private List<Trip> trips;

    public TripItemAdapter(List<Trip> trips) {
        this.trips = trips;
    }

    @NonNull
    @Override
    public TripItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false);
        return new TripItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TripItemViewHolder holder, int position) {
        //spune ce sa puna in view
        Trip currentTrip = trips.get(position);
        holder.getTextView().setText(currentTrip.getName());

    }

    @Override
    public int getItemCount() {
        return trips.size();
    }
}
