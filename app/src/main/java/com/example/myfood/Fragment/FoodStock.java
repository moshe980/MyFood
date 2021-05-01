package com.example.myfood.Fragment;

import android.content.DialogInterface;
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
import com.example.myfood.Class.FirebaseManager;
import com.example.myfood.Class.RightJustifyAlertDialog;
import com.example.myfood.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class FoodStock extends Fragment {

    private RecyclerView mRecyclerView;
    public static FoodListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button addBtn;
    private ImageView emptyFridge;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_food_stock, container, false);

        addBtn = view.findViewById(R.id.add_food_item);
        emptyFridge = view.findViewById(R.id.cartoon_empty_fridge);
        emptyFridge.setVisibility(view.GONE);
        mRecyclerView = view.findViewById(R.id.food_stock_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());

        FirebaseManager.getInstance().getFoodList(new FirebaseManager.FirebaseCallBack() {
            @Override
            public void onCallback(FirebaseManager.FirebaseResult result) {
                if (result.isSuccessful()) {
                    mAdapter = new FoodListAdapter(Family.getInstance().getFoodList());
                    if (Family.getInstance().getFoodList().size() > 0) {
                        emptyFridge.setVisibility(view.GONE);
                        mAdapter.setOnItemClickListener(new FoodListAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Intent intent = new Intent(getContext(), EditFoodList.class);
                                intent.putExtra("foodItem", Family.getInstance().getFoodList().get(position));
                                intent.putExtra("Class", "FoodStock");
                                startActivity(intent);
                            }

                            @Override
                            public void onItemLongClickListener(int position) {
                                MaterialAlertDialogBuilder builder = new RightJustifyAlertDialog(getActivity());
                                builder.setTitle("הסרת מוצר")
                                        .setMessage("להסיר את ה" + Family.getInstance().getFoodList().get(position).getFoodDescription());
                                builder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Family.getInstance().getFoodList().remove(position);
                                        mAdapter.notifyDataSetChanged();
                                        FirebaseManager.getInstance().setFoodList(new FirebaseManager.FirebaseCallBack() {
                                            @Override
                                            public void onCallback(FirebaseManager.FirebaseResult result) {

                                            }
                                        });
                                    }
                                });
                                builder.setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                builder.show();
                            }
                        });

                    } else {
                        emptyFridge.setVisibility(view.VISIBLE);

                    }
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setLayoutManager(mLayoutManager);

                }else{
                    emptyFridge.setVisibility(view.VISIBLE);

                }
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


