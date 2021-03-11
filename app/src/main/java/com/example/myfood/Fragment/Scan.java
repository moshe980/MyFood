package com.example.myfood.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfood.Activity.ManageFood;
import com.example.myfood.Adapter.BarcodeListAdapter;
import com.example.myfood.Class.Family;
import com.example.myfood.Class.FoodItem;
import com.example.myfood.Class.User;
import com.example.myfood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;

public class Scan extends Fragment {
    private Button addImageBtn;
    public static Button addItemsBtn;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    public static RecyclerView barcodesRecyclerView;
    public static RecyclerView unidentified_barcodes_recyclerView;
    public static BarcodeListAdapter barcodesAdapter;
    public static BarcodeListAdapter unidentified_barcodes_Adapter;
    public RecyclerView.LayoutManager mLayoutManager;
    public static ProgressBar progressBar;
    public static TextView unidentified_barcodesTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_scan, container, false);


        progressBar = view.findViewById(R.id.progressBar);
        unidentified_barcodesTV = view.findViewById(R.id.unidentified_barcodesTV);
        unidentified_barcodesTV.setVisibility(View.GONE);

        barcodesRecyclerView = view.findViewById(R.id.barcodes_recyclerView);
        barcodesRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        barcodesRecyclerView.setLayoutManager(mLayoutManager);

        unidentified_barcodes_recyclerView = view.findViewById(R.id.unidentified_barcodes_recyclerView);
        unidentified_barcodes_recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        unidentified_barcodes_recyclerView.setLayoutManager(mLayoutManager);


        addImageBtn = view.findViewById(R.id.addImage);
        addItemsBtn = view.findViewById(R.id.addItems);

        addItemsBtn.setVisibility(View.GONE);
        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inChooseFile(v);

            }
        });

        addItemsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef = database.getReference("families").child(User.getInstance().getFamilyCode()).child("foodStock");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot keyNode : snapshot.getChildren()) {
                            boolean flag = false;
                            for (int i = 0; i < ManageFood.barcodes.size(); i++) {
                                for (int j = 0; j < Family.getInstance().getFoodList().size(); j++) {
                                    if (ManageFood.barcodes.get(i).getFoodDescription().equals(Family.getInstance().getFoodList().get(j).getFoodDescription())) {
                                        flag = true;
                                        Family.getInstance().getFoodList().get(j).setAmount(Family.getInstance().getFoodList().get(j).getAmount() + ManageFood.barcodes.get(i).getAmount());
                                    }
                                }
                                if (flag == false) {
                                    Family.getInstance().getFoodList().add(new FoodItem(null
                                            , ManageFood.barcodes.get(i).getFoodDescription()
                                            , ManageFood.barcodes.get(i).getAmount()
                                            , ManageFood.barcodes.get(i).getUnit()
                                            , ManageFood.barcodes.get(i).getCategory()));
                                }
                                flag = false;

                            }
                            myRef.setValue(Family.getInstance().getFoodList());
                            Scan.unidentified_barcodesTV.setVisibility(View.GONE);
                            if (Family.getInstance().getFoodList() != null) {
                                myRef = database.getReference("families").child(User.getInstance().getFamilyCode()).child("shoppingList");

                                for (int i = 0; i < ManageFood.barcodes.size(); i++) {
                                    for (int j = 0; j < Family.getInstance().getShoppingList().size(); j++) {
                                        if (ManageFood.barcodes.get(i).getFoodDescription().equals(Family.getInstance().getShoppingList().get(j).getFoodDescription())) {
                                            Family.getInstance().getShoppingList().get(j).setAmount(Family.getInstance().getShoppingList().get(j).getAmount() - ManageFood.barcodes.get(i).getAmount());
                                            if (Family.getInstance().getShoppingList().get(j).getAmount() <= 0) {
                                                Family.getInstance().getShoppingList().remove(j);
                                            }
                                        }
                                    }

                                }
                                myRef.setValue(Family.getInstance().getShoppingList());
                            }
                            ManageFood.barcodes.clear();
                            Family.getInstance().getFoodList().clear();
                            ManageFood.unidentified_barcodes.clear();
                            Toast.makeText(getActivity().getApplicationContext(), "הפריטים נוספו בהצלחה!", Toast.LENGTH_SHORT).show();
                            barcodesAdapter.notifyDataSetChanged();
                            unidentified_barcodes_Adapter.notifyDataSetChanged();
                            addItemsBtn.setVisibility(View.GONE);


                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
        return view;

    }


    public void inChooseFile(View v) {
        CropImage.activity().start(getActivity());


    }


}
