package com.example.flex;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity implements ValueEventListener {

    private EditText etName, etEmail, etPhone, etPassword, etConfirmPassword;
    private FirebaseAuth auth;
    private DatabaseReference databaseUser, databaseFeedback, databaseSlot, databaseDL, databaseHistory;
    private View parentLayout;
    static int registerFlag = 0;

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(RegisterActivity.this, android.R.color.background_light));// set status background white
        }

        etName=findViewById(R.id.etName);
        etEmail=findViewById(R.id.etEmail);
        etPhone=findViewById(R.id.etPhone);
        etPassword=findViewById(R.id.etPassword);
        etConfirmPassword=findViewById(R.id.etConfirmPassword);
        Button btnSignUp=findViewById(R.id.btnProfile);
        TextView tvLogin=findViewById(R.id.tvLogin);
        auth = FirebaseAuth.getInstance();
        parentLayout = findViewById(android.R.id.content);

        databaseUser = FirebaseDatabase.getInstance().getReference("User");
        databaseFeedback = FirebaseDatabase.getInstance().getReference("Feedback");
        databaseSlot = FirebaseDatabase.getInstance().getReference("Slot");
        databaseDL = FirebaseDatabase.getInstance().getReference("DL");
        databaseHistory = FirebaseDatabase.getInstance().getReference("History");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                String name = etName.getText().toString();
                String phone_no =etPhone.getText().toString().trim();
                final String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if(name.length()==0) {
                    Snackbar snackbar = Snackbar.make(parentLayout, "Please enter your name ", Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            }).setActionTextColor(getResources().getColor(android.R.color.background_light));

                    snackbar.show();

                    etName.requestFocus();
                } else if(phone_no.length()==0) {
                    Snackbar.make(parentLayout,"Please enter your phone number ", Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();

                    etPhone.requestFocus();
                } else if (phone_no.length() != 10) {
                    Snackbar.make(parentLayout, "Invalid phone number ", Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();

                    etPhone.requestFocus();
                } else if (email.length() == 0) {
                    Snackbar.make(parentLayout,"Please enter your email ", Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();
                    etEmail.requestFocus();
                } else if(password.length()==0) {
                    Snackbar.make(parentLayout,"Please enter your password ", Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();
                    etPassword.requestFocus();
                } else if(password.length()<8) {
                    Snackbar.make(parentLayout,"Password must be at least 8 characters ", Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();
                    etPassword.requestFocus();
                } else if(confirmPassword.length()==0) {
                    Snackbar.make(parentLayout,"Please confirm your password ", Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();

                    etConfirmPassword.requestFocus();
                } else if(!confirmPassword.equals(password)) {
                    Snackbar.make(parentLayout,"Password and confirm password fields do not match ", Snackbar.LENGTH_LONG)
                            .setDuration(3000)
                            .setAction("Close", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            })
                            .setActionTextColor(getResources().getColor(android.R.color.background_light))
                            .show();

                    etConfirmPassword.requestFocus();
                } else {

                    addUser();
                    addFeedback();
                    addDL();
                    addSlot();

                }


            }

        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(it);
                finish();
            }
        });

    }

    private void addDL()
    {
        String usrMail = etEmail.getText().toString().trim();
        String userName = "",LicenseNumber="", userDOB="",userAddress="", LicenseIssueDate="", LicenseExpiryDate="";
        int userDLFlag = 0;
        String id = databaseDL.push().getKey();
        DL dl = new DL(id,usrMail, userName,LicenseNumber,userDOB, userAddress, LicenseIssueDate,LicenseExpiryDate,userDLFlag);
        assert id != null;
        databaseDL.child(id).setValue(dl);

    }

    private void addSlot()
    {
        String usrMail = etEmail.getText().toString().trim();
        String showDate="", showStartDate="", showWorkHours="";
        String id = databaseSlot.push().getKey();
        int slotFlag = 0;
        Slot slot=new Slot(usrMail, id, slotFlag, showDate, showStartDate, showWorkHours);
        assert id != null;
        databaseSlot.child(id).setValue(slot);

    }

    private void addFeedback() {

        String checkEmail = etEmail.getText().toString().trim();
        String feedbackRating = "0", feedbackText = "";
        String id = databaseFeedback.push().getKey();
        Feedback feedback = new Feedback(id,checkEmail,feedbackRating,feedbackText);
        assert id != null;
        databaseFeedback.child(id).setValue(feedback);

    }

    private void addUser() {

        final String name=etName.getText().toString().trim();
        final String mail=etEmail.getText().toString().trim();
        final String phone=etPhone.getText().toString().trim();
        final String password=etPassword.getText().toString().trim();
        final int DLFlag = 0, PhotoFlag = 0;


        final ProgressDialog pd=ProgressDialog.show(RegisterActivity.this, "Creating account", "Please wait", true);


            auth.createUserWithEmailAndPassword(mail, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()) {

                                pd.dismiss();
                                Snackbar.make(parentLayout,"Unable to create account. Please try again. ", Snackbar.LENGTH_LONG)
                                        .setDuration(5000)
                                        .setAction("Close", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                            }
                                        })
                                        .setActionTextColor(getResources().getColor(android.R.color.background_light))
                                        .show();

                            } else {

                                pd.dismiss();
                                String id = databaseUser.push().getKey();
                                User user=new User(id, name, mail, phone, DLFlag, PhotoFlag);
                                assert id != null;
                                databaseUser.child(id).setValue(user);
                                registerFlag = 1;
                                Intent it = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(it);
                                finish();

                            }
                        }
                    });

    }


    @Override
    public void onBackPressed() {
        Intent it = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(it);
        finish();
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}