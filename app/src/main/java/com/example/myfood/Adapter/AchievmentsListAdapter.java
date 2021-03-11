package com.example.myfood.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfood.Class.Achievement;
import com.example.myfood.Class.FoodItem;
import com.example.myfood.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AchievmentsListAdapter extends RecyclerView.Adapter<AchievmentsListAdapter.AchievementsListViewHolder> {
    private ArrayList<Achievement> achievmentsList;
    private OnItemClickListener mlistener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }

    public static class AchievementsListViewHolder extends RecyclerView.ViewHolder {
        public TextView id;
        public TextView name;
        public TextView status;
        public TextView goal;

        public AchievementsListViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            id = itemView.findViewById(R.id.achievments_idTV);
            name = itemView.findViewById(R.id.achievments_nameTV);
            status=itemView.findViewById(R.id.achievments_statusTV);
            goal=itemView.findViewById(R.id.achievments_goalTV);

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

    public AchievmentsListAdapter(ArrayList<Achievement> achievementsList) {
        this.achievmentsList = achievementsList;
    }

    @NonNull
    @Override
    public AchievementsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.achievment_item, parent, false);
        AchievementsListViewHolder achievementsListViewHolder = new AchievementsListViewHolder(v, mlistener);

        return achievementsListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementsListViewHolder holder, int position) {
        Achievement currentAchievementItem = achievmentsList.get(position);

        holder.id.setText(currentAchievementItem.getId());
        holder.name.setText(currentAchievementItem.getName());
        holder.status.setText(String.valueOf(currentAchievementItem.getStatus()));
        holder.goal.setText(currentAchievementItem.getGoal());

    }

    @Override
    public int getItemCount() {
        return achievmentsList.size();
    }
}
