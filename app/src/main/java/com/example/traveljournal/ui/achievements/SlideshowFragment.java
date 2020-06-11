package com.example.traveljournal.ui.achievements;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.traveljournal.BadgeItemAdapter;
import com.example.traveljournal.R;
import com.example.traveljournal.Trip;
import com.example.traveljournal.TripGalleryImageAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> mImageIds = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private Trip trip;
    private int numberOfTrips;
    private FloatingActionButton floatingActionButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.activity_badge, container, false);
        floatingActionButton = root.findViewById(R.id.fab);
        //initimageBitmap();
        getNumberOfTripsAndInit(root);
        //initRecycleView(root);
        return root;
    }

    private void getNumberOfTripsAndInit(final View view){
        FirebaseUser user = auth.getCurrentUser();
        Task<QuerySnapshot> querySnapshotTask = db.collection("users").document(user.getUid())
                .collection("trips").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            numberOfTrips = queryDocumentSnapshots.size();
                        }
                        initimageBitmap();
                        initRecycleView(view);
                    }
                });
    }

    private void initimageBitmap(){
        if(numberOfTrips >= 1){
            mImageIds.add(R.drawable.avion);
            mNames.add(" “I am not the same, having seen the moon shine on the other side of the world.” – Mary Anne Radmacher ");
        }else {
            mImageIds.add(R.drawable.logo);
            mNames.add(" Adventure is worthwhile ");
        }

        if(numberOfTrips <= 5 && numberOfTrips > 1){
            mImageIds.add(R.drawable.carare);
            mNames.add(" “Traveling – it leaves you speechless, then turns you into a storyteller.” – Ibn Battuta ");
        }else {
            mImageIds.add(R.drawable.logo);
            mNames.add(" Adventure is worthwhile ");
        }

        if(numberOfTrips <= 15 && numberOfTrips > 5){
            mImageIds.add(R.drawable.carare);
            mNames.add(" “We travel, some of us forever, to seek other places, other lives, other souls.” – Anais Nin ");
        }else {
            mImageIds.add(R.drawable.logo);
            mNames.add(" Adventure is worthwhile ");
        }

        if(numberOfTrips <= 50 && numberOfTrips > 40){
            mImageIds.add(R.drawable.carare);
            mNames.add("Travel Guru :)");
        }else {
            mImageIds.add(R.drawable.logo);
            mNames.add(" Adventure is worthwhile ");
        }

    }

    private void initRecycleView(View view){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewBadge);
        recyclerView.setLayoutManager(layoutManager);
        BadgeItemAdapter adapter = new BadgeItemAdapter(mNames, mImageIds, this.getContext());
        recyclerView.setAdapter(adapter);
    }

}