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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;

import com.example.myfood.Class.Family;
import com.example.myfood.Class.FirebaseManager;
import com.example.myfood.Class.FoodItem;
import com.example.myfood.Fragment.FoodStock;
import com.example.myfood.Fragment.ShoppingList;
import com.example.myfood.R;

public class AddFoodList extends Activity implements AdapterView.OnItemSelectedListener {
    private int width;
    private int heigh;
    private NumberPicker addNumberPicker;
    private Button addBtn;
    private AutoCompleteTextView addET;
    private Spinner unitSpinner;
    private String currentUnit;
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

        String[] foodNames = getResources().getStringArray(R.array.foods);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, foodNames);
        addET.setAdapter(adapter);

        ArrayAdapter<CharSequence> unitsAdapter = ArrayAdapter.createFromResource(this, R.array.units, android.R.layout.simple_spinner_item);
        unitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitsAdapter);
        unitSpinner.setOnItemSelectedListener(this);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!addET.getText().toString().equals("")) {

                    if (currentClass.equals("ShoppingList")) {
                        Family.getInstance().addToShoppingList(new FoodItem(null, addET.getText().toString(), addNumberPicker.getValue(), currentUnit, null));
                        FirebaseManager.getInstance().setShoppingList(new FirebaseManager.FirebaseCallBack() {
                            @Override
                            public void onCallback(FirebaseManager.FirebaseResult result) {
                                ShoppingList.mAdapter.notifyDataSetChanged();
                                finish();

                            }
                        });
                    } else if(currentClass.equals("FoodStock")) {
                        Family.getInstance().addToFoodList(new FoodItem(null, addET.getText().toString(), addNumberPicker.getValue(), currentUnit, null));
                        FirebaseManager.getInstance().setFoodList(new FirebaseManager.FirebaseCallBack() {
                            @Override
                            public void onCallback(FirebaseManager.FirebaseResult result) {
                                FoodStock.mAdapter.notifyDataSetChanged();
                                finish();

                            }
                        });
                    }
                } else {
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
