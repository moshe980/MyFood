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

import com.example.myfood.Class.FoodItem;
import com.example.myfood.Fragment.FoodStock;
import com.example.myfood.R;

public class EditFoodList extends Activity {
    private int width;
    private int heigh;
    private FoodItem foodItem;
    private TextView food_nameTV, foodUnitTV;
    private NumberPicker numberPicker;
    private Button updateBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        setContentView(R.layout.activity_edit_food_list);
        Intent intent = getIntent();

        foodItem = (FoodItem) intent.getExtras().getSerializable("foodItem");
        numberPicker = findViewById(R.id.numberPicker);
        food_nameTV = findViewById(R.id.food_name);
        food_nameTV.setText(foodItem.getFoodDiscription());
        foodUnitTV = findViewById(R.id.unitPop);
        updateBtn = findViewById(R.id.update);


        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(1000);

        numberPicker.setValue(foodItem.getAmount());
        foodUnitTV.setText(foodItem.getUnit());

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = -1;
                for (int i = 0; i < FoodStock.foodList.size(); i++) {
                    if (foodItem.equals(FoodStock.foodList.get(i))) {
                        pos = i;
                        break;
                    }
                }
                if (numberPicker.getValue() == 0) {
                    FoodStock.foodList.remove(pos);
                } else
                    FoodStock.foodList.get(pos).setAmount(numberPicker.getValue());
                finish();
                FoodStock.mAdapter.notifyDataSetChanged();
            }
        });


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        width = dm.widthPixels;
        heigh = dm.heightPixels;

        getWindow().setLayout((int) (width * .6), (int) (heigh * .2));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

    }
}
