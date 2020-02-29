package com.example.traveljournal.ui.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traveljournal.R;
import com.example.traveljournal.Trip;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import static android.widget.Toast.LENGTH_SHORT;

public class NewTripFragment extends DialogFragment {

    private TextView textView;
    private RatingBar ratingBar;
    private ImageView imageView;
    private Trip trip = new Trip();
    private static final int PICK_PHOTO = 914;

    public NewTripFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.fragment_new_trip, null);
        imageView = view.findViewById(R.id.imageView4);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_PHOTO);
            }
        });
        builder.setView(view)
                .setTitle("New trip")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        // call the method on the parent activity when user click the positive button
                        textView = view.findViewById(R.id.tripName);
                        ratingBar = view.findViewById(R.id.ratingBarTrip);
                        imageView = view.findViewById(R.id.imageView4);
                        FirebaseFirestore dataBase = FirebaseFirestore.getInstance();
                        //trip gol
                        String tripId = dataBase.collection("users")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .collection("trips")
                                .document().getId();
                        trip.setId(tripId);
                        trip.setRating(ratingBar.getNumStars());
                        trip.setName(textView.getText().toString());
                        dataBase.collection("users")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .collection("trips")
                                .document(tripId).set(trip)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("trip", "added");
                                        dialog.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("trip", "failed");
                                        dialog.dismiss();
                                    }});;
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_PHOTO){
            if(resultCode == Activity.RESULT_OK){
                if(data == null){
                    Toast.makeText(this.getActivity(), "Image Pick Erorr", LENGTH_SHORT).show();
                    return;
                }

                Uri uri = data.getData();
                trip.setUrl(uri.toString());
                Picasso.get().load(uri).resize(170, 170).centerInside().into(imageView);

            }
        }
    }
}
