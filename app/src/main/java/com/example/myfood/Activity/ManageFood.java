package com.example.myfood.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.canhub.cropper.CropImage;
import com.example.myfood.Activity.Popup.EditFoodList;
import com.example.myfood.Adapter.BarcodeListAdapter;
import com.example.myfood.Class.FirebaseManager;
import com.example.myfood.Class.FoodItem;
import com.example.myfood.Class.User;
import com.example.myfood.Fragment.Achievements;
import com.example.myfood.Fragment.FoodStock;
import com.example.myfood.Fragment.Scan;
import com.example.myfood.Fragment.SearchRecipe;
import com.example.myfood.Fragment.ShoppingList;
import com.example.myfood.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ManageFood extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Context context;
    public static ArrayList<FoodItem> barcodes;
    public static ArrayList<FoodItem> unidentified_barcodes;

    public NavigationView navigationView;
    public TextView nameSlideTV;
    public TextView familiyCodeSlideTV;
    public TextView userScoreTV;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setContentView(R.layout.activity_manage_food);

        context = this;
        barcodes = new ArrayList();
        unidentified_barcodes = new ArrayList();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.nav_Food_stock);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, new FoodStock()).commit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        nameSlideTV = headerView.findViewById(R.id.nameSlide);
        familiyCodeSlideTV = headerView.findViewById(R.id.familiyCodeSlide);
        userScoreTV = headerView.findViewById(R.id.userScore);

        String type = getIntent().getStringExtra("From");
        if (type != null) {
            switch (type) {
                case "notifyFrag":
                    TextView toolbar_text = findViewById(R.id.toolbar_text);
                    bottomNav.setVisibility(View.GONE);
                    toolbar_text.setText("משימות");
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container,
                            new Achievements()).commit();
                    break;
            }
        }
        nameSlideTV.setText(User.getInstance().getFirstName() + " " + User.getInstance().getLastName());
        familiyCodeSlideTV.setText("קוד משפחה: " + User.getInstance().getFamilyCode());
        userScoreTV.setText("ניקוד שצברתי: " + User.getInstance().getScore());


        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            navigationView.setCheckedItem(R.id.nav_Food_stock);
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        TextView toolbar_text = findViewById(R.id.toolbar_text);


        switch (menuItem.getItemId()) {
            case R.id.nav_manage_food:
                bottomNav.setVisibility(View.VISIBLE);
                toolbar_text.setText("מלאי המזון");
                bottomNav.setSelectedItemId(R.id.nav_Food_stock);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, new FoodStock()).commit();

                break;
            case R.id.nav_search_recipe:
                bottomNav.setVisibility(View.GONE);
                toolbar_text.setText("חפש מתכונים");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container, new SearchRecipe()).commit();
                break;
            case R.id.nav_achievements:
                bottomNav.setVisibility(View.GONE);
                toolbar_text.setText("משימות");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_bottom_container,
                        new Achievements()).addToBackStack(null).commit();
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
        }
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count >= 1 && !doubleBackToExitPressedOnce) {
            getSupportFragmentManager().popBackStack();
        } else {
            Toast.makeText(this, "לחץ עוד פעם כדי לצאת!", Toast.LENGTH_SHORT).show();
            this.doubleBackToExitPressedOnce = true;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri imageUri;
        CropImage.ActivityResult result = CropImage.getActivityResult(data);

        if (resultCode == RESULT_OK) {
            Scan.progressBar.setVisibility(View.VISIBLE);
            barcodes.clear();
            imageUri = result.getUri();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                InputImage imageInput = InputImage.fromBitmap(bitmap, 0);

                TextRecognizer recognizer = TextRecognition.getClient();
                Task<Text> resulta = recognizer.process(imageInput).addOnSuccessListener(new OnSuccessListener<Text>() {
                    @Override
                    public void onSuccess(Text visionText) {
                        String resultText = visionText.getText();
                        String[] splitedS = resultText.split(" " + "|\\" + "\n");

                        for (int i = 0; i < splitedS.length; i++) {
                            try {
                                if (splitedS[i].startsWith("729")) {
                                    System.out.println(splitedS[i].subSequence(0, 13));
                                    for (int j = i + 1; j < splitedS.length; j++) {
                                        if (splitedS[j].equals("x")) {
                                            barcodes.add(new FoodItem(splitedS[i].subSequence(0, 13).toString()
                                                    , null, Integer.parseInt(splitedS[j + 1].subSequence(0, 1).toString()), null, null));

                                            break;
                                        } else if (splitedS[j].startsWith("729")) {
                                            barcodes.add(new FoodItem(splitedS[i].subSequence(0, 13).toString(), null, 1, null, null));
                                            break;
                                        }

                                    }
                                } else if (splitedS[i].equals("42442")) {
                                    boolean flag = false;
                                    for (int j = i + 1; j < splitedS.length; j++) {
                                        if (splitedS[j].equals("x")) {
                                            barcodes.add(new FoodItem("42442"
                                                    , null, Integer.parseInt(splitedS[j + 1].subSequence(0, 1).toString()), null, null));

                                            flag = true;
                                            break;
                                        } else if (splitedS[j].startsWith("729")) {
                                            barcodes.add(new FoodItem(splitedS[i].subSequence(0, 13).toString(), null, 1, null, null));
                                            flag = true;
                                            break;
                                        }

                                    }
                                    if (flag == false) {
                                        barcodes.add(new FoodItem("42442", null, 1, null, null));

                                    }
                                    flag = false;
                                }
                            } catch (Exception e) {
                                System.out.println("Error: " + e);

                            }
                        }
                        ArrayList<FoodItem> tmp = new ArrayList<FoodItem>(barcodes);
                        tmp.parallelStream().forEach(barcode -> {
                            FirebaseManager.getInstance().isBarcodeExist(barcode.getBarcode(), new FirebaseManager.FirebaseCallBack() {
                                @Override
                                public void onCallback(FirebaseManager.FirebaseResult result) {
                                    if (result.isSuccessful()) {
                                        barcode.setFoodDescription(result.getBarcode().getFoodDescription());
                                        barcode.setCategory(result.getBarcode().getCategory());
                                        barcode.setUnit(result.getBarcode().getUnit());
                                        barcode.setAmount(result.getBarcode().getAmount());
                                    } else {
                                        unidentified_barcodes.add(barcode);
                                        barcodes.remove(barcode);

                                    }
                                }

                            });

                        });


                        if (barcodes.size() != 0) {
                            Scan.addItemsBtn.setVisibility(View.VISIBLE);

                        }
                        Scan.barcodesAdapter = new BarcodeListAdapter(barcodes);
                        Scan.barcodesRecyclerView.setAdapter(Scan.barcodesAdapter);
                        Scan.barcodesAdapter.setOnItemClickListener(new BarcodeListAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Intent intent = new Intent(context, EditFoodList.class);
                                intent.putExtra("barcodeItem", barcodes.get(position));
                                intent.putExtra("Class", "Scan");
                                startActivity(intent);
                            }
                        });
                        if (unidentified_barcodes.size() != 0) {
                            Scan.unidentified_barcodesTV.setVisibility(View.VISIBLE);
                        }

                        Scan.unidentified_barcodes_Adapter = new BarcodeListAdapter(unidentified_barcodes);
                        Scan.unidentified_barcodes_recyclerView.setAdapter(Scan.unidentified_barcodes_Adapter);
                        Scan.unidentified_barcodes_Adapter.setOnItemClickListener(new BarcodeListAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Intent intent = new Intent(context, EditFoodList.class);
                                if (barcodes.size() > 0) {
                                    intent.putExtra("barcodeItem", barcodes.get(position));
                                    intent.putExtra("Class", "Scan");
                                    startActivity(intent);
                                } else if (unidentified_barcodes.size() > 0) {

                                }

                            }


                        });
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}




