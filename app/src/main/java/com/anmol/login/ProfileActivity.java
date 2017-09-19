package com.anmol.login;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.anmol.login.Model.Owner;
import com.anmol.login.Model.Working;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.cast.TextTrackStyle;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseReference mFirebaseDatabase,mGeoDatabase,mWorkingDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    Boolean mon = false,tue = false,wed = false,thurs = false,fri = false,sat = false,sun = false;
    CheckBox monday,tuesday,wednesday,thursday,friday,saturday,sunday;
    Button save;
    EditText name;
    EditText ownname;
    EditText address;
    EditText contact;
    EditText minorder;
    EditText estdeltime;
    EditText estdeltimel;
    EditText delrad;
    EditText workhrs;
    EditText workhrsc;
    EditText discper;
    EditText landphone;
    RadioButton hours,days;
    CheckBox alltime;
    String mownname,mname,madd,mcont,mminorder,mestdeltime,mdelrad,mwrkhrs,mwrkhrsc,mdiscper,lat,log,mlandphone,mestdeltimel;
    GeoFire geoFire;
    double lattitude,longitude;
    Button locate;
    SharedPreferences sharedPref;
    SessionManagement session;
    private final int REQUEST_CODE_PLACEPICKER = 1;
    Owner ownerref;
    Working workref;
    float average_time;
    int open,close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sharedPref = getPreferences(MODE_PRIVATE);
        String UserEmail = sharedPref.getString("firebasekey", "");
        session = new SessionManagement(getApplicationContext());
        save = (Button)findViewById(R.id.save);
        name = (EditText)findViewById(R.id.name);
        ownname = (EditText)findViewById(R.id.ownname);
        address = (EditText)findViewById(R.id.address);
        contact = (EditText)findViewById(R.id.phone);
        minorder  =(EditText)findViewById(R.id.minorder);
        estdeltime = (EditText)findViewById(R.id.estdeltime);
        estdeltimel = (EditText)findViewById(R.id.estdeltimel);
        delrad  =(EditText)findViewById(R.id.delrad);
        workhrs = (EditText)findViewById(R.id.workinghours);
        workhrsc = (EditText)findViewById(R.id.workinghoursc);
        discper = (EditText)findViewById(R.id.disc);
        locate = (Button)findViewById(R.id.selectloc);
        landphone = (EditText)findViewById(R.id.landphone);
        hours = (RadioButton)findViewById(R.id.hours);
        days = (RadioButton)findViewById(R.id.days);
        alltime = (CheckBox)findViewById(R.id.alltime);
        monday = (CheckBox)findViewById(R.id.monday);
        tuesday = (CheckBox)findViewById(R.id.tuesday);
        wednesday = (CheckBox)findViewById(R.id.wednesday);
        thursday = (CheckBox)findViewById(R.id.thursday);
        friday = (CheckBox)findViewById(R.id.friday);
        saturday = (CheckBox)findViewById(R.id.saturday);
        sunday = (CheckBox)findViewById(R.id.sunday);
        hours.setChecked(true);
        hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hours.setChecked(true);
                days.setChecked(false);
            }
        });
        days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                days.setChecked(true);
                hours.setChecked(false);
            }
        });
        alltime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    workhrs.setVisibility(View.GONE);
                    workhrsc.setVisibility(View.GONE);
                }
                else if(!compoundButton.isChecked()){
                    workhrsc.setVisibility(View.VISIBLE);
                    workhrs.setVisibility(View.VISIBLE);
                }
            }
        });
        monday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    mon = true;
                }
                else if(!compoundButton.isChecked()) {
                    mon = false;
                }
            }
        });
        tuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    tue = true;
                }
                else if(!compoundButton.isChecked()) {
                    tue = false;
                }
            }
        });
        wednesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    wed = true;
                }
                else if(!compoundButton.isChecked()) {
                    wed = false;
                }
            }
        });
        thursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    thurs = true;
                }
                else if(!compoundButton.isChecked()) {
                    thurs = false;
                }
            }
        });
        friday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    fri = true;
                }
                else if(!compoundButton.isChecked()) {
                    fri = false;
                }
            }
        });
        saturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    sat = true;
                }
                else if(!compoundButton.isChecked()) {
                    sat = false;
                }
            }
        });
        sunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    sun = true;
                }
                else if(!compoundButton.isChecked()) {
                    sun = false;
                }
            }
        });
        Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();


        /**
         * Call this function whenever you want to check user login
         * This will redirect user to LoginActivity is he is not
         * logged in
         * */
        session.checkLogin();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // name


        // email
        String email = user.get(SessionManagement.KEY_EMAIL);

        geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference().getRoot());
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null){
            String userEmail = mAuth.getCurrentUser().getEmail();
            sharedPref = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("firebasekey", userEmail);
            editor.commit();
        }
        
        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    buildAlertMessageNoGps();
                    buildAlertMessageNoInternet();
                }
                else
                startPlacepickerActivity();
            }

            
        });


        workhrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ProfileActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        open = selectedHour;
                        String AM_PM ;
                        if(selectedHour < 12) {
                            AM_PM = "AM";
                        } else {
                            selectedHour = selectedHour - 12;
                            AM_PM = "PM";
                        }
                        workhrs.setText( selectedHour + ":" + selectedMinute + " " + AM_PM);
                    }
                }, hour, minute, false);//Yes ur 24 hotime
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        workhrsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ProfileActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        close = selectedHour;
                        if (close<open){
                            workhrsc.setError("Please provide a valid closing time");
                        }else{
                            String AM_PM ;
                            if(selectedHour < 12) {
                                AM_PM = "AM";
                            } else {
                                selectedHour = selectedHour - 12;
                                AM_PM = "PM";
                            }
                            workhrsc.setText( selectedHour + ":" + selectedMinute + " " + AM_PM);
                            workhrsc.setError(null);
                        }


                    }
                }, hour, minute, false);//Yes ur 24 hotime
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(buttonClick);
                mname = name.getText().toString().trim();
                mownname = ownname.getText().toString().trim();
                mcont = contact.getText().toString().trim();
                mlandphone = landphone.getText().toString().trim();
                mlandphone = "011" + mlandphone;
                mminorder = minorder.getText().toString().trim();



                mestdeltime = estdeltime.getText().toString().trim();
                mestdeltimel = estdeltimel.getText().toString().trim();

                mdelrad = delrad.getText().toString().trim();

                mwrkhrs = workhrs.getText().toString().trim();
                mwrkhrsc = workhrsc.getText().toString().trim();
                mwrkhrs = mwrkhrs + "-" + mwrkhrsc;
                if(alltime.isChecked()){

                    mwrkhrs = "24 hours open";
                }


                mdiscper = discper.getText().toString().trim();



                madd = address.getText().toString().trim();
                if(!TextUtils.isEmpty(mname)
                        && !TextUtils.isEmpty(mownname)
                        && (mcont.length()==10)
                        && (mlandphone.length() == 11)
                        && !TextUtils.isEmpty(mdiscper)
                        && !TextUtils.isEmpty(mminorder)
                        && !TextUtils.isEmpty(mestdeltime)
                        && !TextUtils.isEmpty(mestdeltimel)
                        && !TextUtils.isEmpty(mdelrad)
                        && !TextUtils.isEmpty(mwrkhrs)
                        && !TextUtils.isEmpty(madd)
                        && (Integer.parseInt(mestdeltime)<Integer.parseInt(mestdeltimel))

                        ){
                    int early = Integer.parseInt(mestdeltime);
                    int latest = Integer.parseInt(mestdeltimel);
                    if(latest<early){
                        estdeltimel.setError("Please Enter a valid latest delivery time");
                    }
                    if(days.isChecked()){
                        early = early*24;
                        mestdeltime = String.valueOf(early);
                        latest = latest*24;
                        mestdeltimel = String.valueOf(latest);
                    }
                    average_time = (early + latest)/2;

                    String est = mestdeltime + "-" + mestdeltimel + " hours";
                    int minimum_order = Integer.parseInt(mminorder);
                    int discount = Integer.parseInt(mdiscper);
                    int delivery_radius = Integer.parseInt(mdelrad);
                    Geocoder geocoder = new Geocoder(getApplicationContext());
                    List<Address> addresses;
                    try {
                        addresses = geocoder.getFromLocationName(madd, 1);
                        if(addresses.size() > 0) {
                            lattitude= addresses.get(0).getLatitude();
                            longitude= addresses.get(0).getLongitude();
                            lat = String.valueOf(lattitude);
                            log = String.valueOf(longitude);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mAuth = FirebaseAuth.getInstance();
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user!=null){
                        DatabaseReference geodata = FirebaseDatabase.getInstance().getReference().getRoot().child("orders").child("chemistlocation");
                        mGeoDatabase = FirebaseDatabase.getInstance().getReference().getRoot().child("orders").child("chemist").child(user.getUid() + "_" + mdelrad);
                        mFirebaseDatabase = mGeoDatabase;
                        mWorkingDatabase = mFirebaseDatabase.child("Working Days");
                        workref = new Working(mon,tue,wed,thurs,fri,sat,sun);
                        String token = FirebaseInstanceId.getInstance().getToken();
                        ownerref = new Owner(mname,mownname,mcont,mlandphone,minimum_order,est,delivery_radius,mwrkhrs,discount,madd,average_time,token);
                        mFirebaseDatabase.setValue(ownerref).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ProfileActivity.this,"Task DOne",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ProfileActivity.this,NavActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                        mWorkingDatabase.setValue(workref);


                        geoFire = new GeoFire(mGeoDatabase);
                        geoFire.setLocation(user.getUid() + "_" + mdelrad, new GeoLocation(lattitude, longitude));


                    }

                }
                else{
                    if(mownname.isEmpty()){
                        ownname.setError("Please Enter Owner name");
                    }
                    else if(mname.isEmpty()){
                        name.setError("Please Enter Shop name");
                    }
                    else if(mcont.length() != 10){
                        contact.setError("Please provide a valid mobile number");
                    }
                    else if(mlandphone.length()!=11){
                        landphone.setError("Please provide a valid landline number");
                    }
                    else if(madd.isEmpty()){
                        address.setError("Please provide your address");
                    }
                    else if(mminorder.isEmpty()){
                        minorder.setError("Please provide a minimum order");
                    }
                    else if(mestdeltime.isEmpty()){
                        estdeltime.setError("Please provide earliest delivery time");
                    }
                    else if(mestdeltimel.isEmpty()){
                        estdeltimel.setError("Please provide latest delivery time");
                    }
                    else if(Integer.parseInt(mestdeltime)>Integer.parseInt(mestdeltimel)){
                        estdeltimel.setError("Please provide a valid time");
                    }
                    else if(mdelrad.isEmpty()){
                        delrad.setError("PLease provide your delivery zone");
                    }
                    else if(mwrkhrs.isEmpty()){
                        workhrs.setError("Please provide your store opening time");
                    }
                    else if(mwrkhrsc.isEmpty()){
                        workhrsc.setError("Please provide closing time of the store");
                    }
                    else if(mdiscper.isEmpty()){
                        discper.setError("Please provide a minimum discount");
                    }


                }



            }
        });
    }



    private void startPlacepickerActivity() {
        PlacePicker.IntentBuilder intentbuilder = new PlacePicker.IntentBuilder();
        try {
            Intent intent = intentbuilder.build(this);
            startActivityForResult(intent,REQUEST_CODE_PLACEPICKER);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_PLACEPICKER && resultCode == RESULT_OK){
            display(data);
        }
    }

    private void display(Intent data) {
        Place place = PlacePicker.getPlace(data,this);
        String add = place.getAddress().toString();
        address.setError(null);
        address.setText(add);

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    private void buildAlertMessageNoInternet() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please also ensure that you have active internet conection")
                .setCancelable(false)
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_SETTINGS));
                    }
                }).setNegativeButton("Already Enabled", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
}