package com.example.myfood.Fragment;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfood.Adapter.FoodListAdapter;
import com.example.myfood.Class.Family;
import com.example.myfood.Class.FirebaseManager;
import com.example.myfood.Class.FoodItem;
import com.example.myfood.Class.RecipeItem;
import com.example.myfood.Class.RightJustifyAlertDialog;
import com.example.myfood.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.myfood.Activity.Login.CHANNEL_1_ID;


public class Recipe extends Fragment {
    private Button add_to_shopping_listBtn, complite_recipeBtn;
    private ImageView recipeImage;
    private TextView recipeCategory;
    private TextView kashrot;
    private TextView recipeInstructions;
    private TextView recipeName;
    private RecipeItem chosenRecipe;
    private RecyclerView mRecyclerView;
    public static FoodListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean flag = false;
    private NotificationManagerCompat notificationManager;
    private String dialogResult;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        chosenRecipe = (RecipeItem) getArguments().getSerializable("chosenRecipe");
        add_to_shopping_listBtn = view.findViewById(R.id.add_to_shopping_list);
        complite_recipeBtn = view.findViewById(R.id.complite_recipe);
        recipeImage = view.findViewById(R.id.recipeImage);
        recipeCategory = view.findViewById(R.id.recipeCategory);
        kashrot = view.findViewById(R.id.kashrot);
        recipeInstructions = view.findViewById(R.id.recipeInstructions);
        recipeName = view.findViewById(R.id.recipeName);

        notificationManager = NotificationManagerCompat.from(getContext());
        //  sendChannel1(view);

        Picasso.get()
                .load(chosenRecipe.getImageUrl())
                .fit()
                .centerCrop()
                .into(recipeImage);
        recipeName.setText(chosenRecipe.getName());
        recipeCategory.setText(chosenRecipe.getCategory());
        kashrot.setText(chosenRecipe.getKashrot());

        //recipeIngredients:
        mRecyclerView = view.findViewById(R.id.ingredients_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        ArrayList<FoodItem> ingredientsList = new ArrayList<>();
        ArrayList<FoodItem> unIngredientsList = new ArrayList<>();

        chosenRecipe.getIngredients().parallelStream().forEach(ingredient -> {
            FoodItem recipeItem = new FoodItem(null, (String) ingredient.get("שם"), Double.parseDouble((String) ingredient.get("כמות")),
                    (String) ingredient.get("יחידה"), null);
            recipeItem.setAlternativeList((ArrayList<String>) ingredient.get("תחליפיים"));
            Family.getInstance().getFoodList().parallelStream().forEach(currentFoodItem -> {
                if (currentFoodItem.getFoodDescription().contains(recipeItem.getFoodDescription()) &&
                        currentFoodItem.getAmount() * currentFoodItem.convertAmount() >= recipeItem.getAmount() * recipeItem.convertAmount()) {
                    flag = true;
                }
            });
            if (flag) {
                recipeItem.setAvailable(R.drawable.green_v);

            } else {
                if (recipeItem.getAlternativeList() == null) {
                    recipeItem.setAvailable(R.drawable.red_x);
                } else {
                    for (String alternative : recipeItem.getAlternativeList()) {
                        Family.getInstance().getFoodList().parallelStream().forEach(currentFoodItem -> {
                            if (currentFoodItem.getFoodDescription().contains(alternative) &&
                                    currentFoodItem.getAmount() * currentFoodItem.convertAmount() >= recipeItem.getAmount() * recipeItem.convertAmount()) {
                                flag = true;
                            }
                        });
                        if (flag) {
                            recipeItem.setAvailable(R.drawable.yellow);

                        } else {
                            recipeItem.setAvailable(R.drawable.red_x);

                        }
                    }
                }
                unIngredientsList.add(recipeItem);

            }
            ingredientsList.add(recipeItem);
            flag = false;
        });


        if (unIngredientsList.size() > 0) {
            complite_recipeBtn.setVisibility(View.INVISIBLE);
            add_to_shopping_listBtn.setVisibility(View.VISIBLE);
        } else {
            complite_recipeBtn.setVisibility(View.VISIBLE);
            add_to_shopping_listBtn.setVisibility(View.INVISIBLE);

        }
        mAdapter = new FoodListAdapter(ingredientsList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        //recipeInstructions:
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < chosenRecipe.getInstructions().size(); i++) {
            stringBuilder.append(i + 1 + ". " + chosenRecipe.getInstructions().get(i) + "\n" + "\n");
        }
        recipeInstructions.setText(stringBuilder);

        mAdapter.setOnItemClickListener(new FoodListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (ingredientsList.get(position).getAlternativeList() != null) {
                    int checkedItem = -1;


                    //missing ingredient
                    MaterialAlertDialogBuilder builder = new RightJustifyAlertDialog(getActivity());
                    builder.setTitle("מצרכים חלופיים")
                            .setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    flag = false;
                                    String oldIngredient = ingredientsList.get(position).getFoodDescription();
                                    ingredientsList.get(position).setFoodDescription(dialogResult);
                                    ingredientsList.get(position).getAlternativeList().add(oldIngredient);
                                    ingredientsList.get(position).getAlternativeList().remove(ingredientsList.get(position).getAlternativeList().indexOf(dialogResult));
                                    Family.getInstance().getFoodList().parallelStream().forEach(currentFoodItem -> {
                                        if (currentFoodItem.getFoodDescription().contains(dialogResult) &&
                                                currentFoodItem.getAmount() * currentFoodItem.convertAmount() >= ingredientsList.get(position).getAmount() * ingredientsList.get(position).convertAmount()) {
                                            flag = true;
                                        }
                                    });
                                    if (flag) {
                                        ingredientsList.get(position).setAvailable(R.drawable.green_v);
                                        unIngredientsList.remove(ingredientsList.get(position));


                                    } else {
                                        ingredientsList.get(position).setAvailable(R.drawable.red_x);

                                    }
                                    mRecyclerView.setAdapter(mAdapter);


                                }
                            });
                    builder.setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.setSingleChoiceItems(new ArrayAdapter<String>(getActivity(), R.layout.rtl_list_item, R.id.text, ingredientsList.get(position).getAlternativeList()), checkedItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogResult = ingredientsList.get(position).getAlternativeList().get(i);
                        }
                    }).show();
                }
            }

            @Override
            public void onItemLongClickListener(int position) {

            }
        });
        add_to_shopping_listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < unIngredientsList.size(); i++) {
                    unIngredientsList.get(i).setAvailable(0);
                }
                Family.getInstance().addAllToShoppingList(unIngredientsList);
                FirebaseManager.getInstance().setShoppingList(new FirebaseManager.FirebaseCallBack() {
                    @Override
                    public void onCallback(FirebaseManager.FirebaseResult result) {
                        Toast.makeText(getContext(), "המצרכים נוספו לרשימת הקניות", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        complite_recipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < ingredientsList.size(); i++) {
                    for (int j = 0; j < Family.getInstance().getFoodList().size(); j++) {
                        if (Family.getInstance().getFoodList().get(i).getFoodDescription().contains(ingredientsList.get(i).getFoodDescription())) {
                            Family.getInstance().getFoodList().get(i).setAmount((Family.getInstance().getFoodList().get(i).getAmount() - ingredientsList.get(i).getAmount()));
                        }

                    }
                }

                FirebaseManager.getInstance().updateNum_cooks(new FirebaseManager.FirebaseCallBack() {
                    @Override
                    public void onCallback(FirebaseManager.FirebaseResult result) {

                    }
                });
                Toast.makeText(getContext(), "מלאי המזון עודכן בהצלה", Toast.LENGTH_SHORT).show();
            }
        });


        return view;


    }


    public void sendChannel1(View v) {

        Intent activityIntent = new Intent(getContext(), Achievements.class);
        activityIntent.putExtra("From", "notifyFrag");
        PendingIntent contentIntent = PendingIntent.getActivity(getContext(),
                0, activityIntent, 0);

        Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_videogame_asset_black_24dp)
                .setContentTitle("הישגים")
                .setContentText("השלמת הישיג!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .build();

        notificationManager.notify(1, notification);
    }


}