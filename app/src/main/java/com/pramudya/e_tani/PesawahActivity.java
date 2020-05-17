package com.pramudya.e_tani;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.pramudya.e_tani.pdModel.pdModel;
import com.pramudya.e_tani.petani.MyAdapter;
import com.pramudya.e_tani.petani.PetaniModel;
import com.pramudya.e_tani.petani.PetaniParseModel;
import com.pramudya.e_tani.showdata.AdapterPT;
import com.pramudya.e_tani.showdata.DatabaseHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


public class PesawahActivity extends AppCompatActivity {
    TextView tvnama,tvalamat,tvnohp,tvumur,tvpengalaman,tvspesialis,tvubahprofile,tvtentang,tvhubkami,tvlogout,tvhapuskaun;
    LinearLayout lvhome,lvprofile;
    DatabaseReference rootref;
    DatabaseReference rootref2;
    String no_hpuser="";

    double latitude;
    double longitude ;

    private ListView mRecycler;
    private LinearLayoutManager mManager;
    ArrayList<PetaniModel> list;

    MyAdapter adapter;

    DatabaseHelper mDBHelper;
    AdapterPT adapter2;
    ListView lv;
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;

    private List<PetaniParseModel> listPT = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_pesawah);
        this.mDBHelper = new DatabaseHelper(this);
        getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);
        this.mDBHelper.getReadableDatabase();
        this.mDBHelper.close();

        loadPreferences();
        lv = (ListView) findViewById(R.id.listviewku);
        listPT.addAll(mDBHelper.listPetani());
        adapter2 = new AdapterPT(PesawahActivity.this, listPT);
        lv.setAdapter(adapter2);

        Intent intent = getIntent();
        no_hpuser = intent.getStringExtra("no_telp");
        pdModel.pdData(PesawahActivity.this);
        GPSTracker gps;


        gps = new GPSTracker(PesawahActivity.this);
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();
        rootref = FirebaseDatabase.getInstance().getReference();


        mRecycler = findViewById(R.id.listpetani);
        showAlertDialog(R.layout.dialog_pemilik_sukses);
        rootref2 = FirebaseDatabase.getInstance().getReference();
        rootref = FirebaseDatabase.getInstance().getReference().child("petani");
        rootref.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<PetaniModel>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    PetaniModel p = dataSnapshot1.getValue(PetaniModel.class);
                    list.add(p);

                }


                adapter = new MyAdapter(PesawahActivity.this, list, latitude, longitude);

                mRecycler.setAdapter(adapter);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        pdModel.hideProgressDialog();

                        mRecycler.setVisibility(View.GONE);
                        if (adapter2.getCount() == 0) {
                            Intent intent1 = new Intent(PesawahActivity.this, PesawahActivity.class);
                            intent1.putExtra("no_telp", no_hpuser);
                            startActivity(intent1);
                        }

                    }
                }, 10000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(PesawahActivity.this, "Database tidak ada", Toast.LENGTH_SHORT).show();
                pdModel.hideProgressDialog();
            }

        });


        tvubahprofile = (TextView) findViewById(R.id.tvubahprofile);
        tvtentang = (TextView) findViewById(R.id.tvtentang);
        tvhubkami = (TextView) findViewById(R.id.tvhubkami);
        tvlogout = (TextView) findViewById(R.id.tvlogout);
        tvhapuskaun = (TextView) findViewById(R.id.tvhapusakun);
        lvhome = (LinearLayout) findViewById(R.id.lvhome);
        lvprofile = (LinearLayout) findViewById(R.id.lvprofile);
        lvhome.setVisibility(View.VISIBLE);
        lvprofile.setVisibility(View.GONE);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        lvhome.setVisibility(View.VISIBLE);
                        lvprofile.setVisibility(View.GONE);
                        break;
                    case R.id.navigation_profile:
                        lvhome.setVisibility(View.GONE);
                        lvprofile.setVisibility(View.VISIBLE);
                        break;

                }
                return false;
            }
        });

        tvtentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                munculDialog("Tentang", "Aplikasi e tani adalah .....");
            }
        });
        tvhubkami.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                munculDialog("Hubungi Kami", "082302039545");
            }
        });
        tvlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        PesawahActivity.this);


                alertDialogBuilder.setTitle("Logout");


                alertDialogBuilder
                        .setMessage("Logout App ?")

                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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
                        PesawahActivity.this);


                alertDialogBuilder.setTitle("Hapus Akun");


                alertDialogBuilder
                        .setMessage("Akun akan diapus ?")

                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

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
                Intent intent = new Intent(PesawahActivity.this, PesawahUbahProfile.class);
                intent.putExtra("no_telp", no_hpuser);
                startActivity(intent);
            }
        });

        savePreferences();

    }



    private void munculDialog(String judul, String pesan){
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


    public void hapusAkun(String no_hp) {
        if(rootref2!=null){            rootref2.child("pesawah").child(no_hp).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(PesawahActivity.this,"Akun di hapus", Toast.LENGTH_LONG).show();
                logoutSave();
                finish();
            }
        });

        }
    }
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
    private void logoutSave() {
        SharedPreferences settings = getSharedPreferences("loginsaya",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();



        editor.putString("no_hp", "");
        editor.putString("sebagai","");

        editor.apply();
        Intent intent = new Intent(PesawahActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void savePreferences() {
        SharedPreferences settings = getSharedPreferences("loginsaya",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();



        editor.putString("no_hp", no_hpuser);
        editor.putString("sebagai", "pesawah");

        editor.apply();

    }
    private void simpanNama(String ya) {
        SharedPreferences settings = PesawahActivity.this.getSharedPreferences("DATALEVEL",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();




        editor.putString("level", ya);


        editor.apply();
    }
    private void loadPreferences() {

        SharedPreferences settings = PesawahActivity.this.getSharedPreferences("DATALEVEL",
                Context.MODE_PRIVATE);


        String ya = settings.getString("level", "");
        if (ya.equals("ya")){

        }else {
            copyDatabase(PesawahActivity.this);
        }

    }
    private boolean copyDatabase(Context context) {
        simpanNama("ya");
        try {
            InputStream open = context.getAssets().open(DatabaseHelper.DBNAME);
            StringBuilder sb = new StringBuilder();
            sb.append(DatabaseHelper.DBLOCATION);
            sb.append(DatabaseHelper.DBNAME);
            FileOutputStream fileOutputStream = new FileOutputStream(sb.toString());
            byte[] bArr = new byte[1024];
            while (true) {
                int read = open.read(bArr);
                if (read > 0) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileOutputStream.flush();
                    fileOutputStream.close();

                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
                PesawahActivity.this);


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
        dialogBuilder = new AlertDialog.Builder(PesawahActivity.this);
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
