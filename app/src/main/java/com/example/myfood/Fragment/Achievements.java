package com.example.myfood.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfood.Adapter.AchievmentsListAdapter;
import com.example.myfood.Adapter.FoodListAdapter;
import com.example.myfood.Adapter.RecipeListAdapter;
import com.example.myfood.Class.Achievement;
import com.example.myfood.Class.FoodItem;
import com.example.myfood.Class.RecipeItem;
import com.example.myfood.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


        db.collection("Achievments").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Map<String, Object> doc = documentSnapshot.getData();
                    Achievement achievement = new Achievement();
                    achievement.setId((String) doc.get("id"));
                    achievement.setName((String) doc.get("name"));
                    achievement.setGoal((String) doc.get("goal"));
                    achievement.setPoints((String) doc.get("points"));
                    achievement.setStatus(String.valueOf(0));
                    achievements.add(achievement);

                }

                myRef = database.getReference("users").child(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getEmail().hashCode()));
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int current_num_cooks = 0;
                        int current_num_scans = 0;
                        int currentScore = 0;

                        current_num_cooks = Integer.parseInt(snapshot.child("num_cooks").getValue(String.class));
                        current_num_scans = Integer.parseInt(snapshot.child("num_scans").getValue(String.class));
                        currentScore = Integer.parseInt(snapshot.child("score").getValue(String.class));
                        myRef = snapshot.getRef();


                        for (Achievement achievement : achievements) {
                            if (achievement.getName().contains("להכין")) {
                                achievement.setStatus(String.valueOf(current_num_cooks));
                                if (Integer.parseInt(achievement.getStatus()) >= Integer.parseInt(achievement.getGoal())) {
                                    achievement.setGoal("בוצע");
                                    achievement.setStatus("");
                                    currentScore = currentScore + Integer.parseInt(achievement.getPoints());
                                    myRef.child("score").setValue(String.valueOf(currentScore));
                                }
                            } else if (achievement.getName().contains("לסרוק")) {
                                achievement.setStatus(String.valueOf(current_num_scans));
                                if (Integer.parseInt(achievement.getStatus()) >= Integer.parseInt(achievement.getGoal())) {
                                    achievement.setGoal("בוצע");
                                    achievement.setStatus("");
                                    currentScore = currentScore + Integer.parseInt(achievement.getPoints());
                                    myRef.child("score").setValue(String.valueOf(currentScore));
                                }
                            }
                        }


                        mAdapter = new AchievmentsListAdapter(achievements);
                        mRecyclerView.setLayoutManager(mLayoutManager);
                        mRecyclerView.setAdapter(mAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        return view;

    }
}
