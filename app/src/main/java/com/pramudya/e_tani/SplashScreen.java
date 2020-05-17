package com.pramudya.e_tani;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGHT = 6000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION

                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {

                final LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE );

                assert manager != null;
                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) )
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                else
                    loadPreferences();
               }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

    }
    private void loadPreferences() {

        SharedPreferences settings = getSharedPreferences("loginsaya",
                Context.MODE_PRIVATE);


        String no_hp = settings.getString("no_hp", "");
        String sebagai = settings.getString("sebagai", "");
        if (sebagai.equals("petani")){
            Intent intent=new Intent(SplashScreen.this, PetaniActivity.class);
            intent.putExtra("no_telp",no_hp);
            startActivity(intent);
            finish();
        }else if (sebagai.equals("pesawah")){
            Intent intent=new Intent(SplashScreen.this, PesawahActivity.class);
            intent.putExtra("no_telp",no_hp);
            startActivity(intent);
            finish();
        }else {
            Intent intent=new Intent(SplashScreen.this, LoginActivity.class);

            startActivity(intent);
            finish();
        }




    }

    @Override
    protected void onResume() {

        super.onResume();
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION

                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {

                final LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE );

                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) )
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                else
                    loadPreferences();
               }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
    }
}
