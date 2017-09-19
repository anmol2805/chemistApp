package com.anmol.login.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.anmol.login.Orderdetails;
import com.anmol.login.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by anmol on 9/18/2017.
 */

public class NewAdapter extends ArrayAdapter<Neworder> {
    private Activity context;
    private int resource;
    private List<Neworder>neworders;

    public NewAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<Neworder> objects){
        super(context,resource,objects);
        this.context = context;
        this.resource = resource;
        neworders = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(resource,null);
        TextView date = (TextView)v.findViewById(R.id.date);
        date.setText(neworders.get(position).getDate());
        final TextView address = (TextView)v.findViewById(R.id.add);
        address.setText(neworders.get(position).getAddress());
        Button reject = (Button)v.findViewById(R.id.delete);
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {String oid = neworders.get(position).getOid();
                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("orders").child("request").child("new").child(auth.getCurrentUser().getUid()).child(oid);
                db.removeValue();
            }
        });
        Button viewdet = (Button)v.findViewById(R.id.view);
        viewdet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick
                (View view) {
                Intent intent = new Intent(context,Orderdetails.class);
                intent.putExtra("oid",neworders.get(position).getOid());
                context.startActivity(intent);

            }
        });

        return v;
    }
}
