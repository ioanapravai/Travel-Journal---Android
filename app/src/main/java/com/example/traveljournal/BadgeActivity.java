package com.example.traveljournal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class BadgeActivity extends AppCompatActivity {

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Integer> mImageIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_badge);
        initimageBitmap();
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
        initRecycleView();
    }

    private void initRecycleView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewBadge);
        recyclerView.setLayoutManager(layoutManager);
        BadgeItemAdapter adapter = new BadgeItemAdapter(mNames, mImageIds, this);
        recyclerView.setAdapter(adapter);
    }


}
