package com.example.myfood.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfood.Adapter.AchievmentsListAdapter;
import com.example.myfood.Class.Achievement;
import com.example.myfood.Class.FirebaseManager;
import com.example.myfood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Achievements extends Fragment {
    public static ArrayList<Achievement> achievements;
    private RecyclerView mRecyclerView;
    public static AchievmentsListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_achievements, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db = FirebaseFirestore.getInstance();
        mRecyclerView = view.findViewById(R.id.achievments_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        achievements = new ArrayList<Achievement>();

        FirebaseManager.getInstance().getAchievementsList(new FirebaseManager.FirebaseCallBack() {
            @Override
            public void onCallback(FirebaseManager.FirebaseResult result) {
                if (result.isSuccessful()) {
                    achievements.addAll(result.getAchievementsList());

                    mAdapter = new AchievmentsListAdapter(achievements);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
        });


        return view;

    }
}
