package com.example.myfood.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfood.Activity.Popup.AddFoodList;
import com.example.myfood.Activity.Popup.EditFoodList;
import com.example.myfood.Adapter.FoodListAdapter;
import com.example.myfood.Class.Family;
import com.example.myfood.Class.FoodItem;
import com.example.myfood.Class.User;
import com.example.myfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShoppingList extends Fragment {
    private RecyclerView mRecyclerView;
    public static FoodListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button addBtn;
    private ImageButton shareListBtn;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        addBtn = view.findViewById(R.id.add_food_item);
        shareListBtn = view.findViewById(R.id.share_list);

        myRef = database.getReference("families").child(User.getInstance().getFamilyCode());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Family.getInstance().getShoppingList().clear();
                for (DataSnapshot keyNode : snapshot.child("shoppingList").getChildren()) {
                    FoodItem foodItem = keyNode.getValue(FoodItem.class);
                    Family.getInstance().getShoppingList().add(foodItem);

                }


                mRecyclerView = view.findViewById(R.id.food_list_recyclerView);
                mRecyclerView.setHasFixedSize(true);
                mAdapter = new FoodListAdapter(Family.getInstance().getShoppingList());
                if (Family.getInstance().getShoppingList().size() > 0) {
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.setOnItemClickListener(new FoodListAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Intent intent = new Intent(getContext(), EditFoodList.class);
                            intent.putExtra("shoppingFoodItem", Family.getInstance().getShoppingList().get(position));
                            intent.putExtra("Class", "ShoppingList");
                            startActivity(intent);
                        }
                    });
                }
                mLayoutManager = new LinearLayoutManager(getContext());

                mRecyclerView.setLayoutManager(mLayoutManager);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


        addBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddFoodList.class);
                intent.putExtra("Class", "ShoppingList");
                startActivity(intent);
            }
        });
        shareListBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Document document = new Document();

                try {
                    PdfReader reader = new PdfReader(getActivity().getResources().openRawResource(R.raw.shoppingform));
                    PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(Environment.getExternalStoragePublicDirectory("//MyFood" + "shoppingList" + ".pdf")));
                    PdfContentByte content = stamper.getOverContent(1);
                    content.getPdfDocument().addCreator("hello");

                    document.open();
                    document.add(new Paragraph("hello"));
                    document.close();
                    stamper.setFormFlattening(true);
                    stamper.close();

                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "MyFood" + "shoppingList" + ".pdf");
                    Intent pdfViewIntent = new Intent(Intent.ACTION_VIEW);
                    Uri photoURI = FileProvider.getUriForFile(getActivity().getApplicationContext(), getActivity().getApplicationContext().getPackageName() + ".provider", file);
                    pdfViewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    pdfViewIntent.setDataAndType(photoURI, "application/pdf");


                    getActivity().startActivity(pdfViewIntent);
                } catch (IOException | DocumentException e) {
                    e.printStackTrace();
                }

            }
        });


        return view;

    }
}
