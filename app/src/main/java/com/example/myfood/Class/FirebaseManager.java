package com.example.myfood.Class;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseManager {
    private static FirebaseManager instance;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseFirestore db;

    private FirebaseManager() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();

    }

    public static FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }


    public void login(Context context, String email, String password, final FirebaseCallBack firebaseCallback) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    myRef = database.getReference("users").child(String.valueOf(email.replace(".", "")));
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User currentUser = snapshot.getValue(User.class);
                            User.initUser(currentUser.getEmail(), currentUser.getFirstName(), currentUser.getLastName()
                                    , currentUser.getBirthDay(), currentUser.getFamilyCode(), currentUser.getNum_scans(), currentUser.getNum_cooks(), currentUser.getScore());
                            Family.initFamily(currentUser.getFamilyCode(), currentUser.getLastName());

                            Log.d("TAG", "loginWithEmail:success");
                            firebaseCallback.onCallback(new FirebaseResult(true, null, null, null));

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.w("TAG", "logInWithEmail:failure", task.getException());
                            firebaseCallback.onCallback(new FirebaseResult(false, null, null, null));

                        }
                    });
                } else {
                    Log.w("TAG", "logInWithEmail:failure", task.getException());
                    Toast.makeText(context, "האימייל או הסיסמא אינם נכונים", Toast.LENGTH_SHORT).show();
                    firebaseCallback.onCallback(new FirebaseResult(false, null, null, null));


                }

            }
        });
    }

    public void signIn(Context context, String password, final FirebaseCallBack firebaseCallback) {

        if (User.getInstance().getFamilyCode().equals("")) {
            mAuth.createUserWithEmailAndPassword(User.getInstance().getEmail(), password)
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                String newFamliyCode = String.valueOf(User.getInstance().getEmail().hashCode()).replace("-", "");
                                User.getInstance().setFamilyCode(newFamliyCode);
                                myRef = database.getReference("users");
                                myRef.child(User.getInstance().getEmail().replace(".", "")).setValue(User.getInstance());

                                Family.initFamily(User.getInstance().getFamilyCode(), User.getInstance().getLastName());
                                myRef = database.getReference("families");
                                myRef.child(newFamliyCode).setValue(Family.getInstance());


                                Log.d("TAG", "sign in success!");
                                Toast.makeText(context, "נרשמת בהצלחה!", Toast.LENGTH_LONG).show();
                                firebaseCallback.onCallback(new FirebaseResult(true, null, null, null));

                            } else {
                                Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(context, "ההרשמה כשלה תנסה בפעם אחרת!", Toast.LENGTH_LONG).show();
                                firebaseCallback.onCallback(new FirebaseResult(false, null, null, null));

                            }
                        }
                    });

        } else {
            myRef = database.getReference("families").child(User.getInstance().getFamilyCode());
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean isExist = false;
                    for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                        Family family = keyNode.getValue(Family.class);
                        if (family.getCode().equals(User.getInstance().getFamilyCode())) {
                            isExist = true;
                            break;
                        } else {
                            isExist = false;
                        }
                    }
                    if (isExist) {
                        mAuth.createUserWithEmailAndPassword(User.getInstance().getEmail(), password)
                                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {
                                            Family.initFamily(User.getInstance().getFamilyCode(), User.getInstance().getLastName());
                                            myRef = database.getReference("users");
                                            myRef.child(User.getInstance().getEmail().replace(".", "")).setValue(User.getInstance());

                                            Log.d("TAG", "sign in success!");
                                            Toast.makeText(context, "נרשמת בהצלחה!", Toast.LENGTH_LONG).show();
                                            firebaseCallback.onCallback(new FirebaseResult(true, null, null, null));

                                        } else {
                                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                                            Toast.makeText(context, "החיבור כשל תנסה בפעם אחרת!", Toast.LENGTH_LONG).show();
                                            firebaseCallback.onCallback(new FirebaseResult(false, null, null, null));

                                        }
                                    }
                                });
                    } else {
                        firebaseCallback.onCallback(new FirebaseResult(false, null, null, null));

                    }

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("TAG", "Failed to read value.", error.toException());
                    firebaseCallback.onCallback(new FirebaseResult(false, null, null, null));

                }
            });
        }
    }

    public void isBarcodeExist(String barcode, final FirebaseCallBack firebaseCallback) {
        myRef = database.getReference("products").child(barcode);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    firebaseCallback.onCallback(new FirebaseResult(false, null, null, null));

                } else {
                    FoodItem item = new FoodItem();

                    item.setFoodDescription(String.valueOf(snapshot.child("product_name").getValue()));
                    item.setCategory(String.valueOf(snapshot.child("category").getValue()));
                    item.setUnit(String.valueOf(snapshot.child("unit").getValue()));
                    int amount = Integer.parseInt(String.valueOf(snapshot.child("amount").getValue()));
                    double weight = Double.parseDouble(String.valueOf(snapshot.child("weight").getValue()));
                    item.setAmount(amount * weight);

                    firebaseCallback.onCallback(new FirebaseResult(true, item, null, null));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "sign in success!");

            }

        });
    }

    public void getFoodList(final FirebaseCallBack firebaseCallback) {
        myRef = database.getReference("families").child(User.getInstance().getFamilyCode());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Family.getInstance().getFoodList().clear();
                for (DataSnapshot keyNode : snapshot.child("foodStock").getChildren()) {
                    FoodItem foodItem = keyNode.getValue(FoodItem.class);
                    Family.getInstance().getFoodList().add(foodItem);
                }

                firebaseCallback.onCallback(new FirebaseResult(true, null, null, null));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                firebaseCallback.onCallback(new FirebaseResult(false, null, null, null));

            }
        });

    }

    public void getShoppingList(final FirebaseCallBack firebaseCallback) {
        myRef = database.getReference("families").child(User.getInstance().getFamilyCode());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Family.getInstance().getShoppingList().clear();
                for (DataSnapshot keyNode : snapshot.child("shoppingList").getChildren()) {
                    FoodItem foodItem = keyNode.getValue(FoodItem.class);
                    Family.getInstance().getShoppingList().add(foodItem);
                }

                firebaseCallback.onCallback(new FirebaseResult(true, null, null, null));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                firebaseCallback.onCallback(new FirebaseResult(false, null, null, null));

            }
        });

    }

    public void setFoodList(final FirebaseCallBack firebaseCallback) {
        myRef = database.getReference("families").child(User.getInstance().getFamilyCode()).child("foodStock");
        myRef.setValue(Family.getInstance().getFoodList());
        firebaseCallback.onCallback(new FirebaseResult(true, null, null, null));
    }

    public void setShoppingList(final FirebaseCallBack firebaseCallback) {
        myRef = database.getReference("families").child(User.getInstance().getFamilyCode()).child("shoppingList");
        myRef.setValue(Family.getInstance().getShoppingList());
        firebaseCallback.onCallback(new FirebaseResult(true, null, null, null));
    }

    public void getRecipes(final FirebaseCallBack firebaseCallback) {
        ArrayList<RecipeItem> recipesList = new ArrayList<RecipeItem>();
        db.collection("recipes").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Map<String, Object> doc = documentSnapshot.getData();
                    RecipeItem recipeItem = new RecipeItem();
                    recipeItem.setName((String) doc.get("שם"));
                    recipeItem.setCategory((String) doc.get("קטגוריה"));
                    recipeItem.setDifficultLevel((String) doc.get("קושי"));
                    recipeItem.setKashrot((String) doc.get("כשרות"));
                    recipeItem.setImageUrl((String) doc.get("תמונה"));
                    recipeItem.setIngredients((List<Map<String, Object>>) doc.get("רכיבים"));
                    recipeItem.setInstructions((ArrayList<String>) doc.get("הוראות"));
                    recipesList.add(recipeItem);

                }
                firebaseCallback.onCallback(new FirebaseResult(true, null, recipesList, null));

            }
        });

    }

    public void updateNum_cooks(final FirebaseCallBack firebaseCallback) {
        myRef = database.getReference("users").child(String.valueOf(User.getInstance().getEmail().replace(".", "")));
        int current = Integer.parseInt(User.getInstance().getNum_cooks());
        User.getInstance().setNum_cooks(String.valueOf(current++));
        myRef.setValue(User.getInstance().getNum_cooks());
    }

    public void updateNum_scans(final FirebaseCallBack firebaseCallback) {
        myRef = database.getReference("users").child(String.valueOf(User.getInstance().getEmail().replace(".", "")));
        int current = Integer.parseInt(User.getInstance().getNum_scans());
        User.getInstance().setNum_scans(String.valueOf(current++));
        myRef.setValue(User.getInstance().getNum_scans());
        firebaseCallback.onCallback(new FirebaseResult(true, null, null, null));
    }

    public void getAchievementsList(final FirebaseCallBack firebaseCallback) {
        db.collection("Achievments").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Achievement> achievements = new ArrayList<Achievement>();

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Map<String, Object> doc = documentSnapshot.getData();
                    Achievement achievement = new Achievement();
                    achievement.setId((String) doc.get("id"));
                    achievement.setName((String) doc.get("name"));
                    achievement.setGoal((String) doc.get("goal"));
                    achievement.setPoints((String) doc.get("points"));
                    achievement.setStatus(String.valueOf(0));
                    achievements.add(achievement);

                }


                firebaseCallback.onCallback(new FirebaseResult(true, null, null, updateScore(achievements)));


            }
        });
    }

    public ArrayList<Achievement> updateScore(ArrayList<Achievement> achievements) {
        myRef = database.getReference("users").child(String.valueOf(User.getInstance().getEmail().replace(".", "")));

        for (Achievement achievement : achievements) {
            if (achievement.getName().contains("להכין")) {
                achievement.setStatus(String.valueOf(User.getInstance().getNum_cooks()));
                if (Integer.parseInt(achievement.getStatus()) >= Integer.parseInt(achievement.getGoal())) {
                    achievement.setGoal("בוצע");
                    achievement.setStatus("");
                    User.getInstance().setScore(User.getInstance().getNum_cooks() + Integer.parseInt(achievement.getPoints()));
                    myRef.child("score").setValue(String.valueOf(User.getInstance().getScore()));
                }
            } else if (achievement.getName().contains("לסרוק")) {
                achievement.setStatus(String.valueOf(User.getInstance().getNum_scans()));
                if (Integer.parseInt(achievement.getStatus()) >= Integer.parseInt(achievement.getGoal())) {
                    achievement.setGoal("בוצע");
                    achievement.setStatus("");
                    User.getInstance().setScore(User.getInstance().getNum_scans() + Integer.parseInt(achievement.getPoints()));
                    myRef.child("score").setValue(String.valueOf(User.getInstance().getScore()));
                }
            }
        }
        return achievements;
    }

    public class FirebaseResult {
        private boolean isSuccessful;
        private FoodItem barcode;
        private ArrayList<RecipeItem> recipesList = new ArrayList<RecipeItem>();
        private ArrayList<Achievement> achievementsList = new ArrayList<Achievement>();


        public FirebaseResult(boolean isSuccessful, FoodItem barcode, ArrayList<RecipeItem> recipesList, ArrayList<Achievement> achievements) {
            this.isSuccessful = isSuccessful;
            this.barcode = barcode;
            this.recipesList = recipesList;
            achievementsList = achievements;
        }

        public boolean isSuccessful() {
            return isSuccessful;
        }


        public FoodItem getBarcode() {
            return barcode;
        }

        public ArrayList<RecipeItem> getRecipes() {
            return recipesList;
        }

        public ArrayList<Achievement> getAchievementsList() {
            return achievementsList;
        }
    }


    public interface FirebaseCallBack {
        void onCallback(FirebaseResult result);

    }
}


