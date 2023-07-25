package com.example.myapplication;

//Cursor cursor = database.show_pets();
//        if (cursor.getCount()==0){
//        Toast.makeText(addpet.this, "Nothing here", Toast.LENGTH_SHORT).show();
//        }
//        StringBuffer buffer = new StringBuffer();
//        while(cursor.moveToNext()){
//        buffer.append(cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + cursor.getString(3) + " " +cursor.getString(4) + " " +cursor.getString(5) + " " +cursor.getString(6) + cursor.getString(7) + "\n");
//
//        }
//        AlertDialog.Builder builder= new AlertDialog.Builder(addpet.this);
//        builder.setCancelable(true);
//        builder.setMessage(buffer);
//        builder.show();

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class addpet extends AppCompatActivity {
    String uname,email,name,gendertxt,location,breedtxt,description,pet_type;
    String petd_id;
    Integer peta_id = null;
    Spinner gender, species, breed;
    ArrayAdapter<String> breedAdapter;

    EditText et1,et2,et3;
    Button b1;
    ImageView iv1;

    private static final int PICK_IMAGE_REQUEST=99;
    private Uri imagePath;
    private Bitmap imageToStore;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpet);

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        b1 = findViewById(R.id.b1);
        iv1 = findViewById(R.id.iv1);
        Database database = new Database(addpet.this);
        //-------------------------------------------------------------------------------------------
        getSupportActionBar().hide();

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                choseImage();


            }
        });

        //-------------------------------------------------------------------------------------------
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 name = et1.getText().toString();
                 description = et2.getText().toString();
                 location = et3.getText().toString();
                 long result = database.insert_pets(name,pet_type,gendertxt,location,breedtxt,description,imageToStore);
                 if (result!=-1) {
//                     Toast.makeText(addpet.this, "Inserted", Toast.LENGTH_SHORT).show();
                     petd_id = String.valueOf(result);
                     boolean res = database.insert_users(uname,email,petd_id,peta_id);
                     if (res) {
                         Toast.makeText(addpet.this, "Inserted to user", Toast.LENGTH_SHORT).show();
                         Intent intent = new Intent(addpet.this,MainActivity.class);
                         Bundle bundle = new Bundle();
                         bundle.putString("name", name);
                         bundle.putString("email", email);
                         intent.putExtras(bundle);
                         startActivity(intent);
                     }
                     else {
                         Toast.makeText(addpet.this, "Not Inserted", Toast.LENGTH_SHORT).show();
                     }

                     
                 } else {
//                     Toast.makeText(addpet.this, "Not Inserted", Toast.LENGTH_SHORT).show();
                 }

            }
        });



        //-------------------------------------------------------------------------------------------


        gender = (Spinner) findViewById(R.id.spinner);
        species = (Spinner) findViewById(R.id.spinner2);
        breed = (Spinner) findViewById(R.id.spinner4);

        String genders[] = new String[]{"Male", "Female"};

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,genders);
        gender.setAdapter(genderAdapter);
        String specie[] = new String[]{"Dog","Cat","Rabbit"};
        ArrayAdapter<String> specieAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,specie);
        species.setAdapter(specieAdapter);
        String dogbreeds[] = new String[]{"Labrador Retriever", "Poodle","German Shepherd", "Chihuahua","Bulldog", "Shih Tzu","Golden Retriever", "Dachshund","Boxer", "Siberian Husky","Doberman Pinscher", "Beagle","Rottweiler", "Boston Terrier"};

        String catbreeds[] = new String[]{"Siamese", "Persian", "Sphynx", "British Shorthair", "Bengal", "Russian Blue", "Maine Coon", "American Shorthair", "Scottish Fold", "Ragdoll"};

        String rabbitBreeds[] = new String[]{"American Fuzzy Lop", "Belgian Hare", "Californian", "Dutch", "English Angora", "Flemish Giant", "Holland Lop", "Mini Lop", "Netherland Dwarf", "Polish"};

        species.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pet_type = adapterView.getItemAtPosition(i).toString();
                if(pet_type.equals("Dog")) {
                    ArrayAdapter<String> dogAdapter = new ArrayAdapter<>(addpet.this, android.R.layout.simple_spinner_item, dogbreeds);
                    dogAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    breed.setAdapter(dogAdapter);
                }
                else if(pet_type=="Cat"){
                    ArrayAdapter<String> catAdapter = new ArrayAdapter<>(addpet.this, android.R.layout.simple_spinner_item, catbreeds);
                    catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    breed.setAdapter(catAdapter);
                }
                else if(pet_type=="Rabbit"){
                    ArrayAdapter<String> rabbitAdapter = new ArrayAdapter<>(addpet.this, android.R.layout.simple_spinner_item, rabbitBreeds);
                    rabbitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    breed.setAdapter(rabbitAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gendertxt = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(addpet.this, gendertxt, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        breed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                breedtxt = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(addpet.this, breedtxt, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        uname = getIntent().getExtras().getString("name");
        email = getIntent().getExtras().getString("email");
        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();





    }
    private void choseImage() {
        try{
            Intent intent=new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!= null && data.getData()!=null){
                imagePath=data.getData();
                imageToStore= MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                iv1.setImageBitmap(imageToStore);
            }
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }


}





