package com.pramudya.e_tani;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle incicle){
        super.onCreate(incicle);
        setContentView(R.layout.tes);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = null;
                mainIntent = new Intent(Splash.this,IntroActivity.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, 2000);
    }
}