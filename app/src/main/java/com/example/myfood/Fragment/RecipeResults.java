package com.example.myfood.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.myfood.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class RecipeResults extends Fragment   {
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recipe_results, container, false);

        ImageButton backBtn=view.findViewById(R.id.back_to_searchRecipe);
        CardView chorus=view.findViewById(R.id.chorus);

        chorus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, new Recipe()).commit();

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, new SearchRecipe()).commit();

            }
        });









        return view;

    }

}
