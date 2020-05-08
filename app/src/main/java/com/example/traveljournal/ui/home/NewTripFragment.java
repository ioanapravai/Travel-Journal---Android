package com.example.traveljournal.ui.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
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
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.traveljournal.R;
import com.example.traveljournal.Trip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.UUID;

import io.grpc.Context;

import static android.widget.Toast.LENGTH_SHORT;

public class NewTripFragment extends DialogFragment {

    private TextView textView;
    private ImageView imageView;
    private Trip trip = new Trip();
    private static final int PICK_PHOTO = 914;
    private static final int PICK_IMAGE_REQUEST = 71;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private Uri uploadUri;
    private DialogInterface dialogInterface;
    private FirebaseFirestore dataBase;

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
                .setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        dialogInterface = dialog;
                        // call the method on the parent activity when user click the positive button
                        textView = view.findViewById(R.id.tripName);
                        imageView = view.findViewById(R.id.imageView4);
                        storage = FirebaseStorage.getInstance();
                        storageReference = storage.getReference();
                        dataBase = FirebaseFirestore.getInstance();
                        trip.setName(textView.getText().toString());
                        createTripId();
                        uploadImage();

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


    private void createTripId(){
        String tripId = dataBase.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("trips")
                .document().getId();
        trip.setId(tripId);
    }

    private void uploadTrip(){
        dataBase.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("trips")
                .document(trip.getId()).set(trip)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("trip", "added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("trip", "failed");
                    }});
    }

    private void uploadImage(){
        auth = FirebaseAuth.getInstance();
        if(uploadUri != null){
            final String uniquePictureId = UUID.randomUUID().toString();
            StorageReference reference = storageReference.child(auth.getCurrentUser().getUid() + "/").
                    child(trip.getId() + "/").child(uniquePictureId);
            reference.putFile(uploadUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            trip.setDownloadUrl(uniquePictureId);
                            uploadTrip();
                            dialogInterface.dismiss();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialogInterface.dismiss();
                }
            });
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_PHOTO){
            if(resultCode == Activity.RESULT_OK){
                if(data == null || data.getData() == null){
                    Toast.makeText(this.getActivity(), "Image Pick Erorr", LENGTH_SHORT).show();
                    return;
                }

                uploadUri = data.getData();
                trip.setUrl(uploadUri.toString());
                Picasso.get().load(uploadUri).fit().centerInside().into(imageView);

            }
        }
    }
}
