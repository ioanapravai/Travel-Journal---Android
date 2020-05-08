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
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
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
    private ImageView imageViewStar;
    private Context context;
    private TextView textView;
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


    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }


    public TextView getTextView() {
        return textView;
    }

    public TripItemViewHolder(@NonNull final View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.tripName);
        imageView = itemView.findViewById(R.id.imageView3);
        imageViewStar = itemView.findViewById(R.id.imageViewStar);
        textView.bringToFront();
        imageViewStar.bringToFront();
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
