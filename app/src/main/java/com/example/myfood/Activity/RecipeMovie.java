package com.example.myfood.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.example.myfood.R;

public class RecipeMovie extends AppCompatActivity {
    private WebView webMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_movie);

        webMovie=findViewById(R.id.recipe_movie);
        webMovie.loadUrl("https://www.youtube.com/watch?v=n-jz92fi1zg");
    }
}
