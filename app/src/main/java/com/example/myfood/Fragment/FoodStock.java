package com.example.myfood.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfood.Activity.Login;
import com.example.myfood.Activity.Popup.AddFoodList;
import com.example.myfood.Activity.Popup.EditFoodList;
import com.example.myfood.Class.FoodItem;
import com.example.myfood.Adapter.FoodListAdapter;
import com.example.myfood.R;

import java.util.ArrayList;

public class FoodStock extends Fragment  {

    public static ArrayList<FoodItem> foodList;
    private RecyclerView mRecyclerView;
    public static FoodListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button addBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_food_stock, container, false);

        addBtn=view.findViewById(R.id.add_food_item);

        foodList = new ArrayList<>();
        if(Integer.parseInt(Login.birthDayET.getText().toString())<=20){
            foodList.add(new FoodItem("חמאה", 200, "גרם","https://dairyfarmersofcanada.ca/sites/default/files/product_butter_thumb.jpg"));
            foodList.add(new FoodItem("קמח", 2, "קילוגרם","https://www.apk-inform.com/uploads/Redakciya/2019/%D0%98%D1%8E%D0%BD%D1%8C/%D0%BC%D1%83%D0%BA%D0%B0.jpg"));
            foodList.add(new FoodItem("ביצים", 12, "יחידות","https://chriskresser.com/wp-content/uploads/iStock-172696992.jpg"));

        }else {
            foodList.add(new FoodItem("חמאה", 200, "גרם"));
            foodList.add(new FoodItem("קמח", 2, "קילוגרם"));
            foodList.add(new FoodItem("ביצים", 12, "יחידות"));
        }
        mRecyclerView = view.findViewById(R.id.food_stock_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new FoodListAdapter(foodList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new FoodListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent=new Intent(getContext(), EditFoodList.class);
                intent.putExtra("foodItem",foodList.get(position));
                startActivity(intent);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), AddFoodList.class);
                startActivity(intent);

            }
        });


        return view;


    }



}
