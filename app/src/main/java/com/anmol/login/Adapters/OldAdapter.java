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

import com.anmol.login.Model.Neworder;
import com.anmol.login.Model.Oldorder;
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

public class OldAdapter extends ArrayAdapter<Oldorder> {
    private Activity context;
    private int resource;
    private List<Oldorder>oldorders;

    public OldAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Oldorder> objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        oldorders = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(resource,null);
        TextView date = (TextView)v.findViewById(R.id.date);
        date.setText(oldorders.get(position).getDate());
        TextView address = (TextView)v.findViewById(R.id.address);
        address.setText(oldorders.get(position).getAddress());
        TextView name = (TextView)v.findViewById(R.id.name);
        name.setText(oldorders.get(position).getName());
        return v;
    }


}
