package com.mhyr.instagramclone;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;

public class SocialMediaActivity extends AppCompatActivity {

    private Toolbar myToolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setTitle("Instagram!");

        myToolbar = findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        viewPager = findViewById(R.id.viewPager);
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager , false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu , menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.postImageItem) {

            if (Build.VERSION.SDK_INT >= 23 &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]
                                {Manifest.permission.READ_EXTERNAL_STORAGE} ,
                        3000);

            } else {

                captureImage();

            }

        } else if (item.getItemId() == R.id.logoutUserItem) {

            ParseUser.logOut();
            finish();
            Intent intent = new Intent(SocialMediaActivity.this , SignUpActivity.class);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 3000) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                captureImage();

            }

        }

    }

    private void captureImage() {

        Intent intent = new Intent(Intent.ACTION_PICK ,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent , 4000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 4000) {

            if (resultCode == Activity.RESULT_OK) {

                try {

//                    Toast.makeText(getContext() , "OK" , Toast.LENGTH_SHORT).show();

                    Uri capturedImage = data.getData();

                    Bitmap bitmap = MediaStore.Images.Media.
                            getBitmap(this.getContentResolver() ,
                                    capturedImage);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG , 100 , byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();

                    ParseFile parseFile = new ParseFile("pic.png" , bytes);
                    ParseObject parseObject = new ParseObject("Photo");
                    parseObject.put("picture" , parseFile);
                    parseObject.put("username" , ParseUser.getCurrentUser().getUsername());
                    final ProgressDialog dialog = new ProgressDialog(this);
                    dialog.setMessage("Loading...");
                    dialog.show();
                    parseObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null) {

                                FancyToast.makeText(SocialMediaActivity.this ,
                                        "Picture Uploaded!" ,
                                        Toast.LENGTH_SHORT ,
                                        FancyToast.SUCCESS ,
                                        false).show();

                            } else {

                                FancyToast.makeText(SocialMediaActivity.this ,
                                        e.getMessage() ,
                                        Toast.LENGTH_SHORT ,
                                        FancyToast.SUCCESS ,
                                        false).show();

                            }

                            dialog.dismiss();

                        }
                    });

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}