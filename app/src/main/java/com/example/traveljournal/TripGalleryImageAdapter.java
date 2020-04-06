package com.example.traveljournal;

import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class TripGalleryImageAdapter extends BaseAdapter {

    private Context mContext;
    private Trip trip;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();


    public TripGalleryImageAdapter(Context mContext, Trip trip) {
        this.mContext = mContext;
        this.trip = trip;
    }

    @Override
    public int getCount() {
        return trip.getPhotoUrls().size();
    }

    @Override
    public Object getItem(int position) {
        return trip.getPhotoUrls().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView imageView = new ImageView(mContext);
        storageReference = storage.getReference().child(auth.getCurrentUser().getUid() + "/")
                .child(trip.getId() + "/").child(trip.getPhotoUrls().get(position));
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
        {
            @Override
            public void onSuccess(Uri downloadUrl)
            {
                Glide.with(mContext)
                        .load(downloadUrl.toString())
                        .into(imageView);
            }
        });
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(340, 350));
        return imageView;
    }

}
