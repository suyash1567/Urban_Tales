package com.example.myapplication;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;

public class Database extends SQLiteOpenHelper {
    Context context;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageInBytes;





    public Database(Context context) {
        super(context, "app8.db", null, 1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table users (name text, email text, petd_id int, peta_id int)");
        sqLiteDatabase.execSQL("create table userimage (email text primary key, image blob)");
        sqLiteDatabase.execSQL("create table pets (pet_id INTEGER primary key autoincrement, name text, pet_type text, gender text, location text, breed text, description text,image blob)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //----------------------------------------------------------------------------------------------------------------------
    //Insert Data in users table
    public boolean insert_users(String name, String email, String petd_id, Integer peta_id) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("email", email);
        cv.put("petd_id", petd_id);
        cv.put("peta_id", peta_id);
        long result = sqLiteDatabase.insert("users", null, cv);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    //----------------------------------------------------------------------------------------------------------------------
    //Insert Data in pets table
    public long insert_pets(String name,String pet_type, String gender, String location, String breed, String description, Bitmap image) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        imageInBytes = byteArrayOutputStream.toByteArray();


        cv.put("name", name);
        cv.put("pet_type", pet_type);
        cv.put("gender", gender);
        cv.put("location", location);
        cv.put("breed", breed);
        cv.put("description", description);
        cv.put("image", imageInBytes);
        long result = sqLiteDatabase.insert("pets", null, cv);
        return result;

    }

    //----------------------------------------------------------------------------------------------------------------------
    //Insert Data in userimage table
    public long insert_userimage(String email, Bitmap image) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        imageInBytes = byteArrayOutputStream.toByteArray();


        cv.put("email", email);
        cv.put("image", imageInBytes);
        long result = sqLiteDatabase.insert("userimage", null, cv);
        return result;

    }




    //----------------------------------------------------------------------------------------------------------------------
    //Update Data
//    public boolean update(int id, String name, int age) {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        //cv.put("name",name);
//        cv.put("age", age);
//        Cursor cursor = sqLiteDatabase.rawQuery("select * from test where id =? and name =?", new String[]{String.valueOf(id), name});
//        if (cursor.getCount() > 0) {
//            long result = sqLiteDatabase.update("test", cv, "id=? and name =?", new String[]{String.valueOf(id), name});
//            if (result == -1) {
//                return false;
//            } else {
//                return true;
//            }
//        } else {
//            return false;
//        }
//    }

    //----------------------------------------------------------------------------------------------------------------------
    //Delete Data
//    public boolean delete(int id) {
//        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
//        Cursor cursor = sqLiteDatabase.rawQuery("select * from test where id =?", new String[]{String.valueOf(id)});
//        if (cursor.getCount() > 0) {
//            long result = sqLiteDatabase.delete("test", "id=?", new String[]{String.valueOf(id)});
//            if (result == -1) {
//                return false;
//            } else {
//                return true;
//            }
//        } else {
//            return false;
//        }
//    }

    //----------------------------------------------------------------------------------------------------------------------
    //Show Data
    public Cursor show_pets() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT name, breed, location, gender, image FROM pets", null);
        return cursor;
    }
    //----------------------------------------------------------------------------------------------------------------------
    //Show Dogs
    public Cursor show_dogs() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT name, breed, location, gender, image FROM pets where pet_type = 'Dog' ", null);
        return cursor;
    }
    //----------------------------------------------------------------------------------------------------------------------
    //Show Cats
    public Cursor show_cats() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT name, breed, location, gender, image FROM pets where pet_type = 'Cat' ", null);
        return cursor;
    }
    //----------------------------------------------------------------------------------------------------------------------
    //Show rabbits
    public Cursor show_rabbits() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT name, breed, location, gender, image FROM pets where pet_type = 'Rabbit' ", null);
        return cursor;
    }
    //----------------------------------------------------------------------------------------------------------------------
    //Show Data
    public Cursor show_userimage(String email) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from userimage where email = ? ",new String[] {email} );
        return cursor;
    }


    // Update Data in userimage table
    public int update_userimage(String email, Bitmap image) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        imageInBytes = byteArrayOutputStream.toByteArray();

        cv.put("image", imageInBytes);

        String whereClause = "email = ?";
        String[] whereArgs = {email};
        int result = sqLiteDatabase.update("userimage", cv, whereClause, whereArgs);
        return result;
    }

    //----------------------------------------------------------------------------------------------------------------------
    //Count petd
    public Cursor countd(String email) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select count(petd_id) from users where email = ? ",new String[] {email} );
        return cursor;
    }

    //----------------------------------------------------------------------------------------------------------------------
    //Count peta
    public Cursor counta(String email) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select count(peta_id) from users where email = ?",new String[] {email} );
        return cursor;
    }

    public Cursor getdesc(String name,String breed,String gender,String location ) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select description from pets where name = ? and breed = ? and gender = ? and location = ?",new String[] {name,breed,gender,location} );
        return cursor;
    }

    public Cursor getdpeta_id(String name,String breed,String gender,String location ) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select pet_id from pets where name = ? and breed = ? and gender = ? and location = ?",new String[] {name,breed,gender,location} );
        return cursor;
    }



}