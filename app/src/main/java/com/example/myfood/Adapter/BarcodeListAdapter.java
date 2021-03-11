package com.example.myfood.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfood.Class.FoodItem;
import com.example.myfood.R;

import java.util.ArrayList;

public class BarcodeListAdapter extends RecyclerView.Adapter<BarcodeListAdapter.BarcodeListViewHolder> {
    private ArrayList<FoodItem> barcodeList;
    private OnItemClickListener mlistener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }

    public static class BarcodeListViewHolder extends RecyclerView.ViewHolder {
        public TextView barcodeTV;
        public TextView barcodeDiscriptionTV;
        public TextView amountTV;
        public TextView unitTV;

        public BarcodeListViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            barcodeTV = itemView.findViewById(R.id.barcodeTV);
            barcodeDiscriptionTV = itemView.findViewById(R.id.barcodeDiscriptionTV);
            amountTV = itemView.findViewById(R.id.amountTV);
            unitTV = itemView.findViewById(R.id.unitTV);

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

    public BarcodeListAdapter(ArrayList<FoodItem> barcodeList) {
        this.barcodeList = barcodeList;
    }

    @NonNull
    @Override
    public BarcodeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.barcode_item, parent, false);
        BarcodeListViewHolder barcodeListViewHolder = new BarcodeListViewHolder(v, mlistener);

        return barcodeListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BarcodeListViewHolder holder, int position) {
        FoodItem currentFoodItem = barcodeList.get(position);

        holder.barcodeTV.setText(currentFoodItem.getBarcode());
        holder.barcodeDiscriptionTV.setText(currentFoodItem.getFoodDescription());
        holder.amountTV.setText(fmt(currentFoodItem.getAmount()));
        if (currentFoodItem.getUnit() != null) {
            holder.unitTV.setText(String.valueOf(currentFoodItem.getUnit()));

        }


    }


    @Override
    public int getItemCount() {
        return barcodeList.size();
    }

    public  String fmt(double d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }
}
