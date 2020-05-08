package com.example.traveljournal.ui.home;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.traveljournal.CustomLinearLayoutManager;
import com.example.traveljournal.R;
import com.example.traveljournal.Trip;
import com.example.traveljournal.TripGallery;
import com.example.traveljournal.TripItemViewHolder;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerViewTrip;
    private FirestoreRecyclerAdapter adapter;
    private StorageReference storageReference;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private Context context = getActivity();
    private Trip trip;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewTrip = view.findViewById(R.id.recyclerViewTrip);
        setLayoutManager();
        setAdapter();
    }

    private void setLayoutManager()
    {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewTrip.setLayoutManager(layoutManager);
    }

    private void setAdapter()
    {
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        Query query = database
                .collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("trips");
        FirestoreRecyclerOptions<Trip> options = new FirestoreRecyclerOptions.Builder<Trip>()
                                                    .setQuery(query, Trip.class)
                                                    .build();
        final Context context = this.getActivity().getBaseContext();
        adapter = new FirestoreRecyclerAdapter<Trip, TripItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final TripItemViewHolder holder, int position, @NonNull Trip model) {
                holder.setTrip(model);
                holder.getTextView().setText(model.getName());
                if(model.getDownloadUrl() != null && !model.getDownloadUrl().isEmpty()){
                    storageReference = storage.getReference().child(auth.getCurrentUser().getUid() + "/")
                            .child(model.getId() + "/").child(model.getDownloadUrl());
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                    {
                        @Override
                        public void onSuccess(Uri downloadUrl)
                        {
                            Glide.with(context)
                                    .load(downloadUrl.toString())
                                    .into(holder.getImageView());
                        }
                    });
                }

            }

            @NonNull
            @Override
            public TripItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = (View) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.trip_item, parent, false);
                return new TripItemViewHolder(view);
            }
        };
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerViewTrip);
        recyclerViewTrip.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            TripItemViewHolder tripItemViewHolder = (TripItemViewHolder)viewHolder;
            trip = tripItemViewHolder.getTrip();
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
    };
}