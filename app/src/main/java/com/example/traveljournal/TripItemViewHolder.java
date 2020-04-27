package com.example.traveljournal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import com.example.traveljournal.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class TripItemViewHolder extends RecyclerView.ViewHolder {

    private CircleImageView imageView;
    private Context context;
    private TextView textView;
    private RatingBar ratingBar;
    private Trip trip;
    private ImageButton deleteButton;
    private FloatingActionButton fabDelete;
    private static final String ID_KEY = "trip_id";

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(CircleImageView imageView) {
        this.imageView = imageView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }


    public void setRatingBar(RatingBar ratingBar) {
        this.ratingBar = ratingBar;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }


    public RatingBar getRatingBar() {
        return ratingBar;
    }

    public TextView getTextView() {
        return textView;
    }

    public TripItemViewHolder(@NonNull final View itemView) {
        super(itemView);
        ratingBar = itemView.findViewById(R.id.rateTrip);
        textView = itemView.findViewById(R.id.tripName);
        imageView = itemView.findViewById(R.id.imageView3);
        fabDelete = itemView.findViewById(R.id.floatingActionButton);
        textView.bringToFront();
        ratingBar.bringToFront();
        fabDelete.bringToFront();
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                database.collection("users")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .collection("trips")
                        .document(trip.getId())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }
        });

        context = itemView.getContext();
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(context, TripGallery.class);
                mIntent.putExtra(ID_KEY, trip.getId());
                context.startActivity(mIntent);
            }
        });

    }
}
