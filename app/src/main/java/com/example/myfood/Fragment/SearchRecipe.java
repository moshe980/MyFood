package com.example.myfood.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.myfood.Activity.ManageFood;
import com.example.myfood.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;


public class SearchRecipe extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search_recipe, container, false);

        CardView cardView = view.findViewById(R.id.dessert_category);
        Button searchRecipesBtn=view.findViewById(R.id.search_recipes);
        final EditText searchET=view.findViewById(R.id.searchET);


        searchRecipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchET.getText().toString().equals("")){
                    searchET.setError("נדרש לרשום את שם המתכון");
                }else {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, new RecipeResults()).commit();
                }
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, new RecipeResults()).commit();


            }
        });


        return view;

    }

}
