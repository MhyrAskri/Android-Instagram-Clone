package com.mhyr.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class UsersPosts extends AppCompatActivity {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_posts);

        linearLayout = findViewById(R.id.linearLayout);

        Intent receivedIntentObject = getIntent();
        final String receivedUserName = receivedIntentObject.getStringExtra("username");

        setTitle(receivedUserName + "'s posts");

        ParseQuery<ParseObject> parseQuery = new ParseQuery<>("Photo");
        parseQuery.whereEqualTo("username" , receivedUserName);
        parseQuery.orderByDescending("createdAt");

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (objects.size() > 0 && e == null) {

                    for (ParseObject post : objects) {

                        TextView postDescription = new TextView(UsersPosts.this);
                        postDescription.setText(post.get("image_des") + "");
                        ParseFile postPicture = (ParseFile) post.get("picture");
                        postPicture.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {

                                if (data != null && e == null) {

                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0 , data.length);
                                    ImageView postImageView = new ImageView(UsersPosts.this);
                                    LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT ,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                                    imageViewParams.setMargins(5 , 5 , 5 , 5);
                                    postImageView.setLayoutParams(imageViewParams);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setPadding(30 , 30 , 30 , 30);
                                    postImageView.setImageBitmap(bitmap);
                                    postImageView.setMaxWidth(150);
                                    postImageView.setMaxHeight(150);

                                    LinearLayout.LayoutParams descriptionParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT ,
                                            ViewGroup.LayoutParams.WRAP_CONTENT);
                                    descriptionParams.setMargins(5 , 5 , 5 , 25);
                                    postDescription.setLayoutParams(descriptionParams);
                                    postDescription.setGravity(Gravity.CENTER);
                                    postDescription.setBackgroundColor(Color.WHITE);
                                    postDescription.setTextSize(30f);
                                    postDescription.setTextColor(Color.BLACK);

                                    linearLayout.setPadding(20 , 20 , 20 , 20);
                                    linearLayout.addView(postImageView);
                                    linearLayout.addView(postDescription);

                                }

                            }

                        });

                    }

                } else {

                    FancyToast.makeText(UsersPosts.this,
                            receivedUserName + " doesn't have any posts." ,
                            Toast.LENGTH_SHORT, FancyToast.INFO,
                            false).show();
                    finish();

                }

                dialog.dismiss();

            }

        });

    }

}