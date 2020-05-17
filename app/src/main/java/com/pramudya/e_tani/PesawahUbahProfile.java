package com.pramudya.e_tani;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.pramudya.e_tani.pdModel.pdModel;
import com.pramudya.e_tani.pesawah.Pesawah;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class PesawahUbahProfile extends AppCompatActivity {
    private Button btnambilfoto, btndaftar;
    private ImageView imgfoto;

    private Uri filePath;
    private DatabaseReference database;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage storage;
    StorageReference storageReference;
    EditText edtnotelp,edtnama,edtumur,edtalamat;
    String no_hpuser="";
    Spinner spluassawah;
    String updatefoto="tidak";
    String urlfoto="";
    Query quesr;
    ValueEventListener valueEventListener;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pesawah_ubah_profil);
        database = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        spluassawah = (Spinner) findViewById(R.id.spluassawah);

        edtnotelp = (EditText) findViewById(R.id.edtnotelp);
        edtnama = (EditText) findViewById(R.id.edtnama);
        edtumur = (EditText) findViewById(R.id.edtumur);
        edtalamat = (EditText) findViewById(R.id.edtalamat);

        btnambilfoto = (Button) findViewById(R.id.btnambilfoto);
        btndaftar = (Button) findViewById(R.id.btndaftar);
        imgfoto = (ImageView) findViewById(R.id.imgfoto);
        final Intent intent =getIntent();
        edtnotelp.setText(intent.getStringExtra("no_telp"));
        no_hpuser=intent.getStringExtra("no_telp");
        btnambilfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity(filePath)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(PesawahUbahProfile.this);
            }
        });
        btndaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Pesawah pesawah = new Pesawah();
                pesawah.setNama(edtnama.getText().toString());
                pesawah.setAlamat(edtalamat.getText().toString());

                pesawah.setLuas_sawah(spluassawah.getSelectedItem().toString());

                pesawah.setUmur(edtumur.getText().toString());

                pesawah.setNo_telp(edtnotelp.getText().toString());
                if (updatefoto.equals("ya")){
                    kirimDaftar(edtnotelp.getText().toString());
                }else {
                    pesawah.setFoto(urlfoto);
                    updateProfile(pesawah,edtnotelp.getText().toString());
                }
            }


        });

        spluassawah();
        getdatauser(no_hpuser);
    }
    private void spluassawah() {
        List<String> kota_asal = new ArrayList<String>();
        kota_asal.add("1000m2");
        kota_asal.add("2000m2");


        ArrayAdapter<String> dataAdapterkota_asal = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, kota_asal);
        dataAdapterkota_asal.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spluassawah.setAdapter(dataAdapterkota_asal);
    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == REQUEST_CAPTURE_IMAGE &&
                resultCode == RESULT_OK) {

            if (data != null && data.getExtras() != null) {
                filePath = data.getData();
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                imgfoto.setImageBitmap(imageBitmap);

            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                imgfoto.setImageURI(resultUri);
                filePath = resultUri;
                updatefoto="ya";

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void kirimDaftar(String no_hp) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Loading.....");
            progressDialog.show();

            StorageReference ref = storageReference.child("gambar_pesawah/").child(no_hp);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(PesawahUbahProfile.this, "Memulai pendaftaran", Toast.LENGTH_SHORT).show();
                            updateProfile(new Pesawah(edtnama.getText().toString(),edtumur.getText().toString(),spluassawah.getSelectedItem().toString(),edtalamat.getText().toString(),edtnotelp.getText().toString(),taskSnapshot.getDownloadUrl().toString()),edtnotelp.getText().toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(PesawahUbahProfile.this, "Gagal pendaftaran "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.setMessage("Silakan tunggu...");
                        }
                    });
        }
    }
    private void updateProfile(Pesawah pesawah, String no_telp) {

        database.child("pesawah")
                .child(no_telp)
                .setValue(pesawah)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();

                    }
                });


    }
    public void getdatauser(String no_telp){
        pdModel.pdData(PesawahUbahProfile.this);
        quesr = database.child("pesawah").orderByChild("no_telp").equalTo(no_telp);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String nama = "";
                String alamat = "";
                String nohp = "";
                String luas_sawah = "";

                String umur = "";
                String foto = "";


                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    nama= ds.child("nama").getValue().toString();
                    alamat= ds.child("alamat").getValue().toString();
                    nohp= ds.child("no_telp").getValue().toString();

                    luas_sawah= ds.child("luas_sawah").getValue().toString();
                    umur= ds.child("umur").getValue().toString();
                    foto= ds.child("foto").getValue().toString();
                }
                edtnama.setText(nama);
                edtalamat.setText(alamat);
                edtnotelp.setText(nohp);

                edtumur.setText(umur);
                urlfoto=foto;
                Picasso.get().load(foto)
                        .placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background)
                        .into(imgfoto);
            pdModel.hideProgressDialog();



            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        };


        quesr.addValueEventListener(valueEventListener);
    }
}
