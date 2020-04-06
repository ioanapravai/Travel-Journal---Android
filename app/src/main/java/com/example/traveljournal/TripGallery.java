package com.example.traveljournal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.traveljournal.ui.home.NewTripFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class TripGallery extends AppCompatActivity {

    private Context context;
    private TextView textView;
    private ImageView imageView;
    private Trip trip;
    private GridView gridView;
    private static final String ID_KEY = "trip_id";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_gallery);
        context = this;
        textView = findViewById(R.id.TextViewTripName);
        imageView = findViewById(R.id.ImageViewTripIcon);
        floatingActionButton = findViewById(R.id.floatingActionButton2);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentManager fm = getSupportFragmentManager();
                AddPhotoToTripGalleryFragment addPhotoToTripGalleryFragment = new AddPhotoToTripGalleryFragment();
                addPhotoToTripGalleryFragment.show(fm, "fragment_add_photo_to_trip_gallery");
            }
        });
        String tripId = getIntent().getStringExtra(ID_KEY);
        FirebaseUser user = auth.getCurrentUser();
        db.collection("users").document(user.getUid())
                .collection("trips").document(tripId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        trip = documentSnapshot.toObject(Trip.class);
                        textView.setText(trip.getName()); //load trip name
                        StorageReference reference = storage.getReference().child(auth.getCurrentUser().getUid() + "/").
                                child(trip.getId() + "/").child(trip.getDownloadUrl());
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                        {
                            @Override
                            public void onSuccess(Uri downloadUrl)
                            {
                                Glide.with(context)
                                        .load(downloadUrl.toString())
                                        .into(imageView);
                            }
                        });
                        gridView = findViewById(R.id.grid_view_trip_gallery);
                        gridView.setAdapter(new TripGalleryImageAdapter(context, trip));
                    }
                });



    }

    public void uploadImageToTripGallery(Uri uri){
        final String uniquePictureId = UUID.randomUUID().toString();
        StorageReference reference = storageReference.child(auth.getCurrentUser().getUid() + "/").
                child(trip.getId() + "/").child(uniquePictureId);
        reference.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        trip.getPhotoUrls().add(uniquePictureId);
                        db.collection("users").document(auth.getCurrentUser().getUid())
                                .collection("trips").document(trip.getId())
                                .set(trip, SetOptions.merge());
                        finish();
                        startActivity(getIntent());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "upload failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }


}
