package com.pramudya.e_tani.petani;

import android.annotation.SuppressLint;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.pramudya.e_tani.DetailPetani;
import com.pramudya.e_tani.R;
import com.pramudya.e_tani.showdata.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import androidx.cardview.widget.CardView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAdapter extends BaseAdapter {
    Context context;
    ArrayList<PetaniModel> petaniModel;
    Double latitude,longitude;
    final int radius = 6371;
    DatabaseHelper mDBHelper;
    public MyAdapter(Context context, ArrayList<PetaniModel> petaniModel,Double latitude,Double longitude) {
        this.context = context;
        this.petaniModel = petaniModel;
        this.latitude=latitude;
        this.longitude=longitude;
        this.mDBHelper = new DatabaseHelper(context);
        context.getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);
        this.mDBHelper.getReadableDatabase();
        this.mDBHelper.close();

        loadPreferences();

    }

    public int getCount() {
        // TODO Auto-generated method stub
        return petaniModel.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
    @SuppressLint("NewApi")
    public View getView(final int pos, View child, final ViewGroup parent) {
        PetaniModel modelPt = petaniModel.get(pos);
        Double lat1 = Double.parseDouble(modelPt.getLatitude());
        Double lon1 = Double.parseDouble(modelPt.getLongitude());
        Double latDistance = toRad(latitude-lat1);
        Double lonDistance = toRad(longitude-lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRad(lat1)) * Math.cos(toRad(latitude)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        final Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        Double distance = radius * c;
        modelPt.setJarak(String.valueOf(round(distance,2)));
        MyAdapter.Holder mHolder;
        LayoutInflater layoutInflater;
        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.item_petani, null);
            mHolder = new MyAdapter.Holder();
            mHolder.cvklick=child.findViewById(R.id.cvklick);
            mHolder.tvnama = child.findViewById(R.id.tvnama);
            mHolder.tvumur = child.findViewById(R.id.tvumur);
            mHolder.tvjarak = child.findViewById(R.id.tvjarak);

            mHolder.tvspesialis = child.findViewById(R.id.tvspesialis);
            mHolder.imgp = child.findViewById(R.id.imgp);


            child.setTag(mHolder);

        } else {
            mHolder = (MyAdapter.Holder) child.getTag();
        }

        mHolder.tvnama.setText(String.valueOf(modelPt.getNama()));
        mHolder.tvumur.setText(String.valueOf(modelPt.getUmur()));
        mHolder.tvspesialis.setText(String.valueOf(modelPt.getSpesialis()));
        mHolder.tvjarak.setText(String.valueOf(modelPt.getJarak()));
        Picasso.get().load(String.valueOf(modelPt.getFoto()))
                .placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background)
                .into(mHolder.imgp);
        final String no_hp=modelPt.getNo_telp();
        mHolder.cvklick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DetailPetani.class);
                intent.putExtra("no_telp",no_hp);
                context.startActivity(intent);

            }
        });
        mDBHelper.masukanData(modelPt.getNama(),modelPt.getNo_telp(),modelPt.getSpesialis(),String.valueOf(round(distance,2)),modelPt.getFoto(),modelPt.getUmur());



        return child;
    }


    public class Holder {

        public TextView tvnama;
        public TextView tvumur;
        public TextView tvspesialis;
        public TextView tvjarak;
        public CircleImageView imgp;
        public CardView cvklick;


    }


    public double toRad(Double value){
        return value * Math.PI / 180;
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    private void loadPreferences() {

        SharedPreferences settings = context.getSharedPreferences("DATALEVEL",
                Context.MODE_PRIVATE);


        String ya = settings.getString("level", "");
        if (ya.equals("ya")){

        }else {
            copyDatabase(context);
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
    private void simpanNama(String ya) {
        SharedPreferences settings = context.getSharedPreferences("DATALEVEL",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();




        editor.putString("level", ya);


        editor.apply();
    }

}
