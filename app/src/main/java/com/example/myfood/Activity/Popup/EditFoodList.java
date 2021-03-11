package com.example.myfood.Activity.Popup;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myfood.Activity.ManageFood;
import com.example.myfood.Class.Family;
import com.example.myfood.Class.FoodItem;
import com.example.myfood.Class.User;
import com.example.myfood.Fragment.FoodStock;
import com.example.myfood.Fragment.Scan;
import com.example.myfood.Fragment.ShoppingList;
import com.example.myfood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditFoodList extends Activity {
    private int width;
    private int heigh;
    private FoodItem currentFoodItem;
    private FoodItem currentBarcodeItem;
    private FoodItem currentShoppingItem;

    private TextView food_nameTV, foodUnitTV;
    private NumberPicker numberPicker;
    private Button updateBtn;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private String currentClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        setContentView(R.layout.activity_edit_food_list);
        Intent intent = getIntent();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        currentFoodItem = (FoodItem) intent.getExtras().getSerializable("foodItem");
        currentBarcodeItem = (FoodItem) intent.getExtras().getSerializable("barcodeItem");
        currentShoppingItem = (FoodItem) intent.getExtras().getSerializable("shoppingFoodItem");
        currentClass = (String) intent.getExtras().getSerializable("Class");

        numberPicker = findViewById(R.id.numberPicker);
        food_nameTV = findViewById(R.id.food_name);
        foodUnitTV = findViewById(R.id.unitPop);
        updateBtn = findViewById(R.id.update);


        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(100);
        //show details about the chosen item:
        if (currentFoodItem != null) {
            food_nameTV.setText(currentFoodItem.getFoodDescription());
            numberPicker.setValue(((int) currentFoodItem.getAmount()));

        } else if (currentBarcodeItem != null) {
            food_nameTV.setText(currentBarcodeItem.getBarcode());
            numberPicker.setValue(((int) currentBarcodeItem.getAmount()));
        } else if (currentShoppingItem != null) {
            food_nameTV.setText(currentShoppingItem.getFoodDescription());
            numberPicker.setValue(((int) currentShoppingItem.getAmount()));
        }


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = -1;

                switch (currentClass) {
                    case "FoodStock":
                        if (numberPicker.getValue() == 0) {
                            Family.getInstance().getShoppingList().add(currentFoodItem);
                            Family.getInstance().getFoodList().remove(currentFoodItem);
                            myRef = database.getReference("families").child(User.getInstance().getFamilyCode()).child("shoppingList");
                            myRef.setValue(Family.getInstance().getShoppingList());
                            myRef = database.getReference("families").child(User.getInstance().getFamilyCode()).child("foodStock");
                            myRef.setValue(Family.getInstance().getFoodList());
                            FoodStock.mAdapter.notifyDataSetChanged();
                            ShoppingList.mAdapter.notifyDataSetChanged();
                            finish();


                        } else {
                            for (int i = 0; i < Family.getInstance().getFoodList().size(); i++) {
                                if (currentFoodItem.getFoodDescription().equals(Family.getInstance().getFoodList().get(i).getFoodDescription())) {
                                    pos = i;
                                    break;
                                }
                            }
                            Family.getInstance().getFoodList().get(pos).setAmount(numberPicker.getValue());
                            String currentFamilyCode;
                            myRef = database.getReference("families").child(User.getInstance().getFamilyCode()).child("foodStock");
                            myRef.setValue(Family.getInstance().getFoodList());
                            FoodStock.mAdapter.notifyDataSetChanged();
                            finish();
                            break;


                        }
                        break;
                    case "ShoppingList":
                        if (numberPicker.getValue() == 0) {
                            Family.getInstance().getShoppingList().remove(currentShoppingItem);
                            myRef = database.getReference("families").child(User.getInstance().getFamilyCode()).child("shoppingList");
                            myRef.setValue(Family.getInstance().getShoppingList());
                            ShoppingList.mAdapter.notifyDataSetChanged();
                            finish();


                        } else {
                            for (int i = 0; i < Family.getInstance().getShoppingList().size(); i++) {
                                if (currentShoppingItem.getFoodDescription().equals(Family.getInstance().getShoppingList().get(i).getFoodDescription())) {
                                    pos = i;
                                    break;
                                }
                            }
                            Family.getInstance().getShoppingList().get(pos).setAmount(numberPicker.getValue());
                            myRef = database.getReference("families").child(User.getInstance().getFamilyCode()).child("shoppingList");
                            myRef.setValue(Family.getInstance().getShoppingList());
                            ShoppingList.mAdapter.notifyDataSetChanged();
                            finish();


                        }
                        break;
                    case "Scan":
                        if (currentBarcodeItem != null) {
                            for (int i = 0; i < ManageFood.barcodes.size(); i++) {
                                if (currentBarcodeItem.getBarcode().equals(ManageFood.barcodes.get(i).getBarcode())) {
                                    pos = i;
                                    break;
                                }
                            }
                            if (numberPicker.getValue() == 0) {
                                ManageFood.barcodes.remove(pos);
                            } else
                                ManageFood.barcodes.get(pos).setAmount(numberPicker.getValue());
                            finish();
                            Scan.barcodesAdapter.notifyDataSetChanged();
                        }
                        break;

                }
            }
        });


        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().

                getDefaultDisplay().

                getMetrics(dm);

        width = dm.widthPixels;
        heigh = dm.heightPixels;

        getWindow().

                setLayout((int) (width * .6), (int) (heigh * .2));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        // params.x = 0;
        //  params.y = -20;

        getWindow().

                setAttributes(params);

    }
}
