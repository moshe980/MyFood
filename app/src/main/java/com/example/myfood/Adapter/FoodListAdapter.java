package com.example.myfood.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfood.Class.FoodItem;
import com.example.myfood.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.FoodListViewHolder> {
    private ArrayList<FoodItem> foodList;
    private OnItemClickListener mlistener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }

    public static class FoodListViewHolder extends RecyclerView.ViewHolder {
        public ImageView foodImage;
        public TextView foodDiscription;
        public TextView amount;
        public TextView unit;

        public FoodListViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            foodDiscription = itemView.findViewById(R.id.foodDiscriptionTV);
            amount = itemView.findViewById(R.id.amountTV);
            unit = itemView.findViewById(R.id.unitTV);
            foodImage = itemView.findViewById(R.id.food_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }

    public FoodListAdapter(ArrayList<FoodItem> foodList) {
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public FoodListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        FoodListViewHolder foodListViewHolder = new FoodListViewHolder(v, mlistener);

        return foodListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodListViewHolder holder, int position) {
        FoodItem currentFoodItem = foodList.get(position);

        holder.foodDiscription.setText(currentFoodItem.getFoodDiscription());
        holder.amount.setText(String.valueOf(currentFoodItem.getAmount()));
        holder.unit.setText(currentFoodItem.getUnit());
        Picasso.get()
                .load(currentFoodItem.getUrl())
                .fit()
                .centerCrop()
                .into(holder.foodImage);

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
