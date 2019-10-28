package com.example.flex;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LicenseDetailsActivity extends AppCompatActivity {

    DatabaseReference dlRef, dbRef;
    FirebaseUser user;
    View parentLayout;
    TextView tvDLNumber, tvName, tvDOB, tvAddress, tvIssueDate, tvExpiryDate ;
    String checkMail = "", umail = "", dlNumber, dlName, DOB, address, issueDate, expiryDate;

    @RequiresApi(api=Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_details);

        final ProgressDialog pd = ProgressDialog.show(LicenseDetailsActivity.this,"Loading License Details","Please wait...",true);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1db945")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbRef = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        checkMail = user.getEmail();
        parentLayout = findViewById(android.R.id.content);

        ImageView ivHome = (ImageView) findViewById(R.id.ivHome);

        tvDLNumber = (TextView) findViewById(R.id.tvDLNumber);
        tvName = (TextView) findViewById(R.id.tvDLName);
        tvDOB = (TextView) findViewById(R.id.tvDOB);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvIssueDate = (TextView) findViewById(R.id.tvIssueDate);
        tvExpiryDate = (TextView) findViewById(R.id.tvExpiryDate);


        dlRef = dbRef.child("DL");

        ValueEventListener dlListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    umail = ds.child("userMail").getValue(String.class);

                    assert umail != null;
                    if(umail.equals(checkMail))
                    {
                        dlNumber = ds.child("licenseNumber").getValue(String.class);
                        dlName = ds.child("userName").getValue(String.class);
                        DOB = ds.child("userDOB").getValue(String.class);
                        address = ds.child("userAddress").getValue(String.class);
                        issueDate = ds.child("licenseIssueDate").getValue(String.class);
                        expiryDate = ds.child("licenseExpiryDate").getValue(String.class);

                        break;
                    }
                }

                tvDLNumber.setText(dlNumber);
                tvName.setText(dlName);
                tvDOB.setText(DOB);
                tvAddress.setText(address);
                tvIssueDate.setText(issueDate);
                tvExpiryDate.setText(expiryDate);

                pd.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                pd.dismiss();

                Snackbar.make(parentLayout, "Failed to load license details", Snackbar.LENGTH_LONG)
                        .setDuration(3000)
                        .setAction("Close", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.background_light))
                        .show();


            }
        };

        dlRef.addListenerForSingleValueEvent(dlListener);


        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(LicenseDetailsActivity.this, MainActivity.class));
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent it = new Intent(getApplicationContext(),MainActivity.class);
        startActivityForResult(it,0);
        return true;
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(LicenseDetailsActivity.this,MainActivity.class));
        finish();
    }
}