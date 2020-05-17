package com.pramudya.e_tani;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pramudya.e_tani.pdModel.pdModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailPetani extends AppCompatActivity {
    TextView tvnama,tvalamat,tvnohp,tvumur,tvpengalaman,tvspesialis;

    DatabaseReference rootref;
    Query queryFirebase;
    ValueEventListener valueEventListener;
    CircleImageView fotopetani;
    ImageView coverpetani;
    String no_hpuser="",nama="";
    TextView tvlatitude,tvlongitude;
    Button btnhubungi;
    ImageButton btndatangi,btnWA;

    private boolean isOpen = false;
    private ConstraintSet layout1,layout2;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_petani);

        Intent intent=getIntent();
        no_hpuser=intent.getStringExtra("no_telp");
        nama=intent.getStringExtra("nama");
        btndatangi=(ImageButton) findViewById(R.id.btndatangi);
        btnhubungi=(Button)findViewById(R.id.btnhubungi);
        rootref= FirebaseDatabase.getInstance().getReference();
        fotopetani=(CircleImageView)findViewById(R.id.imgp) ;
        coverpetani=(ImageView)findViewById(R.id.cover);
        tvnama=(TextView)findViewById(R.id.tvnama);
        tvalamat=(TextView)findViewById(R.id.tvalamat);
        tvnohp=(TextView)findViewById(R.id.tvnohp);
        tvumur=(TextView)findViewById(R.id.tvumur);
        tvpengalaman=(TextView)findViewById(R.id.tvpengalaman);
        tvspesialis=(TextView)findViewById(R.id.tvspesialis);
        tvlatitude = (TextView) findViewById(R.id.tvlatitude);
        tvlongitude = (TextView) findViewById(R.id.tvlongitude);
        btnWA = (ImageButton) findViewById(R.id.btnWA) ;
        getdatauser(no_hpuser);
        pdModel.pdData(DetailPetani.this);

//        Window w = getWindow();
//        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        layout1 = new ConstraintSet();
//        layout2 = new ConstraintSet();
//        constraintLayout = findViewById(R.id.constraint_layout);
//        layout2.clone(this,R.layout.profile_expanded);
//        layout1.clone(constraintLayout);

//        coverpetani.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onClick(View view) {
//                if (!isOpen){
//                    TransitionManager.beginDelayedTransition(constraintLayout);
//                    layout2.applyTo(constraintLayout);
//                    isOpen = !isOpen;
//                }
//                else {
//                    TransitionManager.beginDelayedTransition(constraintLayout);
//                    layout1.applyTo(constraintLayout);
//                    isOpen = !isOpen;
//                }
//            }
//        });

        btnWA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kirimWA=new Intent(Intent.ACTION_SEND);
                kirimWA.setType("text/plain");
                kirimWA.putExtra(Intent.EXTRA_TEXT, "Permisi bapak/ibu "+tvnama.getText()+" saya tertarik dengan profil anda di aplikasi E-Tani.\n" +
                        "Bolehkah bicara lebih lanjut?");
                kirimWA.putExtra("jid", no_hpuser.substring(1)+ "@s.whatsapp.net");
//                kirimWA.putExtra("jid", "628123157897"+ "@s.whatsapp.net");
                kirimWA.setPackage("com.whatsapp");
                startActivity(kirimWA);
            }
        });

        btnhubungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+tvnohp.getText().toString()));
                startActivity(intent);
            }
        });
        btndatangi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://maps.google.com/maps?daddr=" +tvlatitude.getText().toString() + "," +tvlongitude.getText().toString())));
            }
        });
    }



    public void getdatauser(String no_telp){
        queryFirebase = rootref.child("petani").orderByChild("no_telp").equalTo(no_telp);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String nama = "";
                String alamat = "";
                String nohp = "";
                String spesialis = "";
                String pengalaman = "";
                String umur = "";
                String foto = "";
                String cover = "";
                String slatitude = "";
                String slongitude = "";


                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    nama= ds.child("nama").getValue().toString();
                    alamat= ds.child("alamat").getValue().toString();
                    nohp= ds.child("no_telp").getValue().toString();
                    pengalaman= ds.child("pengamalan").getValue().toString();
                    spesialis= ds.child("spesialis").getValue().toString();
                    umur= ds.child("umur").getValue().toString();
                    foto= ds.child("foto").getValue().toString();
                    cover = ds.child("foto").getValue().toString();
                    slatitude= ds.child("latitude").getValue().toString();
                    slongitude= ds.child("longitude").getValue().toString();
                }
                tvnama.setText(nama);
                tvalamat.setText(alamat);
                tvnohp.setText(nohp);
                tvpengalaman.setText(pengalaman);
                tvspesialis.setText(spesialis);
                tvumur.setText(umur);
                tvlatitude.setText(slatitude);
                tvlongitude.setText(slongitude);
                Picasso.get().load(cover)
                        .into(coverpetani);
                Picasso.get().load(foto)
                        .placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background)
                        .into(fotopetani);
                pdModel.hideProgressDialog();



            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pdModel.hideProgressDialog();
            }


        };


        queryFirebase.addValueEventListener(valueEventListener);
    }
}
