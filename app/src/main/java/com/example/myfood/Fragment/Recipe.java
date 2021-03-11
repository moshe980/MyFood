package com.example.myfood.Fragment;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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

import com.example.myfood.Activity.RecipeMovie;
import com.example.myfood.Adapter.FoodListAdapter;
import com.example.myfood.Class.Family;
import com.example.myfood.Class.FoodItem;
import com.example.myfood.Class.RecipeItem;
import com.example.myfood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import static com.example.myfood.Activity.Login.CHANNEL_1_ID;


public class Recipe extends Fragment {
    private Button add_to_shopping_listBtn, complite_recipeBtn;
    private ImageButton movieBtn;
    private ImageView recipeImage;
    private TextView recipeCategory;
    private TextView kashrot;
    private TextView recipeInstructions;
    private TextView recipeName;
    private RecipeItem chosenRecipe;
    private RecyclerView mRecyclerView;
    public static FoodListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private boolean flag = false;
    private NotificationManagerCompat notificationManager;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_recipe, container, false);

        chosenRecipe = (RecipeItem) getArguments().getSerializable("chosenRecipe");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        ImageButton backBtn = view.findViewById(R.id.back_to_searchRecipe);
        add_to_shopping_listBtn = view.findViewById(R.id.add_to_shopping_list);
        complite_recipeBtn = view.findViewById(R.id.complite_recipe);
        movieBtn = view.findViewById(R.id.recipe_movieBtn);
        recipeImage = view.findViewById(R.id.recipeImage);
        recipeCategory = view.findViewById(R.id.recipeCategory);
        kashrot = view.findViewById(R.id.kashrot);
        recipeInstructions = view.findViewById(R.id.recipeInstructions);
        recipeName = view.findViewById(R.id.recipeName);

        notificationManager = NotificationManagerCompat.from(getContext());
        sendChannel1(view);

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

        for (Map<String, Object> map : chosenRecipe.getIngredients()) {
            FoodItem foodItem = new FoodItem(null, (String) map.get("שם"), Double.parseDouble((String) map.get("כמות")), (String) map.get("יחידה"), null);
            for (int i = 0; i < Family.getInstance().getFoodList().size(); i++) {
                if (Family.getInstance().getFoodList().get(i).getFoodDescription().contains(foodItem.getFoodDescription())) {
                    flag = true;
                }
            }
            if (flag) {
                foodItem.setAvailable("https://www.sh100.co.il/wp-content/uploads/2018/09/fa-check_125_150_007f02_none-425x400-e1538466694132.png");

            } else {
                foodItem.setAvailable("https://upload.wikimedia.org/wikipedia/commons/f/f1/Heavy_red_%22x%22.png");
                unIngredientsList.add(foodItem);
            }
            ingredientsList.add(foodItem);
            flag = false;
        }

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

        movieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RecipeMovie.class);
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
                myRef = database.getReference("users");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String currentFamilyCode;
                        for (DataSnapshot keyNode : snapshot.getChildren()) {
                            if (keyNode.child("email").getValue(String.class).equals(user.getEmail())) {
                                currentFamilyCode = keyNode.child("familyCode").getValue(String.class);
                                myRef = database.getReference("families");
                                String finalCurrentFamilyCode = currentFamilyCode;
                                myRef = database.getReference("families").child(finalCurrentFamilyCode).child("shoppingList");
                                for (int i = 0; i < unIngredientsList.size(); i++) {
                                    unIngredientsList.get(i).setAvailable(null);
                                }
                                myRef.setValue(unIngredientsList);

                                break;

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Toast.makeText(getContext(), "המצרכים נוספו לרשימת הקניות", Toast.LENGTH_SHORT).show();
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
                myRef = database.getReference("users");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int current_num_cooks;
                        for (DataSnapshot keyNode : snapshot.getChildren()) {
                            if (keyNode.child("email").getValue(String.class).equals(user.getEmail())) {
                                current_num_cooks = Integer.parseInt(keyNode.child("num_cooks").getValue(String.class));
                                current_num_cooks++;
                                keyNode.getRef().child("num_cooks").setValue(String.valueOf(current_num_cooks));

                                break;

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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
