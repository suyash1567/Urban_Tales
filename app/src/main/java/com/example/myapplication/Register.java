package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    private EditText email,firstname,lastname,password,confirmpassword;
    Button register;
    TextView login;
    private static final String TAG = "Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirmpassword);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);

        // Register Activity to Login activity
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
            }
        });

        //Register to main page
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String firstname_txt = firstname.getText().toString();
                String lastname_txt = lastname.getText().toString();
                String email_txt = email.getText().toString();
                String password_txt = password.getText().toString();
                String confirmpassword_txt = confirmpassword.getText().toString();


                if(TextUtils.isEmpty(firstname_txt)){
                    firstname.setError("Enter First name");
                    firstname.requestFocus();
                }
                else if(TextUtils.isEmpty(lastname_txt)){
                    lastname.setError("Enter Last name");
                    lastname.requestFocus();
                }
                else if(TextUtils.isEmpty(email_txt)){
                    email.setError("Enter Email Id");
                    email.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email_txt).matches()){
                    email.setError("Enter valid Email id");
                    email.requestFocus();
                }
                else if(TextUtils.isEmpty(password_txt)){
                    password.setError("Enter Password");
                    password.requestFocus();
                }
                else if(TextUtils.isEmpty(confirmpassword_txt)){
                    confirmpassword.setError("This Field is required");
                    confirmpassword.requestFocus();
                }
                else if(!password_txt.equals(confirmpassword_txt)){
                    confirmpassword.setError("Passwords don't match");
                    confirmpassword.requestFocus();
                    password.clearComposingText();
                    confirmpassword.clearComposingText();
                }
                else{
                    registerUser(firstname_txt,lastname_txt,email_txt,password_txt);
                }

            }
        });
    }
    //User Registration using the given credentials using FireBase
    private void registerUser(String firstname_txt, String lastname_txt, String email_txt, String password_txt) {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    auth.createUserWithEmailAndPassword(email_txt,password_txt).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){

                FirebaseUser firebaseUser = auth.getCurrentUser();
                // Enter User Details in the Database
                ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(firstname_txt,lastname_txt,email_txt,password_txt);

                //Extracting user reference from database for Registered Users
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Users");
                reference.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //Send Verification Email
                            firebaseUser.sendEmailVerification();
                            Toast.makeText(Register.this, "user created", Toast.LENGTH_SHORT).show();
                            //Open The Main Page
//                             Intent intent = new Intent(Register.this,MainActivity.class);
//                             intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                             startActivity(intent);
//
//                    //     To finish the Registration Activity and prevent the user from going back to it
//                             finish();

                        } else {
                            Toast.makeText(Register.this, "Registration Failed", Toast.LENGTH_SHORT).show();

                        }




                    }
                });


            }
            else{
                try{
                    throw task.getException();
                } catch(FirebaseAuthWeakPasswordException e){
                    password.setError("Password is too weak");
                    password.requestFocus();
                } catch(FirebaseAuthInvalidCredentialsException e){
                    email.setError("Your Email is invalid or already in use");
                    email.requestFocus();
                } catch (FirebaseAuthUserCollisionException e){
                    email.setError("User is already Registered with this email");
                } catch (Exception e){
                    Log.e(TAG,e.getMessage());
                    Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    });

    }

}