package com.example.myfood.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfood.Class.RecipeItem;
import com.example.myfood.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> implements Filterable {
    private ArrayList<RecipeItem> recipeList;
    private ArrayList<RecipeItem> recipeListFull;

    private OnItemClickListener mlistener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }

    public static class RecipeListViewHolder extends RecyclerView.ViewHolder {
        public ImageView recipeImage;
        public TextView recipeName;

        public RecipeListViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            recipeName = itemView.findViewById(R.id.recipeName);

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

    public RecipeListAdapter(ArrayList<RecipeItem> recipeList) {
        this.recipeList = recipeList;
        this.recipeListFull=new ArrayList<RecipeItem>(recipeList);
    }

    @NonNull
    @Override
    public RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipie_result_item, parent, false);
        RecipeListViewHolder recipeListViewHolder = new RecipeListViewHolder(v, mlistener);

        return recipeListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListViewHolder holder, int position) {
        RecipeItem currentRecipeItem = recipeList.get(position);

        holder.recipeName.setText(currentRecipeItem.getName());
        Picasso.get()
                .load(currentRecipeItem.getImageUrl())
                .fit()
                .centerCrop()
                .into(holder.recipeImage);

    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<RecipeItem> filteredRecipeList=new ArrayList<RecipeItem>();
            
            if (constraint==null||constraint.length() == 0){
                filteredRecipeList.addAll(recipeListFull);
            }else{
                String filteredPattern=constraint.toString().toLowerCase().trim();

                for(RecipeItem item:recipeListFull){
                    if(item.getName().toLowerCase().contains(filteredPattern)){
                        filteredRecipeList.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredRecipeList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            recipeList.clear();
            recipeList.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };
}
