package com.anmol.login;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class COrderdetails extends AppCompatActivity {
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
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(COrderdetails.this);
                dialog.setContentView(R.layout.chat);
                /*
                Your code here
                 */
                dialog.show();
            }
        });
        from.child(oid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String date = dataSnapshot.child("date").getValue().toString();
                String add = dataSnapshot.child("address").getValue().toString();
                String des = dataSnapshot.child("description").getValue().toString();
                ddate.setText(date);
                dadd.setText(add);
                ddes.setText(des);
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
                                Glide.with(COrderdetails.this).load(medias.get(pos).getUrl()).into(previewimg);
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
                    madapter = new MyAdapter(COrderdetails.this,medias,itemClickListener);
                    madapter.notifyDataSetChanged();
                    rv.setAdapter(madapter);



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

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
