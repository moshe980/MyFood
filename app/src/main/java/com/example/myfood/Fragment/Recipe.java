package com.example.myfood.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.myfood.Activity.RecipeMovie;
import com.example.myfood.R;


public class Recipe extends Fragment {
    private Button add_to_shopping_listBtn, complite_recipeBtn;
    private ImageButton movieBtn;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        ImageButton backBtn = view.findViewById(R.id.back_to_searchRecipe);
        add_to_shopping_listBtn = view.findViewById(R.id.add_to_shopping_list);
        complite_recipeBtn = view.findViewById(R.id.complite_recipe);
        movieBtn = view.findViewById(R.id.recipe_movieBtn);


        movieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), RecipeMovie.class);
                startActivity(intent);
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, new SearchRecipe()).commit();

            }
        });

        add_to_shopping_listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "המצרכים נוספו לרשימת הקניות", Toast.LENGTH_SHORT).show();
            }
        });

        complite_recipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "מלאי המזון עודכן בהצלה", Toast.LENGTH_SHORT).show();
            }
        });


        return view;

    }

}
