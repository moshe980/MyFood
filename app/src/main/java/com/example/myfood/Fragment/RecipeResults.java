package com.example.myfood.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfood.Adapter.RecipeListAdapter;
import com.example.myfood.Class.Family;
import com.example.myfood.Class.FirebaseManager;
import com.example.myfood.Class.RecipeItem;
import com.example.myfood.R;

import java.util.ArrayList;


public class RecipeResults extends Fragment {
    private RecyclerView mRecyclerView;
    public static RecipeListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public ArrayList<RecipeItem> recipesList;
    public ArrayList<RecipeItem> myRecipesList;
    private String currentCategory;
    private String searchText;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_recipe_results, container, false);

        Switch aSwitch = view.findViewById(R.id.mySwitch);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            currentCategory = String.valueOf(bundle.getString("category"));
            searchText = String.valueOf(bundle.getString("searchET"));
        }

        mRecyclerView = view.findViewById(R.id.recipe_results_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        recipesList = new ArrayList<RecipeItem>();
        myRecipesList = new ArrayList<RecipeItem>();

        recipesList.clear();

        FirebaseManager.getInstance().getRecipes(new FirebaseManager.FirebaseCallBack() {
            @Override
            public void onCallback(FirebaseManager.FirebaseResult result) {
                result.getRecipes().parallelStream().forEach(recipe -> {
                    if (searchText.equals("null") && recipe.getCategory().equals(currentCategory)) {
                        recipesList.add(recipe);
                    } else if (!searchText.equals("null") && recipe.getName().contains(searchText)) {
                        recipesList.add(recipe);

                    }
                });
                mAdapter = new RecipeListAdapter(recipesList);
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(new RecipeListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("chosenRecipe", recipesList.get(position));
                        //set Fragmentclass Arguments
                        Recipe recipe = new Recipe();
                        recipe.setArguments(bundle);
                        getParentFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, recipe, "recipe").addToBackStack("recipeResults").commit();
                    }
                });
            }
        });


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myRecipesList.clear();
                    FirebaseManager.getInstance().getRecipes(new FirebaseManager.FirebaseCallBack() {
                        @Override
                        public void onCallback(FirebaseManager.FirebaseResult result) {
                            result.getRecipes().parallelStream().forEach(recipe -> {
                                boolean flag = false;
                                if (searchText.equals("null") && recipe.getCategory().equals(currentCategory)) {
                                    for (int i = 0; i < recipe.getIngredients().size(); i++) {
                                        for (int j = 0; j < Family.getInstance().getFoodList().size(); j++) {
                                            if (!recipe.getIngredients().get(i).get("שם").toString().contains(Family.getInstance().getFoodList().get(j).getFoodDescription())) {
                                                flag = true;
                                            }
                                        }
                                    }
                                    if (!flag && Family.getInstance().getFoodList().size() > 0) {
                                        myRecipesList.add(recipe);

                                    }

                                } else if (!searchText.equals("null") && recipe.getName().contains(searchText)) {
                                    for (int i = 0; i < recipe.getIngredients().size(); i++) {
                                        for (int j = 0; j < Family.getInstance().getFoodList().size(); j++) {
                                            if (!recipe.getIngredients().get(i).get("שם").toString().contains(Family.getInstance().getFoodList().get(j).getFoodDescription())) {
                                                flag = true;
                                            }
                                        }
                                    }
                                    if (!flag && Family.getInstance().getFoodList().size() > 0) {
                                        myRecipesList.add(recipe);

                                    }
                                }
                            });
                        }
                    });


                    mAdapter = new RecipeListAdapter(myRecipesList);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);

                    mAdapter.setOnItemClickListener(new RecipeListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("chosenRecipe", myRecipesList.get(position));
                            //set Fragmentclass Arguments
                            Recipe recipe = new Recipe();
                            recipe.setArguments(bundle);
                            getParentFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, recipe, "recipe").addToBackStack("recipeResults").commit();
                        }
                    });


                } else {
                    recipesList.clear();
                    FirebaseManager.getInstance().getRecipes(new FirebaseManager.FirebaseCallBack() {
                        @Override
                        public void onCallback(FirebaseManager.FirebaseResult result) {
                            result.getRecipes().parallelStream().forEach(recipe -> {
                                if (searchText.equals("null") && recipe.getCategory().equals(currentCategory)) {
                                    recipesList.add(recipe);
                                } else if (!searchText.equals("null") && recipe.getName().contains(searchText)) {
                                    recipesList.add(recipe);

                                }
                            });
                            mAdapter = new RecipeListAdapter(recipesList);
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            mRecyclerView.setAdapter(mAdapter);

                            mAdapter.setOnItemClickListener(new RecipeListAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("chosenRecipe", recipesList.get(position));
                                    //set Fragmentclass Arguments
                                    Recipe recipe = new Recipe();
                                    recipe.setArguments(bundle);
                                    getParentFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, recipe, "recipe").addToBackStack("recipeResults").commit();
                                }
                            });
                        }
                    });
                }
            }
        });


        return view;

    }


}
