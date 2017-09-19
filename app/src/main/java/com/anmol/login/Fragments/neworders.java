package com.anmol.login.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.anmol.login.Adapters.NewAdapter;
import com.anmol.login.Model.Neworder;
import com.anmol.login.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anmol on 9/16/2017.
 */

public class neworders extends android.support.v4.app.Fragment {
    List<Neworder> neworders = new ArrayList<>();
    NewAdapter newAdapter;
    ListView list;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference reqdatabase,newdatabase;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.neworders, container, false);
        list = (ListView)vi.findViewById(R.id.nlist);
        reqdatabase = FirebaseDatabase.getInstance().getReference().child("orders").child("request");
        newdatabase = reqdatabase.child("new").child(auth.getCurrentUser().getUid());
        newdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                neworders.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    String date = data.child("date").getValue().toString();
                    String add = data.child("address").getValue().toString();
                    String oid = data.getKey();
                    Neworder neworder = new Neworder(date,add,oid);
                    neworders.add(neworder);
                }
                if(getActivity()!=null){
                    newAdapter = new NewAdapter(getActivity(),R.layout.norder,neworders);
                    newAdapter.notifyDataSetChanged();
                    list.setAdapter(newAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return vi;
    }
}
