package com.pramudya.e_tani;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    Button btnpemilik,btnpetani,btndaftar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btndaftar=(Button)findViewById(R.id.btndaftar);
        btnpetani=(Button)findViewById(R.id.btnpetani);
        btnpemilik=(Button)findViewById(R.id.btnpemilik);

        btndaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,DaftarActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnpetani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,VerifikasiPetaniActivity.class);
                startActivity(intent);

            }
        });
        btnpemilik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,VerifikasiPesawahActivity.class);
                startActivity(intent);

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
                LoginActivity.this);


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
