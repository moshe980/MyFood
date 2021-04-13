package com.example.myfood.Fragment;

import android.os.Bundle;
import android.view.KeyEvent;
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
import com.example.myfood.Class.RecipeItem;
import com.example.myfood.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class RecipeResults extends Fragment {
    private RecyclerView mRecyclerView;
    public static RecipeListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseFirestore db;
    public ArrayList<RecipeItem> recipesList;
    public ArrayList<RecipeItem> myRecipesList;
    private String currentCategory;
    private String searchText;
    private Switch aSwitch;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recipe_results, container, false);

        aSwitch = view.findViewById(R.id.mySwitch);
        db = FirebaseFirestore.getInstance();
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
        db.collection("recipes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Map<String, Object> doc = documentSnapshot.getData();
                    RecipeItem recipeItem = new RecipeItem();
                    recipeItem.setName((String) doc.get("שם"));
                    recipeItem.setCategory((String) doc.get("קטגוריה"));
                    recipeItem.setDifficultLevel((String) doc.get("קושי"));
                    recipeItem.setKashrot((String) doc.get("כשרות"));
                    recipeItem.setImageUrl((String) doc.get("תמונה"));
                    recipeItem.setIngredients((List<Map<String, Object>>) doc.get("רכיבים"));
                    recipeItem.setInstructions((ArrayList<String>) doc.get("הוראות"));
                    if (searchText.equals("null") && recipeItem.getCategory().equals(currentCategory)) {
                        recipesList.add(recipeItem);
                    } else if (!searchText.equals("null") && recipeItem.getName().contains(searchText)) {
                        recipesList.add(recipeItem);

                    }

                }
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
                        getParentFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, recipe).commit();
                    }
                });
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myRecipesList.clear();
                    db.collection("recipes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            boolean flag = false;
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Map<String, Object> doc = documentSnapshot.getData();
                                RecipeItem recipeItem = new RecipeItem();
                                recipeItem.setName((String) doc.get("שם"));
                                recipeItem.setCategory((String) doc.get("קטגוריה"));
                                recipeItem.setDifficultLevel((String) doc.get("קושי"));
                                recipeItem.setKashrot((String) doc.get("כשרות"));
                                recipeItem.setImageUrl((String) doc.get("תמונה"));
                                recipeItem.setIngredients((List<Map<String, Object>>) doc.get("רכיבים"));
                                recipeItem.setInstructions((ArrayList<String>) doc.get("הוראות"));
                                if (searchText.equals("null") && recipeItem.getCategory().equals(currentCategory)) {
                                    for (int i = 0; i < recipeItem.getIngredients().size(); i++) {
                                        for (int j = 0; j < Family.getInstance().getFoodList().size(); j++) {
                                            if (!recipeItem.getIngredients().get(i).get("שם").toString().contains(Family.getInstance().getFoodList().get(j).getFoodDescription())) {
                                                flag = true;
                                            }
                                        }
                                    }
                                    if (!flag && Family.getInstance().getFoodList().size() > 0) {
                                        myRecipesList.add(recipeItem);

                                    }

                                } else if (!searchText.equals("null") && recipeItem.getName().contains(searchText)) {
                                    for (int i = 0; i < recipeItem.getIngredients().size(); i++) {
                                        for (int j = 0; j < Family.getInstance().getFoodList().size(); j++) {
                                            if (!recipeItem.getIngredients().get(i).get("שם").toString().contains(Family.getInstance().getFoodList().get(j).getFoodDescription())) {
                                                flag = true;
                                            }
                                        }
                                    }
                                    if (!flag && Family.getInstance().getFoodList().size() > 0) {
                                        myRecipesList.add(recipeItem);

                                    }
                                }
                                flag = false;
                            }
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
                                    getParentFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, recipe).commit();
                                }
                            });
                        }
                    });
                } else {
                    recipesList.clear();
                    db.collection("recipes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Map<String, Object> doc = documentSnapshot.getData();
                                RecipeItem recipeItem = new RecipeItem();
                                recipeItem.setName((String) doc.get("שם"));
                                recipeItem.setCategory((String) doc.get("קטגוריה"));
                                recipeItem.setDifficultLevel((String) doc.get("קושי"));
                                recipeItem.setKashrot((String) doc.get("כשרות"));
                                recipeItem.setImageUrl((String) doc.get("תמונה"));
                                recipeItem.setIngredients((List<Map<String, Object>>) doc.get("רכיבים"));
                                recipeItem.setInstructions((ArrayList<String>) doc.get("הוראות"));
                                if (searchText.equals("null") && recipeItem.getCategory().equals(currentCategory)) {
                                    recipesList.add(recipeItem);
                                } else if (!searchText.equals("null") && recipeItem.getName().contains(searchText)) {
                                    recipesList.add(recipeItem);

                                }

                            }
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
                                    getParentFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, recipe).commit();
                                }
                            });
                        }
                    });
                }
            }
        });

        //On back press:
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    getParentFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, new SearchRecipe()).commit();
                    return true;
                }
                return false;
            }
        } );
        return view;

    }



}
