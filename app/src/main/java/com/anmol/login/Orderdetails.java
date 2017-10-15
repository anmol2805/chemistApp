package com.anmol.login;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anmol.login.Adapters.MyAdapter;
import com.anmol.login.Model.Media;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Orderdetails extends AppCompatActivity {
    String oid;
    Button buy;
    TextView ddate,dadd,ddes;
    Bitmap bmp;
    private List<Media> medias = new ArrayList<>();
    MyAdapter madapter;
    RecyclerView rv;
    ImageView previewimg;
    String source,edittype,uid;
    Uri edituri;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference from = FirebaseDatabase.getInstance().getReference().child("orders").child("request").child("new").child(auth.getCurrentUser().getUid());
    DatabaseReference todata = FirebaseDatabase.getInstance().getReference().child("orders").child("request").child("current").child(auth.getCurrentUser().getUid());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails);
        oid = getIntent().getStringExtra("oid");
        buy = (Button)findViewById(R.id.buy);
        ddate = (TextView)findViewById(R.id.date);
        dadd = (TextView)findViewById(R.id.address);
        ddes = (TextView)findViewById(R.id.description);
        previewimg = (ImageView)findViewById(R.id.previewimg);
        rv = (RecyclerView)findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        from.child(oid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null){
                    String date = dataSnapshot.child("date").getValue().toString();
                    String add = dataSnapshot.child("address").getValue().toString();
                    String des = dataSnapshot.child("description").getValue().toString();
                    ddate.setText(date);
                    dadd.setText(add);
                    ddes.setText(des);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        if(from.child(oid).child("presuploaded")!=null){
            from.child(oid).child("presuploaded").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterator iterator = dataSnapshot.getChildren().iterator();


                    Set<Media> set = new HashSet<Media>();
                    while (iterator.hasNext()) {
                        set.add((Media) ((DataSnapshot) iterator.next()).getValue(Media.class));
                    }
                    medias.clear();
                    medias.addAll(set);
                    ItemClickListener itemClickListener = new ItemClickListener() {
                        @Override
                        public void onItemClick(int pos) {
                            if((medias.get(pos).getType()).contains("image")){
                                Glide.with(Orderdetails.this).load(medias.get(pos).getUrl()).into(previewimg);
                                source = medias.get(pos).getPresuri();
                                edituri = Uri.parse(medias.get(pos).getPresuri());
                                edittype = medias.get(pos).getType();

                                uid = medias.get(pos).getUploadid();

                            }
                            else{
                                generateImageFromPdf(Uri.parse(medias.get(pos).getPresuri()));
                                previewimg.setImageBitmap(bmp);

                            }
                        }
                    };
                    madapter = new MyAdapter(Orderdetails.this,medias,itemClickListener);
                    madapter.notifyDataSetChanged();
                    rv.setAdapter(madapter);



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                from.child(oid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String uid  = dataSnapshot.child("uid").getValue().toString();
                        String oid  = dataSnapshot.child("oid").getValue().toString();
                        final DatabaseReference db =  FirebaseDatabase.getInstance().getReference().child("orders").child("customers").child(uid).child(oid);
                        final Map<String,Object> map = new HashMap<>();
                        map.put("status",true);
                        db.updateChildren(map);
                        final DatabaseReference cdb = FirebaseDatabase.getInstance().getReference().child("orders").child("chemist");
                        cdb.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot data:dataSnapshot.getChildren()){
                                    if (data.getKey().contains(auth.getCurrentUser().getUid())){
                                        Toast.makeText(Orderdetails.this,data.getKey(),Toast.LENGTH_SHORT).show();
//                                        String accepted_by = dataSnapshot.child("mname").getValue().toString();
//                                        String chemistphone = dataSnapshot.child("mcont").getValue().toString();
//                                        int due = 250;
//                                        String chemistland = dataSnapshot.child("mlandphone").getValue().toString();
//                                        map.put("accepted_by",accepted_by);
//                                        map.put("chemistphone",chemistphone);
//                                        map.put("chemistland",chemistland);
//                                        map.put("due",due);
//                                        db.updateChildren(map);

                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                movePres(from.child(oid),todata.child(oid));
                from.child(oid).removeValue();
                finish();
            }
        });

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
    void generateImageFromPdf(Uri pdfUri) {
        int pageNumber = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(this);
        try {
            //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
            ParcelFileDescriptor fd = getContentResolver().openFileDescriptor(pdfUri, "r");
            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            pdfiumCore.openPage(pdfDocument, pageNumber);
            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height);

            pdfiumCore.closeDocument(pdfDocument);// important!

        } catch(Exception e) {
            //todo with exception
        }
    }
}
