package com.example.traveljournal;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BadgeItemAdapter  extends RecyclerView.Adapter<BadgeItemAdapter.ViewHolder>{

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> mImageIds = new ArrayList<>();
    private Context mContext;

    public BadgeItemAdapter(ArrayList<String> mNames, ArrayList<Integer> mImageIds, Context mContext) {
        this.mNames = mNames;
        this.mImageIds = mImageIds;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.badge_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(mImageIds.get(position))
                .into(holder.image);
        holder.name.setText(mNames.get(position));
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
        }
    }
}
