package com.example.myfood.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.myfood.R;


public class SearchRecipe extends Fragment {
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search_recipe, container, false);

        CardView meat_category = view.findViewById(R.id.meat_category);
        CardView chicken_category = view.findViewById(R.id.chicken_category);
        CardView sideDishe_category = view.findViewById(R.id.sideDishe_category);
        CardView pasta_category = view.findViewById(R.id.pasta_category);
        CardView breakfast_category = view.findViewById(R.id.breakfast_category);
        CardView dessert_category = view.findViewById(R.id.dessert_category);
        CardView starters_category = view.findViewById(R.id.starters_category);
        CardView vegetarian_category = view.findViewById(R.id.vegetarian_category);
        CardView vegan_category = view.findViewById(R.id.vegan_category);

        Button searchRecipesBtn = view.findViewById(R.id.search_recipes);

        AutoCompleteTextView searchET = view.findViewById(R.id.searchET);
        String[] recipes = getResources().getStringArray(R.array.recipes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, recipes);
        searchET.setAdapter(adapter);
        searchRecipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchET.getText().toString().equals("")) {
                    searchET.setError("נדרש לרשום את שם המתכון");
                } else {
                    RecipeResults recipeResults = new RecipeResults();
                    Bundle bundle = new Bundle();
                    bundle.putString("searchET", searchET.getText().toString());
                    recipeResults.setArguments(bundle);
                    getParentFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, recipeResults,"recipeResults").addToBackStack("SearchRecipe").commit();
                }
            }
        });

        meat_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeResults recipeResults = new RecipeResults();
                Bundle bundle = new Bundle();
                bundle.putString("category", "בקר");
                recipeResults.setArguments(bundle);

                getParentFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, recipeResults,"recipeResults").addToBackStack("SearchRecipe").commit();


            }
        });
        chicken_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeResults recipeResults = new RecipeResults();
                Bundle bundle = new Bundle();
                bundle.putString("category", "עוף");
                recipeResults.setArguments(bundle);

                getParentFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, recipeResults,"recipeResults").addToBackStack("SearchRecipe").commit();
            }
        });
        sideDishe_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeResults recipeResults = new RecipeResults();
                Bundle bundle = new Bundle();
                bundle.putString("category", "תוספות");
                recipeResults.setArguments(bundle);

                getParentFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, recipeResults,"recipeResults").addToBackStack("SearchRecipe").commit();
            }
        });
        pasta_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeResults recipeResults = new RecipeResults();
                Bundle bundle = new Bundle();
                bundle.putString("category", "פסטה");
                recipeResults.setArguments(bundle);

                getParentFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, recipeResults,"recipeResults").addToBackStack("SearchRecipe").commit();


            }
        });
        breakfast_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeResults recipeResults = new RecipeResults();
                Bundle bundle = new Bundle();
                bundle.putString("category", "ארוחות בוקר");
                recipeResults.setArguments(bundle);

                getParentFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, recipeResults,"recipeResults").addToBackStack("SearchRecipe").commit();


            }
        });
        dessert_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeResults recipeResults = new RecipeResults();
                Bundle bundle = new Bundle();
                bundle.putString("category", "קינוחים");
                recipeResults.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, recipeResults,"recipeResults").addToBackStack("SearchRecipe").commit();



            }
        });
        starters_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeResults recipeResults = new RecipeResults();
                Bundle bundle = new Bundle();
                bundle.putString("category", "ראשונות");
                recipeResults.setArguments(bundle);

                getParentFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, recipeResults,"recipeResults").addToBackStack("SearchRecipe").commit();


            }
        });
        vegetarian_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeResults recipeResults = new RecipeResults();
                Bundle bundle = new Bundle();
                bundle.putString("category", "צמחוני");
                recipeResults.setArguments(bundle);

                getParentFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, recipeResults,"recipeResults").addToBackStack("SearchRecipe").commit();


            }
        });
        vegan_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecipeResults recipeResults = new RecipeResults();
                Bundle bundle = new Bundle();
                bundle.putString("category", "טבעוני");
                recipeResults.setArguments(bundle);

                getParentFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, recipeResults,"recipeResults").addToBackStack("SearchRecipe").commit();


            }
        });


        return view;

    }

}
