package com.pramudya.e_tani;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class PetaniActivity extends AppCompatActivity {
    TextView tvnama,tvalamat,tvnohp,tvumur,tvpengalaman,tvspesialis,tvubahprofile,tvtentang,tvhubkami,tvlogout,tvhapuskaun;
    LinearLayout lvhome,lvprofile,lvhome2;
    DatabaseReference rootref;
    Query queryFirebase;
    ValueEventListener valueEventListener;
    CircleImageView fotopetani;
    String no_hpuser="";
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_petani);
        Intent intent=getIntent();
        no_hpuser=intent.getStringExtra("no_telp");
        rootref= FirebaseDatabase.getInstance().getReference();
        fotopetani=(CircleImageView)findViewById(R.id.imgp) ;
        tvnama=(TextView)findViewById(R.id.tvnama);
        tvalamat=(TextView)findViewById(R.id.tvalamat);
        tvnohp=(TextView)findViewById(R.id.tvnohp);
        tvumur=(TextView)findViewById(R.id.tvumur);
        tvpengalaman=(TextView)findViewById(R.id.tvpengalaman);
        tvspesialis=(TextView)findViewById(R.id.tvspesialis);
        tvubahprofile=(TextView)findViewById(R.id.tvubahprofile);
        tvtentang=(TextView)findViewById(R.id.tvtentang);
        tvhubkami=(TextView)findViewById(R.id.tvhubkami);
        tvlogout=(TextView)findViewById(R.id.tvlogout);
        tvhapuskaun=(TextView)findViewById(R.id.tvhapusakun);
        lvhome2=(LinearLayout)findViewById(R.id.lvhome2);
        lvhome=(LinearLayout)findViewById(R.id.lvhome);
        lvprofile=(LinearLayout)findViewById(R.id.lvprofile);
        lvhome.setVisibility(View.VISIBLE);
        lvhome2.setVisibility(View.VISIBLE);
        lvprofile.setVisibility(View.GONE);
        showAlertDialog(R.layout.dialog_petani_sukses);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        lvhome.setVisibility(View.VISIBLE);
                        lvhome2.setVisibility(View.VISIBLE);
                        lvprofile.setVisibility(View.GONE);
                        break;
                    case R.id.navigation_profile:
                        lvhome.setVisibility(View.GONE);
                        lvhome2.setVisibility(View.GONE);
                        lvprofile.setVisibility(View.VISIBLE);
                        break;

                }
                return false;
            }
        });
        tvtentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    munculDialog("Tentang","Aplikasi e tani adalah .....");
            }
        });
        tvhubkami.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                munculDialog("Hubungi Kami","081615731024");
            }
        });
        tvlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        PetaniActivity.this);


                alertDialogBuilder.setTitle("Logout");


                alertDialogBuilder
                        .setMessage("Logout App ?")

                        .setCancelable(false)
                        .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                logoutSave();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                ;


                AlertDialog alertDialog = alertDialogBuilder.create();


                alertDialog.show();
            }
        });
        tvhapuskaun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        PetaniActivity.this);


                alertDialogBuilder.setTitle("Hapus Akun");


                alertDialogBuilder
                        .setMessage("Akun akan diapus ?")

                        .setCancelable(false)
                        .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                hapusAkun(no_hpuser);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                ;


                AlertDialog alertDialog = alertDialogBuilder.create();


                alertDialog.show();
            }
        });
        tvubahprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PetaniActivity.this, PetaniUbahProfile.class);
                intent.putExtra("no_telp",no_hpuser);
                startActivity(intent);
            }
        });
        getdatauser(no_hpuser);
    }
    private void munculDialog(String judul,String pesan){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);


        alertDialogBuilder.setTitle(judul);


        alertDialogBuilder
                .setMessage(pesan)

                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        dialog.cancel();
                    }
                })
                ;


        AlertDialog alertDialog = alertDialogBuilder.create();


        alertDialog.show();
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


                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    nama= ds.child("nama").getValue().toString();
                    alamat= ds.child("alamat").getValue().toString();
                    nohp= ds.child("no_telp").getValue().toString();
                    pengalaman= ds.child("pengamalan").getValue().toString();
                    spesialis= ds.child("spesialis").getValue().toString();
                    umur= ds.child("umur").getValue().toString();
                    foto= ds.child("foto").getValue().toString();
                }
                if (foto.equals("")){
                   Intent intent=new Intent(PetaniActivity.this,LoginActivity.class);
                   startActivity(intent);
                }else {
                    tvnama.setText(nama);
                    tvalamat.setText(alamat);
                    tvnohp.setText(nohp);
                    tvpengalaman.setText(pengalaman);
                    tvspesialis.setText(spesialis);
                    tvumur.setText(umur);
                    Picasso.get().load(foto)
                            .placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background)
                            .into(fotopetani);
                    savePreferences();
                }




            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        };


        queryFirebase.addValueEventListener(valueEventListener);
    }
    public void hapusAkun(String no_hp) {
           if(rootref!=null){            rootref.child("petani").child(no_hp).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(PetaniActivity.this,"Akun di hapus", Toast.LENGTH_LONG).show();
                logoutSave();
                finish();
            }
        });

        }
    }
    private void logoutSave() {
        SharedPreferences settings = getSharedPreferences("loginsaya",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();



        editor.putString("no_hp", "");
        editor.putString("sebagai","");

        editor.commit();
        Intent intent = new Intent(PetaniActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void savePreferences() {
        SharedPreferences settings = getSharedPreferences("loginsaya",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();



        editor.putString("no_hp", no_hpuser);
        editor.putString("sebagai", "petani");

        editor.commit();

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();



            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    protected void exitByBackKey() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                PetaniActivity.this);


        alertDialogBuilder.setTitle("Keluar");


        alertDialogBuilder
                .setMessage("Keluar ?")

                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        finish();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
        ;


        AlertDialog alertDialog = alertDialogBuilder.create();


        alertDialog.show();

    }
    private void showAlertDialog(int layout){
        dialogBuilder = new AlertDialog.Builder(PetaniActivity.this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        Button dialogButton = layoutView.findViewById(R.id.btnDialog);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
}
