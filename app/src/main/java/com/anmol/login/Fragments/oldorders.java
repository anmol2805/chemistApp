package com.anmol.login.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.anmol.login.Adapters.CurrentAdapter;
import com.anmol.login.Adapters.OldAdapter;
import com.anmol.login.Model.Currentorder;
import com.anmol.login.Model.Oldorder;
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

public class oldorders extends android.support.v4.app.Fragment {
    List<Oldorder> oldorders = new ArrayList<>();
    OldAdapter oldAdapter;
    ListView list;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference reqdatabase,olddatabase;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.oldorders,container,false);
        list = (ListView)vi.findViewById(R.id.olist);
        reqdatabase = FirebaseDatabase.getInstance().getReference().child("orders").child("request");
        olddatabase = reqdatabase.child("old").child(auth.getCurrentUser().getUid());
        olddatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                oldorders.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    String date = data.child("date").getValue().toString();
                    String add = data.child("address").getValue().toString();
                    String name = data.child("name").getValue().toString();
                    String phone1 = data.child("phone").getValue().toString();
                    String phone2 = data.child("phone2").getValue().toString();
                    String oid = data.getKey();
                    Oldorder oldorder = new Oldorder(date,name,add,phone1,phone2,oid);
                    oldorders.add(oldorder);

                }
                if(getActivity()!=null){
                    oldAdapter = new OldAdapter(getActivity(),R.layout.oorder,oldorders);
                    oldAdapter.notifyDataSetChanged();
                    list.setAdapter(oldAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return vi;
    }
}
