package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Profile_page extends AppCompatActivity {
    TextView name1,email1,tv1,tv3;
    ImageButton back,logout;
    ImageView pic1;
    Database database = new Database(Profile_page.this);

    private static final int PICK_IMAGE_REQUEST = 99;
    private Uri imagePath;
    private Bitmap imageToStore;

    String email,name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        name1 = findViewById(R.id.name1);

        email1 = findViewById(R.id.email1);
        tv1 = findViewById(R.id.tv1);
        tv3 = findViewById(R.id.tv3);
        back = findViewById(R.id.back);
        pic1 = findViewById(R.id.picture);
        logout = findViewById(R.id.logout);
        getSupportActionBar().hide();

        email = getIntent().getExtras().getString("email");
        name = getIntent().getExtras().getString("name");
        name1.setText(name);

        email1.setText(email);
        // fetch pets donated

        Cursor cursor1 = database.countd(email);
        if (cursor1.getCount()==0){
            Toast.makeText(Profile_page.this, "Nothing here", Toast.LENGTH_SHORT).show();
        }
        StringBuffer buffer = new StringBuffer();
        while(cursor1.moveToNext()){
            tv3.setText(cursor1.getString(0));

        }


        //
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Back Button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Profile_page.this,MainActivity.class);
                startActivity(intent);
            }
        });


        // Set up click listener for choosing an image
        pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choseImage();
                if (imageToStore != null) {
                    long result = database.insert_userimage(email, imageToStore);
                    if (result != -1) {
                        Toast.makeText(Profile_page.this, "Inserted image of user", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Profile_page.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Take image from database
        Cursor cursor = database.show_userimage(email);
        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "No entries", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                byte[] imageByte = cursor.getBlob(1);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
                if (bitmap != null) {
                    pic1.setImageBitmap(bitmap);

        }
    }
}





}

    private void choseImage() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                imagePath = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                pic1.setImageBitmap(imageToStore);

                if (imageToStore != null) {
                    // Check if the image already exists for the user
                    Cursor cursor = database.show_userimage(email);
                    if (cursor.getCount() > 0) {
                        // Update the image in the database for the user
                        long result = database.update_userimage(email, imageToStore);
                        if (result != -1) {
                            Toast.makeText(Profile_page.this, "Updated image of user", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Profile_page.this, "Failed to update image", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Insert the image for the user
                        long result = database.insert_userimage(email, imageToStore);
                        if (result != -1) {
                            Toast.makeText(Profile_page.this, "Inserted image of user", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Profile_page.this, "Failed to insert image", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
