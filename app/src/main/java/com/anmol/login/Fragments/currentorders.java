package com.anmol.login.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.anmol.login.Adapters.CurrentAdapter;
import com.anmol.login.Adapters.NewAdapter;
import com.anmol.login.Model.Currentorder;
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

public class currentorders extends android.support.v4.app.Fragment {
    List<Currentorder> currentorders = new ArrayList<>();
    CurrentAdapter currentAdapter;
    ListView list;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference reqdatabase,currentdatabase;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View vi = inflater.inflate(R.layout.currentorders, container, false);
        list = (ListView)vi.findViewById(R.id.clist);
        reqdatabase = FirebaseDatabase.getInstance().getReference().child("orders").child("request");
        currentdatabase = reqdatabase.child("current").child(auth.getCurrentUser().getUid());
        currentdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentorders.clear();
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    String date = data.child("date").getValue().toString();
                    String add = data.child("address").getValue().toString();
                    String name = data.child("name").getValue().toString();
                    String phone1 = data.child("phone").getValue().toString();
                    String phone2 = data.child("phone2").getValue().toString();
                    String oid = data.getKey();
                    Currentorder currentorder = new Currentorder(date,name,add,phone1,phone2,oid);
                    currentorders.add(currentorder);

                }
                if(getActivity()!=null){
                    currentAdapter = new CurrentAdapter(getActivity(),R.layout.corder,currentorders);
                    currentAdapter.notifyDataSetChanged();
                    list.setAdapter(currentAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return vi;
    }
}
