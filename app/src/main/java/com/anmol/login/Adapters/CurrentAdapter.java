package com.anmol.login.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.anmol.login.Model.Currentorder;
import com.anmol.login.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by anmol on 9/18/2017.
 */

public class CurrentAdapter extends ArrayAdapter<Currentorder> {
    private Activity context;
    private int resource;
    private List<Currentorder>currentorders;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference from = FirebaseDatabase.getInstance().getReference().child("orders").child("request").child("current").child(auth.getCurrentUser().getUid());
    DatabaseReference todata = FirebaseDatabase.getInstance().getReference().child("orders").child("request").child("old").child(auth.getCurrentUser().getUid());
    public CurrentAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Currentorder> objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        currentorders = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(resource,null);
        TextView date = (TextView)v.findViewById(R.id.date);
        date.setText(currentorders.get(position).getDate());
        TextView address = (TextView)v.findViewById(R.id.address);
        address.setText(currentorders.get(position).getAddress());
        TextView name = (TextView)v.findViewById(R.id.name);
        name.setText(currentorders.get(position).getName());
        Button comp = (Button)v.findViewById(R.id.comp);
        comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movePres(from.child(currentorders.get(position).getOid()), todata.child(currentorders.get(position).getOid()));
                from.child(currentorders.get(position).getOid()).removeValue();
            }
        });

        return v;
    }
    private void movePres(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {
                            System.out.println("Copy failed");
                        } else {
                            System.out.println("Success");

                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
