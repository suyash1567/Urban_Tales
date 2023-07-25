package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final int cards = 3;
    private int currentOffset = 0;
    private Cursor cursor,cursor1,cursor2,cursor3,cursor4,cursor5,cursor6;
    private RelativeLayout overlay;
    private PopupWindow popupWindow;
    String petd_id = null;
    ImageView picture;
    TextView petname,location,gender,breed,description;
    Button adopt;
    Integer peta_id;
    TextView seemore, previous,all;
    String name, email;
    String type = "All";
    String desc;
    Database database = new Database(MainActivity.this);
    TextView hello;
    Button addpet;
    ImageView dog1,cat1,rabbit1;
    Bitmap image;

    TextView name1, breed1, location1, gender1, name2, breed2, location2, gender2, name3, breed3, location3, gender3;


    ImageView iv2, pic1, pic2, pic3;
    private static final int PICK_IMAGE_REQUEST = 99;
    private Uri imagePath;
    private Bitmap imageToStore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();



        overlay = (RelativeLayout)findViewById(R.id.overlay);
//        Database database = new Database(MainActivity.this);
        //------------------------------------------------------------------------------------------
        iv2 = findViewById(R.id.iv2);
        seemore = findViewById(R.id.tv2);
        previous = findViewById(R.id.previous);

        //------------------------------------------------------------------------------------------
        all = findViewById(R.id.gender);
        name1 = findViewById(R.id.name1);
        breed1 = findViewById(R.id.breed1);
        location1 = findViewById(R.id.email1);
        gender1 = findViewById(R.id.gender1);
        pic1 = findViewById(R.id.picture);

        name2 = findViewById(R.id.name2);
        breed2 = findViewById(R.id.breed2);
        location2 = findViewById(R.id.location2);
        gender2 = findViewById(R.id.gender2);
        pic2 = findViewById(R.id.pic2);

        name3 = findViewById(R.id.name3);
        breed3 = findViewById(R.id.breed3);
        location3 = findViewById(R.id.location3);
        gender3 = findViewById(R.id.gender3);
        pic3 = findViewById(R.id.pic3);

        dog1 = findViewById(R.id.dog1);
        cat1 = findViewById(R.id.cat1);
        rabbit1 = findViewById(R.id.rabbit1);

        // Fetch the initial set of data from the database
        cursor = database.show_pets();
        cursor1 = database.show_dogs();
        cursor2 = database.show_cats();
        cursor3 = database.show_rabbits();
        cursor4 = database.show_dogs();
        cursor5 = database.show_cats();
        cursor6 = database.show_rabbits();


        //displayNextCards();

        //---------------------------------------------------------------------------------------

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "All";
            }
        });


        //------------------------------------------------------------------------------------------
        dog1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentOffset = 0;
                type = "Dog";
                displaydogs();
            }
        });

        cat1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentOffset = 0;
                type = "Cat";
                displaycats();
            }
        });

        rabbit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentOffset = 0;
                type = "Rabbit";
                displayrabbits();
            }
        });

        //------------------------------------------------------------------------------------------

        pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                overlay.setVisibility(View.VISIBLE);
                overlay.setAlpha(0.7f); // set alpha to make it darker
                View popupView = getLayoutInflater().inflate(R.layout.activity_popup,null);

                petname = popupView.findViewById(R.id.name);
                gender = popupView.findViewById(R.id.gender);
                location = popupView.findViewById(R.id.location);
                breed = popupView.findViewById(R.id.breed);
                description = popupView.findViewById(R.id.description);
                picture = popupView.findViewById(R.id.picture);
                adopt = popupView.findViewById(R.id.adopt);
                //
                Cursor cur1 = database.getdpeta_id(name1.getText().toString(),breed1.getText().toString(),gender1.getText().toString(),location1.getText().toString());
                if (cur1.getCount()==0){
                    Toast.makeText(MainActivity.this, "Nothing here", Toast.LENGTH_SHORT).show();
                }

                while(cur1.moveToNext()){
                    peta_id = Integer.parseInt(cur1.getString(0));

                }
                Toast.makeText(MainActivity.this, String.valueOf(peta_id), Toast.LENGTH_SHORT).show();



                //

                adopt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                            boolean result1 = database.insert_users(name,email,petd_id,peta_id);
                            if (result1) {
                                Toast.makeText(MainActivity.this, "Inserted petaid", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Not inserted", Toast.LENGTH_SHORT).show();
                            }
                    }
                });

                petname.setText(name1.getText().toString());
                gender.setText(gender1.getText().toString());
                breed.setText(breed1.getText().toString());
                gender.setText(gender1.getText().toString());
                location.setText(location1.getText().toString());
                Bitmap imageBitmap = ((BitmapDrawable) pic1.getDrawable()).getBitmap();
                picture.setImageBitmap(imageBitmap);
                Cursor cur = database.getdesc(name1.getText().toString(),breed1.getText().toString(),gender1.getText().toString(),location1.getText().toString());
                if (cur.getCount()==0){
//                    Toast.makeText(MainActivity.this, "Nothing here", Toast.LENGTH_SHORT).show();
                }

                while(cur.moveToNext()){
                    desc = (cur.getString(0));

                }
                description.setText(desc);






                popupWindow = new PopupWindow(popupView, (int)(getResources().getDisplayMetrics().widthPixels*(0.75)), (int)(getResources().getDisplayMetrics().heightPixels*(0.75)));
                popupWindow.showAtLocation(findViewById(R.id.main_layout), Gravity.CENTER, 0, 0);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
                popupWindow.setClippingEnabled(false);
                popupWindow.setAnimationStyle(androidx.constraintlayout.widget.R.style.Animation_AppCompat_Dialog);
                popupWindow.showAsDropDown(view);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                            popupWindow.dismiss();
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

        pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overlay.setVisibility(View.VISIBLE);
                overlay.setAlpha(0.7f); // set alpha to make it darker
                View popupView = getLayoutInflater().inflate(R.layout.activity_popup,null);

                petname = popupView.findViewById(R.id.name);
                gender = popupView.findViewById(R.id.gender);
                location = popupView.findViewById(R.id.location);
                breed = popupView.findViewById(R.id.breed);
                description = popupView.findViewById(R.id.description);
                picture = popupView.findViewById(R.id.picture);
                adopt = popupView.findViewById(R.id.adopt);
                //
                Cursor cur1 = database.getdpeta_id(name2.getText().toString(),breed2.getText().toString(),gender2.getText().toString(),location2.getText().toString());
                if (cur1.getCount()==0){
                    Toast.makeText(MainActivity.this, "Nothing here", Toast.LENGTH_SHORT).show();
                }

                while(cur1.moveToNext()){
                    peta_id = Integer.parseInt(cur1.getString(0));

                }
                Toast.makeText(MainActivity.this, String.valueOf(peta_id), Toast.LENGTH_SHORT).show();



                //

                adopt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean result1 = database.insert_users(name,email,petd_id,peta_id);
                        if (result1) {
                            Toast.makeText(MainActivity.this, "Inserted petaid", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Not inserted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                petname.setText(name2.getText().toString());
                gender.setText(gender2.getText().toString());
                breed.setText(breed2.getText().toString());
                gender.setText(gender2.getText().toString());
                location.setText(location2.getText().toString());
                Bitmap imageBitmap = ((BitmapDrawable) pic2.getDrawable()).getBitmap();
                picture.setImageBitmap(imageBitmap);
                Cursor cur = database.getdesc(name2.getText().toString(),breed2.getText().toString(),gender2.getText().toString(),location2.getText().toString());
                if (cur.getCount()==0){
//                    Toast.makeText(MainActivity.this, "Nothing here", Toast.LENGTH_SHORT).show();
                }

                while(cur.moveToNext()){
                    desc = (cur.getString(0));

                }
                description.setText(desc);






                popupWindow = new PopupWindow(popupView, (int)(getResources().getDisplayMetrics().widthPixels*(0.75)), (int)(getResources().getDisplayMetrics().heightPixels*(0.75)));
                popupWindow.showAtLocation(findViewById(R.id.main_layout), Gravity.CENTER, 0, 0);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
                popupWindow.setClippingEnabled(false);
                popupWindow.setAnimationStyle(androidx.constraintlayout.widget.R.style.Animation_AppCompat_Dialog);
                popupWindow.showAsDropDown(view);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                            popupWindow.dismiss();
                            return true;
                        }
                        return false;
                    }
                });

            }
        });

        pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overlay.setVisibility(View.VISIBLE);
                overlay.setAlpha(0.7f); // set alpha to make it darker
                View popupView = getLayoutInflater().inflate(R.layout.activity_popup,null);

                petname = popupView.findViewById(R.id.name);
                gender = popupView.findViewById(R.id.gender);
                location = popupView.findViewById(R.id.location);
                breed = popupView.findViewById(R.id.breed);
                description = popupView.findViewById(R.id.description);
                picture = popupView.findViewById(R.id.picture);
                adopt = popupView.findViewById(R.id.adopt);
                //
                Cursor cur1 = database.getdpeta_id(name3.getText().toString(),breed3.getText().toString(),gender3.getText().toString(),location3.getText().toString());
                if (cur1.getCount()==0){
                    Toast.makeText(MainActivity.this, "Nothing here", Toast.LENGTH_SHORT).show();
                }

                while(cur1.moveToNext()){
                    peta_id = Integer.parseInt(cur1.getString(0));

                }
                Toast.makeText(MainActivity.this, String.valueOf(peta_id), Toast.LENGTH_SHORT).show();



                //

                adopt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean result1 = database.insert_users(name,email,petd_id,peta_id);
                        if (result1) {
                            Toast.makeText(MainActivity.this, "Inserted petaid", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Not inserted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                petname.setText(name3.getText().toString());
                gender.setText(gender3.getText().toString());
                breed.setText(breed3.getText().toString());
                gender.setText(gender3.getText().toString());
                location.setText(location3.getText().toString());
                Bitmap imageBitmap = ((BitmapDrawable) pic3.getDrawable()).getBitmap();
                picture.setImageBitmap(imageBitmap);
                Cursor cur = database.getdesc(name3.getText().toString(),breed3.getText().toString(),gender3.getText().toString(),location3.getText().toString());
                if (cur.getCount()==0){
//                    Toast.makeText(MainActivity.this, "Nothing here", Toast.LENGTH_SHORT).show();
                }

                while(cur.moveToNext()){
                    desc = (cur.getString(0));

                }
                description.setText(desc);






                popupWindow = new PopupWindow(popupView, (int)(getResources().getDisplayMetrics().widthPixels*(0.75)), (int)(getResources().getDisplayMetrics().heightPixels*(0.75)));
                popupWindow.showAtLocation(findViewById(R.id.main_layout), Gravity.CENTER, 0, 0);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
                popupWindow.setClippingEnabled(false);
                popupWindow.setAnimationStyle(androidx.constraintlayout.widget.R.style.Animation_AppCompat_Dialog);
                popupWindow.showAsDropDown(view);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                            popupWindow.dismiss();
                            return true;
                        }
                        return false;
                    }
                });
            }
        });

















        //------------------------------------------------------------------------------------------



        //------------------------------------------------------------------------------------------

        seemore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (type.equals("All")) {
                    displayNextCards();
                } else if (type.equals("Dog")) {
                    displaydogs();
                } else if (type.equals("Cat")) {
                    displaycats();
                } else {
                    displayrabbits();
                }


            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equals("All")) {
                    displayPreviousCards();
                } else if (type.equals("Dog")) {
                    displayPreviousDogs();
                } else if (type.equals("Cat")) {
                    displayPreviousCats();
                } else {
                    displayPreviousRabbits();
                }
            }
        });


        //------------------------------------------------------------------------------------------

        hello = findViewById(R.id.hello);
        fetchUserDetails(new UserDetailsCallback() {
            @Override
            public void onUserDetailsReceived(String email, String name) {
                MainActivity.this.email = email;
                MainActivity.this.name = name;

                hello.setText("Hello " + name + "!");
                showUserDetailsDialog();

                // Set up click listener for choosing an image
                iv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString("name", name);
                        bundle.putString("email", email);
                        Intent intent = new Intent(MainActivity.this, Profile_page.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
//                        choseImage();
//                        if (imageToStore != null) {
//                            long result = database.insert_userimage(email, imageToStore);
//                            if (result != -1) {
//                                Toast.makeText(MainActivity.this, "Inserted image of user", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                            }
//                        }
                    }
                });

//                 Take image from database
                Cursor cursor = database.show_userimage(email);
                if (cursor.getCount() == 0) {
                    Toast.makeText(getApplicationContext(), "No entries", Toast.LENGTH_SHORT).show();
                } else {
                    while (cursor.moveToNext()) {
                        byte[] imageByte = cursor.getBlob(1);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
                        if (bitmap != null) {
                            iv2.setImageBitmap(bitmap);
                        }
                    }
                }
            }
        });


        addpet = findViewById(R.id.addpet);

        addpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)) {
                    Toast.makeText(MainActivity.this, "Cant Proceed", Toast.LENGTH_SHORT).show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
                    bundle.putString("email", email);
                    Intent intent = new Intent(MainActivity.this, addpet.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            overlay.setVisibility(View.INVISIBLE);
        } else {
            super.onBackPressed();
        }
    }



    private interface UserDetailsCallback {
        void onUserDetailsReceived(String email, String name);
    }

    private void fetchUserDetails(UserDetailsCallback callback) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");
        reference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = "";
                String name = "";

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals("email_txt")) {
                        email = snapshot.getValue().toString();
                    } else if (snapshot.getKey().equals("firstname_txt")) {
                        name = snapshot.getValue().toString();
                    }
                }

                callback.onUserDetailsReceived(email, name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showUserDetailsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(true);
        builder.setMessage("Name: " + name + "\nEmail: " + email);
        builder.show();
    }

//    private void choseImage() {
//        try {
//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(intent, PICK_IMAGE_REQUEST);
//        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), e.getMessage(),
//                    Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        try {
//            super.onActivityResult(requestCode, resultCode, data);
//            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//                imagePath = data.getData();
//                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
//                iv2.setImageBitmap(imageToStore);
//
//                if (imageToStore != null) {
//                    // Check if the image already exists for the user
//                    Cursor cursor = database.show_userimage(email);
//                    if (cursor.getCount() > 0) {
//                        // Update the image in the database for the user
//                        long result = database.update_userimage(email, imageToStore);
//                        if (result != -1) {
//                            Toast.makeText(MainActivity.this, "Updated image of user", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(MainActivity.this, "Failed to update image", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        // Insert the image for the user
//                        long result = database.insert_userimage(email, imageToStore);
//                        if (result != -1) {
//                            Toast.makeText(MainActivity.this, "Inserted image of user", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(MainActivity.this, "Failed to insert image", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

//

    private void displayNextCards() {
        if (cursor != null && cursor.getCount() > 0) {
            // Move the cursor to the next set of rows
            if (currentOffset + cards <= cursor.getCount()) {
                cursor.move(cards);
                currentOffset += cards;
            } else {
                // If there are no more rows, reset the cursor and offset
                cursor.moveToFirst();
                currentOffset = 0;
            }

            // Display the card data
            for (int i = 0; i < cards; i++) {
                if (!cursor.moveToNext()) {
                    // If there are no more rows, move the cursor to the first row
                    cursor.moveToFirst();
                    currentOffset = 0;
                }

                // Retrieve the values from the cursor
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String breed = cursor.getString(cursor.getColumnIndex("breed"));
                @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex("location"));
                @SuppressLint("Range") String gender = cursor.getString(cursor.getColumnIndex("gender"));
                @SuppressLint("Range") byte[] imageByte = cursor.getBlob(cursor.getColumnIndex("image"));
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);

                // Populate the card views
                switch (i) {
                    case 0:
                        name1.setText(name);
                        breed1.setText(breed);
                        location1.setText(location);
                        gender1.setText(gender);
                        if (bitmap != null) {
                            pic1.setImageBitmap(bitmap);
                        }
                        break;
                    case 1:
                        name2.setText(name);
                        breed2.setText(breed);
                        location2.setText(location);
                        gender2.setText(gender);
                        if (bitmap != null) {
                            pic2.setImageBitmap(bitmap);
                        }
                        break;
                    case 2:
                        name3.setText(name);
                        breed3.setText(breed);
                        location3.setText(location);
                        gender3.setText(gender);
                        if (bitmap != null) {
                            pic3.setImageBitmap(bitmap);
                        }
                        break;
                }
            }
        }
    }


    private void displayPreviousCards() {
        if (cursor != null && cursor.getCount() > 0) {
            // Calculate the new offset value
            int newOffset = currentOffset - cards;
            if (newOffset < 0) {
                // If the new offset is negative, move to the last set of rows
                newOffset = cursor.getCount() - (cursor.getCount() % cards);
                if (newOffset == cursor.getCount()) {
                    newOffset -= cards;
                }
            }

            // Move the cursor to the new offset position
            cursor.moveToPosition(newOffset);
            currentOffset = newOffset;

            // Display the card data
            for (int i = 0; i < cards; i++) {
                if (!cursor.moveToNext()) {
                    // If there are no more rows, break the loop
                    break;
                }

                // Retrieve the values from the cursor
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String breed = cursor.getString(cursor.getColumnIndex("breed"));
                @SuppressLint("Range") String location = cursor.getString(cursor.getColumnIndex("location"));
                @SuppressLint("Range") String gender = cursor.getString(cursor.getColumnIndex("gender"));
                @SuppressLint("Range") byte[] imageByte = cursor.getBlob(cursor.getColumnIndex("image"));
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);

                // Populate the card views
                switch (i) {
                    case 0:
                        name1.setText(name);
                        breed1.setText(breed);
                        location1.setText(location);
                        gender1.setText(gender);
                        if (bitmap != null) {
                            pic1.setImageBitmap(bitmap);
                        }
                        break;
                    case 1:
                        name2.setText(name);
                        breed2.setText(breed);
                        location2.setText(location);
                        gender2.setText(gender);
                        if (bitmap != null) {
                            pic2.setImageBitmap(bitmap);
                        }
                        break;
                    case 2:
                        name3.setText(name);
                        breed3.setText(breed);
                        location3.setText(location);
                        gender3.setText(gender);
                        if (bitmap != null) {
                            pic3.setImageBitmap(bitmap);
                        }
                        break;
                }
            }
        }
    }

    private void displaydogs() {
        if (cursor1 != null && cursor1.getCount() >= cards) {
            // Move the cursor1 to the current offset position
            cursor1.moveToPosition(currentOffset);

            // Display the card data
            for (int i = 0; i < cards; i++) {
                if (!cursor1.moveToNext()) {
                    // If there are no more rows, move the cursor1 to the first row
                    cursor1.moveToFirst();
                    currentOffset = 0;
                }

                // Retrieve the values from the cursor1
                @SuppressLint("Range") String name = cursor1.getString(cursor1.getColumnIndex("name"));
                @SuppressLint("Range") String breed = cursor1.getString(cursor1.getColumnIndex("breed"));
                @SuppressLint("Range") String location = cursor1.getString(cursor1.getColumnIndex("location"));
                @SuppressLint("Range") String gender = cursor1.getString(cursor1.getColumnIndex("gender"));
                @SuppressLint("Range") byte[] imageByte = cursor1.getBlob(cursor1.getColumnIndex("image"));
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);

                // Update the card views
                switch (i) {
                    case 0:
                        name1.setText(name);
                        breed1.setText(breed);
                        location1.setText(location);
                        gender1.setText(gender);
                        if (bitmap != null) {
                            pic1.setImageBitmap(bitmap);
                        }
                        break;
                    case 1:
                        name2.setText(name);
                        breed2.setText(breed);
                        location2.setText(location);
                        gender2.setText(gender);
                        if (bitmap != null) {
                            pic2.setImageBitmap(bitmap);
                        }
                        break;
                    case 2:
                        name3.setText(name);
                        breed3.setText(breed);
                        location3.setText(location);
                        gender3.setText(gender);
                        if (bitmap != null) {
                            pic3.setImageBitmap(bitmap);
                        }
                        break;
                }
            }

            // Increment the offset for the next set of cards
            currentOffset += cards;
        }
    }


    private void displaycats() {
        if (cursor2 != null && cursor2.getCount() >= cards) {
            // Move the cursor2 to the current offset position
            cursor2.moveToPosition(currentOffset);

            // Display the card data
            for (int i = 0; i < cards; i++) {
                if (!cursor2.moveToNext()) {
                    // If there are no more rows, move the cursor2 to the first row
                    cursor2.moveToFirst();
                    currentOffset = 0;
                }

                // Retrieve the values from the cursor2
                @SuppressLint("Range") String name = cursor2.getString(cursor2.getColumnIndex("name"));
                @SuppressLint("Range") String breed = cursor2.getString(cursor2.getColumnIndex("breed"));
                @SuppressLint("Range") String location = cursor2.getString(cursor1.getColumnIndex("location"));
                @SuppressLint("Range") String gender = cursor2.getString(cursor2.getColumnIndex("gender"));
                @SuppressLint("Range") byte[] imageByte = cursor2.getBlob(cursor2.getColumnIndex("image"));
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);

                // Update the card views
                switch (i) {
                    case 0:
                        name1.setText(name);
                        breed1.setText(breed);
                        location1.setText(location);
                        gender1.setText(gender);
                        if (bitmap != null) {
                            pic1.setImageBitmap(bitmap);
                        }
                        break;
                    case 1:
                        name2.setText(name);
                        breed2.setText(breed);
                        location2.setText(location);
                        gender2.setText(gender);
                        if (bitmap != null) {
                            pic2.setImageBitmap(bitmap);
                        }
                        break;
                    case 2:
                        name3.setText(name);
                        breed3.setText(breed);
                        location3.setText(location);
                        gender3.setText(gender);
                        if (bitmap != null) {
                            pic3.setImageBitmap(bitmap);
                        }
                        break;
                }
            }

            // Increment the offset for the next set of cards
            currentOffset += cards;
        }
    }

    private void displayrabbits() {
        if (cursor3 != null && cursor3.getCount() >= cards) {
            // Move the cursor3 to the current offset position
            cursor3.moveToPosition(currentOffset);

            // Display the card data
            for (int i = 0; i < cards; i++) {
                if (!cursor3.moveToNext()) {
                    // If there are no more rows, move the cursor3 to the first row
                    cursor3.moveToFirst();
                    currentOffset = 0;
                }

                // Retrieve the values from the cursor3
                @SuppressLint("Range") String name = cursor3.getString(cursor3.getColumnIndex("name"));
                @SuppressLint("Range") String breed = cursor3.getString(cursor3.getColumnIndex("breed"));
                @SuppressLint("Range") String location = cursor3.getString(cursor3.getColumnIndex("location"));
                @SuppressLint("Range") String gender = cursor3.getString(cursor3.getColumnIndex("gender"));
                @SuppressLint("Range") byte[] imageByte = cursor3.getBlob(cursor3.getColumnIndex("image"));
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);

                // Update the card views
                switch (i) {
                    case 0:
                        name1.setText(name);
                        breed1.setText(breed);
                        location1.setText(location);
                        gender1.setText(gender);
                        if (bitmap != null) {
                            pic1.setImageBitmap(bitmap);
                        }
                        break;
                    case 1:
                        name2.setText(name);
                        breed2.setText(breed);
                        location2.setText(location);
                        gender2.setText(gender);
                        if (bitmap != null) {
                            pic2.setImageBitmap(bitmap);
                        }
                        break;
                    case 2:
                        name3.setText(name);
                        breed3.setText(breed);
                        location3.setText(location);
                        gender3.setText(gender);
                        if (bitmap != null) {
                            pic3.setImageBitmap(bitmap);
                        }
                        break;
                }
            }

            // Increment the offset for the next set of cards
            currentOffset += cards;
        }
    }

    private void displayPreviousDogs() {
        if (cursor4 != null && cursor4.getCount() > 0) {
            // Calculate the new offset value
            int newOffset = currentOffset - cards;
            if (newOffset < 0) {
                // If the new offset is negative, move to the last set of rows
                newOffset = cursor4.getCount() - (cursor4.getCount() % cards);
                if (newOffset == cursor4.getCount()) {
                    newOffset -= cards;
                }
            }

            // Move the cursor to the new offset position
            cursor4.moveToPosition(newOffset);
            currentOffset = newOffset;

            // Display the card data
            for (int i = 0; i < cards; i++) {
                if (!cursor4.moveToNext()) {
                    // If there are no more rows, break the loop
                    break;
                }

                // Retrieve the values from the cursor
                @SuppressLint("Range") String name = cursor4.getString(cursor4.getColumnIndex("name"));
                @SuppressLint("Range") String breed = cursor4.getString(cursor4.getColumnIndex("breed"));
                @SuppressLint("Range") String location = cursor4.getString(cursor4.getColumnIndex("location"));
                @SuppressLint("Range") String gender = cursor4.getString(cursor4.getColumnIndex("gender"));
                @SuppressLint("Range") byte[] imageByte = cursor4.getBlob(cursor4.getColumnIndex("image"));
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);

                // Populate the card views
                switch (i) {
                    case 0:
                        name1.setText(name);
                        breed1.setText(breed);
                        location1.setText(location);
                        gender1.setText(gender);
                        if (bitmap != null) {
                            pic1.setImageBitmap(bitmap);
                        }
                        break;
                    case 1:
                        name2.setText(name);
                        breed2.setText(breed);
                        location2.setText(location);
                        gender2.setText(gender);
                        if (bitmap != null) {
                            pic2.setImageBitmap(bitmap);
                        }
                        break;
                    case 2:
                        name3.setText(name);
                        breed3.setText(breed);
                        location3.setText(location);
                        gender3.setText(gender);
                        if (bitmap != null) {
                            pic3.setImageBitmap(bitmap);
                        }
                        break;
                }
            }
        }
    }
    private void displayPreviousCats() {
        if (cursor5 != null && cursor5.getCount() > 0) {
            // Calculate the new offset value
            int newOffset = currentOffset - cards;
            if (newOffset < 0) {
                // If the new offset is negative, move to the last set of rows
                newOffset = cursor5.getCount() - (cursor5.getCount() % cards);
                if (newOffset == cursor5.getCount()) {
                    newOffset -= cards;
                }
            }

            // Move the cursor to the new offset position
            cursor5.moveToPosition(newOffset);
            currentOffset = newOffset;

            // Display the card data
            for (int i = 0; i < cards; i++) {
                if (!cursor5.moveToNext()) {
                    // If there are no more rows, break the loop
                    break;
                }

                // Retrieve the values from the cursor
                @SuppressLint("Range") String name = cursor5.getString(cursor5.getColumnIndex("name"));
                @SuppressLint("Range") String breed = cursor5.getString(cursor5.getColumnIndex("breed"));
                @SuppressLint("Range") String location = cursor5.getString(cursor5.getColumnIndex("location"));
                @SuppressLint("Range") String gender = cursor5.getString(cursor5.getColumnIndex("gender"));
                @SuppressLint("Range") byte[] imageByte = cursor5.getBlob(cursor5.getColumnIndex("image"));
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);

                // Populate the card views
                switch (i) {
                    case 0:
                        name1.setText(name);
                        breed1.setText(breed);
                        location1.setText(location);
                        gender1.setText(gender);
                        if (bitmap != null) {
                            pic1.setImageBitmap(bitmap);
                        }
                        break;
                    case 1:
                        name2.setText(name);
                        breed2.setText(breed);
                        location2.setText(location);
                        gender2.setText(gender);
                        if (bitmap != null) {
                            pic2.setImageBitmap(bitmap);
                        }
                        break;
                    case 2:
                        name3.setText(name);
                        breed3.setText(breed);
                        location3.setText(location);
                        gender3.setText(gender);
                        if (bitmap != null) {
                            pic3.setImageBitmap(bitmap);
                        }
                        break;
                }
            }
        }
    }
    private void displayPreviousRabbits() {
        if (cursor6!= null && cursor6.getCount() > 0) {
            // Calculate the new offset value
            int newOffset = currentOffset - cards;
            if (newOffset < 0) {
                // If the new offset is negative, move to the last set of rows
                newOffset = cursor6.getCount() - (cursor6.getCount() % cards);
                if (newOffset == cursor6.getCount()) {
                    newOffset -= cards;
                }
            }

            // Move the cursor to the new offset position
            cursor6.moveToPosition(newOffset);
            currentOffset = newOffset;

            // Display the card data
            for (int i = 0; i < cards; i++) {
                if (!cursor6.moveToNext()) {
                    // If there are no more rows, break the loop
                    break;
                }

                // Retrieve the values from the cursor
                @SuppressLint("Range") String name = cursor6.getString(cursor6.getColumnIndex("name"));
                @SuppressLint("Range") String breed = cursor6.getString(cursor6.getColumnIndex("breed"));
                @SuppressLint("Range") String location = cursor6.getString(cursor6.getColumnIndex("location"));
                @SuppressLint("Range") String gender = cursor6.getString(cursor6.getColumnIndex("gender"));
                @SuppressLint("Range") byte[] imageByte = cursor6.getBlob(cursor6.getColumnIndex("image"));
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);

                // Populate the card views
                switch (i) {
                    case 0:
                        name1.setText(name);
                        breed1.setText(breed);
                        location1.setText(location);
                        gender1.setText(gender);
                        if (bitmap != null) {
                            pic1.setImageBitmap(bitmap);
                        }
                        break;
                    case 1:
                        name2.setText(name);
                        breed2.setText(breed);
                        location2.setText(location);
                        gender2.setText(gender);
                        if (bitmap != null) {
                            pic2.setImageBitmap(bitmap);
                        }
                        break;
                    case 2:
                        name3.setText(name);
                        breed3.setText(breed);
                        location3.setText(location);
                        gender3.setText(gender);
                        if (bitmap != null) {
                            pic3.setImageBitmap(bitmap);
                        }
                        break;
                }
            }
        }
    }

}







