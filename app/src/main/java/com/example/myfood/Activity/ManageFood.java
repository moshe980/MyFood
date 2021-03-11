package com.example.myfood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfood.Activity.Popup.EditFoodList;
import com.example.myfood.Adapter.BarcodeListAdapter;
import com.example.myfood.Adapter.FoodListAdapter;
import com.example.myfood.Class.FoodItem;
import com.example.myfood.Class.User;
import com.example.myfood.Fragment.Achievements;
import com.example.myfood.Fragment.FoodStock;
import com.example.myfood.Fragment.Scan;
import com.example.myfood.Fragment.SearchRecipe;
import com.example.myfood.Fragment.ShoppingList;
import com.example.myfood.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.ArrayList;

public class ManageFood extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private Context context;
    public static ArrayList<FoodItem> barcodes;
    public static ArrayList<FoodItem> unidentified_barcodes;

    public static User currentUser;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    public NavigationView navigationView;
    public TextView nameSlideTV;
    public TextView familiyCodeSlideTV;
    public TextView userScoreTV;
    public TextView familiyScoreTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
        setContentView(R.layout.activity_manage_food);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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
        familiyScoreTV = headerView.findViewById(R.id.familyScore);

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

        myRef = database.getReference("users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    if (keyNode.child("email").getValue(String.class).equals(user.getEmail())) {
                        currentUser = keyNode.getValue(User.class);
                        nameSlideTV.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
                        familiyCodeSlideTV.setText("קוד משפחה: " + currentUser.getFamilyCode());
                        userScoreTV.setText("ניקוד שצברתי: " + currentUser.getScore());
                        //familiyScoreTV.setText("ניקוד משפחתי: " + currentUser.getFamilyCode());

                        break;


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri imageUri;
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Scan.progressBar.setVisibility(View.VISIBLE);
                barcodes.clear();
                imageUri = result.getUri();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("products");

                FirebaseVisionImage firebaseVisionImage = null;
                try {
                    firebaseVisionImage = FirebaseVisionImage.fromFilePath(context, imageUri);
                    //2. Get an instance of FirebaseVision
                    FirebaseVision firebaseVision = FirebaseVision.getInstance();
                    //3. Create an instance of FirebaseVisionTextRecognizer
                    FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = firebaseVision.getOnDeviceTextRecognizer();
                    //4. Create a task to process the image
                    Task<FirebaseVisionText> task = firebaseVisionTextRecognizer.processImage(firebaseVisionImage);
                    //5. if task is success
                    task.addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                        @Override
                        public void onSuccess(FirebaseVisionText firebaseVisionText) {
                            String s = firebaseVisionText.getText();
                            String[] splitedS = s.split(" " + "|\\" + "\n");
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

                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    boolean flag = false;
                                    ArrayList<FoodItem> tmp=new ArrayList<FoodItem>(barcodes);

                                    for (FoodItem barcode:tmp) {
                                        for (DataSnapshot keyNode : snapshot.getChildren()) {
                                            if (keyNode.child("barcode").getValue().equals(barcode.getBarcode())) {
                                                barcode.setFoodDescription(String.valueOf(keyNode.child("product_name").getValue()));
                                                barcode.setCategory(String.valueOf(keyNode.child("category").getValue()));
                                                barcode.setUnit(String.valueOf(keyNode.child("unit").getValue()));
                                                int amount = Integer.parseInt(String.valueOf(keyNode.child("amount").getValue()));
                                                double weight = Double.parseDouble(String.valueOf(keyNode.child("weight").getValue()));
                                                barcode.setAmount(amount * weight);
                                                flag = true;
                                                break;

                                            }

                                        }
                                        if (!flag) {
                                            unidentified_barcodes.add(barcode);
                                            barcodes.remove(barcode);
                                        }
                                        flag = false;
                                    }

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
                                            if(barcodes.size()>0){
                                                intent.putExtra("barcodeItem", barcodes.get(position));
                                                intent.putExtra("Class", "Scan");
                                                startActivity(intent);
                                            }else if(unidentified_barcodes.size()>0){

                                            }

                                        }
                                    });

                                    Scan.progressBar.setVisibility(View.GONE);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }
                    });
                    //6. if task is failure
                    task.addOnFailureListener(new

                                                      OnFailureListener() {
                                                          @Override
                                                          public void onFailure(@NonNull Exception e) {
                                                              Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                                          }
                                                      });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception e = result.getError();
                Toast.makeText(this, "Error:" + e, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
