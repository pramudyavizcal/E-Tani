package com.pramudya.e_tani;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DaftarActivity extends AppCompatActivity {

    Button btnpemilik,btnpetani,btnmasuk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);
        btnpetani=(Button)findViewById(R.id.btnpetani);
        btnpemilik=(Button)findViewById(R.id.btnpemilik);
        btnmasuk=(Button)findViewById(R.id.btnmasuk);
        btnpemilik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DaftarActivity.this,VerifikasiPesawahActivity.class);
                startActivity(intent);

            }
        });
        btnpetani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DaftarActivity.this,VerifikasiPetaniActivity.class);
                startActivity(intent);

            }
        });
        btnmasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DaftarActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

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
                DaftarActivity.this);


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
}
