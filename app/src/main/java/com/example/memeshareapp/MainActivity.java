package com.example.memeshareapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {
ImageView imageView;
    ProgressBar progressbar;
    String meme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData();
        imageView = findViewById(R.id.imageView);
      progressbar= findViewById(R.id.progressBar);

    }
 void getData(){

// Instantiate the RequestQueue.
     RequestQueue queue = Volley.newRequestQueue(this);
     String url = "https://meme-api.com/gimme";

// Request a string response from the provided URL.
    JsonObjectRequest  JSONRequest = new JsonObjectRequest(Request.Method.GET, url,null,
             response -> {Log.d("success","Response is: " + response.toString());
                meme = null;
                 try {
                     meme = response.getString("url");

                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
                 Glide.with(this).load(meme).listener(new RequestListener<Drawable>() {
                     @Override
                     public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                         // log exception
                         Log.e("TAG", "Error loading image", e);
                         progressbar.setVisibility(View.GONE);
                         return false; // important to return false so the error placeholder can be placed
                     }

                     @Override
                     public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                         progressbar.setVisibility(View.GONE);
                         return false;
                     }
                 }).into(imageView);
             },
            error -> Log.d("That didn't work!",error.toString()));

// Add the request to the RequestQueue.
     queue.add(JSONRequest);}
    public void share(View view) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT,"Checkout this meme "+meme);
        startActivity(Intent.createChooser(share,"share this meme"));
    }

    public void next(View view) {
        progressbar.setVisibility(View.VISIBLE);
        getData();

    }
}