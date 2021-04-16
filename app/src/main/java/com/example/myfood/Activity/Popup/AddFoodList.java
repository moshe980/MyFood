package com.example.myfood.Activity.Popup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.example.myfood.Class.Family;
import com.example.myfood.Class.FoodItem;
import com.example.myfood.Class.User;
import com.example.myfood.Fragment.FoodStock;
import com.example.myfood.Fragment.ShoppingList;
import com.example.myfood.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFoodList extends Activity implements AdapterView.OnItemSelectedListener {
    private int width;
    private int heigh;
    private NumberPicker addNumberPicker;
    private Button addBtn;
    private TextInputLayout addET;
    private Spinner unitSpinner;
    private String currentUnit;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private String currentClass;
    boolean flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setContentView(R.layout.activity_add_food_list);
        Intent intent = getIntent();
        currentClass = (String) intent.getExtras().getSerializable("Class");

        addET = findViewById(R.id.addET);
        unitSpinner = findViewById(R.id.unit_spinner);
        addNumberPicker = findViewById(R.id.add_numberPicker);
        addBtn = findViewById(R.id.add_food_item);
        addNumberPicker.setMinValue(1);
        addNumberPicker.setMaxValue(1000);


        ArrayAdapter<CharSequence> unitsAdapter = ArrayAdapter.createFromResource(this, R.array.units, android.R.layout.simple_spinner_item);
        unitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitsAdapter);
        unitSpinner.setOnItemSelectedListener(this);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!addET.getEditText().getText().toString().equals("")) {
                    if (currentClass.equals("ShoppingList")) {
                        myRef = database.getReference("families").child(User.getInstance().getFamilyCode()).child("shoppingList");
                        Family.getInstance().getShoppingList().add(new FoodItem(null, addET.getEditText().getText().toString(), addNumberPicker.getValue(), currentUnit, null));
                        myRef.setValue(Family.getInstance().getShoppingList());
                        ShoppingList.mAdapter.notifyDataSetChanged();
                        finish();
                    } else {
                        myRef = database.getReference("families").child(User.getInstance().getFamilyCode()).child("foodStock");
                        Family.getInstance().getFoodList().parallelStream().forEach(foodItem -> {
                            if (foodItem.getFoodDescription().equals(addET.getEditText().getText().toString())&&foodItem.getUnit().equals(currentUnit)) {
                                foodItem.setAmount(foodItem.getAmount()+addNumberPicker.getValue());
                                flag=true;
                            }
                        } );
                        if(!flag) {
                            Family.getInstance().getFoodList().add(new FoodItem(null, addET.getEditText().getText().toString(), addNumberPicker.getValue(), currentUnit, null));
                        }
                        flag=false;
                        myRef.setValue(Family.getInstance().getFoodList());
                        FoodStock.mAdapter.notifyDataSetChanged();
                        finish();
                    }
                }else{
                    addET.setError("נדרש להשלים שם מוצר");
                }

            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        width = dm.widthPixels;
        heigh = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (heigh * .4));


        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentUnit = parent.getItemAtPosition(position).toString();
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
