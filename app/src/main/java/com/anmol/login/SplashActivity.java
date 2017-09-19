package com.anmol.login;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("orders").child("chemist");
    Boolean check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.pg);
        if(auth.getCurrentUser()!=null){
            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot data:dataSnapshot.getChildren()){
                        String key = data.getKey();
                        if(key.contains(auth.getCurrentUser().getUid())){
                            check = true;
                        }
                    }
                    if(check){
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(SplashActivity.this,NavActivity.class));
                        finish();
                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(SplashActivity.this,ProfileActivity.class));
                        finish();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                    finish();
                }
            },3000);
        }
    }
}
