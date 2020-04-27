package com.example.traveljournal.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.traveljournal.BadgeItemAdapter;
import com.example.traveljournal.R;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> mImageIds = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.activity_badge, container, false);
        initimageBitmap();
        initRecycleView(root);
        return root;
    }

    private void initimageBitmap(){
        mImageIds.add(R.drawable.avion);
        mNames.add("Congrats! First trip achieved");
        mImageIds.add(R.drawable.carare);
        mNames.add("Congrats! First trip achieved");
        mImageIds.add(R.drawable.aventura);
        mNames.add("Congrats! First trip achieved");
        mImageIds.add(R.drawable.munte);
        mNames.add("Congrats! First trip achieved");
    }

    private void initRecycleView(View view){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewBadge);
        recyclerView.setLayoutManager(layoutManager);
        BadgeItemAdapter adapter = new BadgeItemAdapter(mNames, mImageIds, this.getContext());
        recyclerView.setAdapter(adapter);
    }

}