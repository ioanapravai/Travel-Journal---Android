package com.example.traveljournal;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import static android.widget.Toast.LENGTH_SHORT;


public class AddPhotoToTripGalleryFragment extends DialogFragment {


    private ImageView imageView;
    private Trip trip;
    private Context context;
    private static final int PICK_PHOTO = 914;
    private static final int PICK_IMAGE_REQUEST = 71;
    private Uri url;

    public AddPhotoToTripGalleryFragment() {
        // Required empty public constructor
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_add_photo_to_trip_gallery, null);
        imageView = view.findViewById(R.id.imageView5);
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

                        getImageUrl();

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



    private void getImageUrl(){
        if(url == null){
            Toast.makeText(context, "Choose photo", LENGTH_SHORT).show();
        }
        else {
            TripGallery tripGallery = (TripGallery)getActivity();
            try{
                tripGallery.uploadImageToTripGallery(url);
            }catch (NullPointerException e){
               //TODO: log not working
            }

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

                url = data.getData();
                Picasso.get().load(url).fit().centerInside().into(imageView);

            }
        }
    }
}
