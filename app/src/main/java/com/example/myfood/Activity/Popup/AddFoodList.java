package com.example.myfood.Activity.Popup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myfood.Activity.Login;
import com.example.myfood.Activity.ManageFood;
import com.example.myfood.Class.Family;
import com.example.myfood.Class.FoodItem;
import com.example.myfood.Class.User;
import com.example.myfood.Fragment.FoodStock;
import com.example.myfood.Fragment.ShoppingList;
import com.example.myfood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AddFoodList extends Activity implements AdapterView.OnItemSelectedListener {
    private int width;
    private int heigh;
    private NumberPicker addNumberPicker;
    private Button addBtn;
    private Spinner categorySpinner;
    private Spinner unitSpinner;
    private String currentCategory;
    private String currentUnit;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private String currentClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        setContentView(R.layout.activity_add_food_list);
        Intent intent = getIntent();
        currentClass = (String) intent.getExtras().getSerializable("Class");

        categorySpinner = findViewById(R.id.category_spinner);
        unitSpinner = findViewById(R.id.unit_spinner);
        addNumberPicker = findViewById(R.id.add_numberPicker);
        addBtn = findViewById(R.id.add_food_item);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        addNumberPicker.setMinValue(1);
        addNumberPicker.setMaxValue(100);


        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this, R.array.categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        categorySpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> unitsAdapter = ArrayAdapter.createFromResource(this, R.array.units, android.R.layout.simple_spinner_item);
        unitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitsAdapter);
        unitSpinner.setOnItemSelectedListener(this);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentClass.equals("ShoppingList")) {
                    myRef = database.getReference("families").child(User.getInstance().getFamilyCode()).child("shoppingList");
                    Family.getInstance().getShoppingList().add(new FoodItem(null, currentCategory, addNumberPicker.getValue(), currentUnit, null));
                    myRef.setValue(Family.getInstance().getShoppingList());
                    ShoppingList.mAdapter.notifyDataSetChanged();
                    finish();
                } else {
                    myRef = database.getReference("families").child(User.getInstance().getFamilyCode()).child("foodStock");
                    Family.getInstance().getFoodList().add(new FoodItem(null, currentCategory, addNumberPicker.getValue(), currentUnit, null));
                    myRef.setValue(Family.getInstance().getFoodList());
                    FoodStock.mAdapter.notifyDataSetChanged();
                    finish();
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
        switch (parent.getId()) {
            case R.id.category_spinner:
                currentCategory = parent.getItemAtPosition(position).toString();
                break;
            case R.id.unit_spinner:
                currentUnit = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}
