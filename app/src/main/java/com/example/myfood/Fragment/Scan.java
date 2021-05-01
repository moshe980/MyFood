package com.example.myfood.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.canhub.cropper.CropImage;
import com.example.myfood.Activity.ManageFood;
import com.example.myfood.Adapter.BarcodeListAdapter;
import com.example.myfood.Class.Family;
import com.example.myfood.Class.FoodItem;
import com.example.myfood.Class.User;
import com.example.myfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                getPermission(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE);
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


    public void inChooseFile() {
        CropImage.activity().start(getActivity(), this);



    }

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.d("pttt", "Is Granted");
                    Log.d("pttt", "action ! !");
                    inChooseFile();
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    getPermission(Manifest.permission.SEND_SMS);


                    Log.d("pttt", "No Granted");
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.

                }
            });

    private ActivityResultLauncher<String> firstRequestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.d("pttt", "Is Granted");
                    Log.d("pttt", "action ! !");
                    inChooseFile();

                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    requestWithExplainDialog("נדרשת הרשאה לטובת סריקת הקבלה");
                    Log.d("pttt", "No Granted");

                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });

    ActivityResultLauncher<Intent> manuallyActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            Log.d("pttt", "action ! !");
                            inChooseFile();

                        } else if (true) {
                            getPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                        } else {

                            Log.d("pttt", "Cant Action ! !");
                        }
                    }
                }
            });

    private void getPermission(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            inChooseFile();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            requestWithExplainDialog("נדרשת הרשאה לטובת סריקת הקבלה");
        } else {

            firstRequestPermissionLauncher.launch(permission);
        }
    }

    private void requestWithExplainDialog(String message) {
        AlertDialog alertDialog =
                new AlertDialog.Builder(getActivity())
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(getString(android.R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                                    }
                                }).show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    private void getPermission(String permission) {
        if (shouldShowRequestPermissionRationale(permission)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
            requestWithExplainDialog("נדרשת הרשאה לטובת סריקת הקבלה");

        } else if (!shouldShowRequestPermissionRationale(permission)) {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            manuallyDialog(permission);
        } else {
            Log.d("pttt", "Cant Action ! !");
        }
    }

    private void manuallyDialog(String permission) {
        if (shouldShowRequestPermissionRationale(permission)) {
            Log.d("pttt", "Cant Action ! !");
            return;
        }

        String message = "תועברו למסך הגדרות האפליקצייה על מנת לתת הרשאה לטובת סריקת הקבלה.";
        AlertDialog alertDialog =
                new AlertDialog.Builder(getActivity())
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(getString(android.R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                        intent.setData(uri);
                                        manuallyActivityResultLauncher.launch(intent);
                                        dialog.cancel();
                                    }
                                }).show();
        alertDialog.setCanceledOnTouchOutside(false);
    }


}
