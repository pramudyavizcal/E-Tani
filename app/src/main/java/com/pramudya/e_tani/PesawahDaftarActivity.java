package com.pramudya.e_tani;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.pramudya.e_tani.pesawah.Pesawah;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class PesawahDaftarActivity extends AppCompatActivity {
    private Button btnambilfoto, btndaftar;
    private ImageView imgfoto;

    private Uri filePath;
    private DatabaseReference database;

    FirebaseStorage storage;
    StorageReference storageReference;
    EditText edtnotelp,edtnama,edtumur,edtalamat;

    Spinner spluassawah;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pendaftaran_pemilik);
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
        btnambilfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity(filePath)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(PesawahDaftarActivity.this);
            }
        });
        btndaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kirimDaftar(edtnotelp.getText().toString());

            }
        });

        spluassawah();

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

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void kirimDaftar(String no_hp) {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Silakan Tunggu.....");
            progressDialog.show();

            StorageReference ref = storageReference.child("gambar_pesawah/").child(no_hp);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(PesawahDaftarActivity.this, "Memulai pendaftaran", Toast.LENGTH_SHORT).show();
                            submitBarang(new Pesawah(edtnama.getText().toString(),edtumur.getText().toString(),spluassawah.getSelectedItem().toString(),edtalamat.getText().toString(),edtnotelp.getText().toString(),taskSnapshot.getDownloadUrl().toString()),edtnotelp.getText().toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(PesawahDaftarActivity.this, "Gagal pendaftaran "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.setMessage("Mencari data petani...");
                        }
                    });
        }
    }
    private void submitBarang(Pesawah pesawah,String no_hp) {

        database.child("pesawah").child(no_hp).setValue(pesawah).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Snackbar.make(findViewById(R.id.btndaftar), "Data berhasil ditambahkan", Snackbar.LENGTH_LONG).show();
                savePreferences();
            }
        });
    }
    private void savePreferences() {
        SharedPreferences settings = getSharedPreferences("loginsaya",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("no_hp", edtnotelp.getText().toString());
        editor.putString("sebagai", "pesawah");
        editor.commit();
        Intent intent =new Intent(getApplicationContext(),SplashScreen.class);
        startActivity(intent);
        finish();
    }

}