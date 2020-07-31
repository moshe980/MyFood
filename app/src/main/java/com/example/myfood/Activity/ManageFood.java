package com.example.myfood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.myfood.Fragment.Achievements;
import com.example.myfood.Fragment.FoodStock;
import com.example.myfood.Fragment.Scan;
import com.example.myfood.Fragment.SearchRecipe;
import com.example.myfood.Fragment.Settings;
import com.example.myfood.Fragment.ShoppingList;
import com.example.myfood.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class ManageFood extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        setContentView(R.layout.activity_manage_food);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.nav_Food_stock);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, new FoodStock()).commit();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState==null){
            navigationView.setCheckedItem(R.id.nav_Food_stock);
        }

        if (Integer.parseInt(Login.birthDayET.getText().toString()) >= 20) {
            Menu nav_Menu = navigationView.getMenu();
            nav_Menu.findItem(R.id.nav_achievements).setVisible(false);

        }

        }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        TextView toolbar_text = findViewById(R.id.toolbar_text);

      
        switch (menuItem.getItemId()){
            case R.id.nav_manage_food:
                bottomNav.setVisibility(View.VISIBLE);
                toolbar_text.setText("מלאי המזון");
                bottomNav.setSelectedItemId(R.id.nav_Food_stock);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, new FoodStock()).commit();

                break;
            case R.id.nav_search_recipe:
                bottomNav.setVisibility(View.GONE);
                toolbar_text.setText("חפש מתכונים");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container,
                        new SearchRecipe()).commit();
                break;
            case R.id.nav_achievements:
                bottomNav.setVisibility(View.GONE);
                toolbar_text.setText("משימות");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container,
                        new Achievements()).commit();
                break;
            case R.id.nav_log_out:
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                finish();
                break;
        }
        drawer.closeDrawer(Gravity.RIGHT);
        return true;
    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    TextView toolbar_text = findViewById(R.id.toolbar_text);

                    switch (menuItem.getItemId()) {
                        case R.id.nav_Shopping_List:
                            toolbar_text.setText("רשימת הקניות");
                            selectedFragment = new ShoppingList();
                            break;
                        case R.id.nav_Scan:
                            toolbar_text.setText("סריקת קבלה");
                            selectedFragment = new Scan();
                            break;
                        case R.id.nav_Food_stock:
                            toolbar_text.setText("מלאי המזון");
                            selectedFragment = new FoodStock();
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, selectedFragment).commit();

                    return true;
                }
            };


}
