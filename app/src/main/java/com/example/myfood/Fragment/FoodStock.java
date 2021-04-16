package com.example.myfood.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfood.Activity.Popup.AddFoodList;
import com.example.myfood.Activity.Popup.EditFoodList;
import com.example.myfood.Adapter.FoodListAdapter;
import com.example.myfood.Class.Family;
import com.example.myfood.Class.FoodItem;
import com.example.myfood.Class.User;
import com.example.myfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FoodStock extends Fragment {

    private RecyclerView mRecyclerView;
    public static FoodListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button addBtn;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private ImageView emptyFridge;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_food_stock, container, false);

        addBtn = view.findViewById(R.id.add_food_item);
        emptyFridge = view.findViewById(R.id.cartoon_empty_fridge);
        mRecyclerView = view.findViewById(R.id.food_stock_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        emptyFridge.setVisibility(view.GONE);

        myRef = database.getReference("families").child(User.getInstance().getFamilyCode());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Family.getInstance().getFoodList().clear();
                for (DataSnapshot keyNode : snapshot.child("foodStock").getChildren()) {
                    FoodItem foodItem = keyNode.getValue(FoodItem.class);
                    Family.getInstance().getFoodList().add(foodItem);
                }

                mAdapter = new FoodListAdapter( Family.getInstance().getFoodList());
                if (Family.getInstance().getFoodList().size() > 0) {
                    emptyFridge.setVisibility(view.GONE);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.setOnItemClickListener(new FoodListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Intent intent = new Intent(getContext(), EditFoodList.class);
                            intent.putExtra("foodItem", Family.getInstance().getFoodList().get(position));
                            intent.putExtra("Class", "FoodStock");
                            startActivity(intent);
                        }
                    });
                } else {
                    emptyFridge.setVisibility(view.VISIBLE);

                }
                mRecyclerView.setLayoutManager(mLayoutManager);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddFoodList.class);
                intent.putExtra("Class", "FoodStock");
                startActivity(intent);

            }
        });


        return view;


    }


}
