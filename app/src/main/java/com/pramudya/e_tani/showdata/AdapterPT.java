package com.pramudya.e_tani.showdata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.pramudya.e_tani.DetailPetani;
import com.pramudya.e_tani.R;
import com.pramudya.e_tani.petani.PetaniParseModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.cardview.widget.CardView;
import de.hdodenhof.circleimageview.CircleImageView;


public class AdapterPT extends BaseAdapter {

    private Context context;
    private List<PetaniParseModel> ptList;


    public AdapterPT(Context context, List<PetaniParseModel> notesList) {
        this.context = context;
        this.ptList = notesList;
    }


    public int getCount() {
        // TODO Auto-generated method stub
        return ptList.size();
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
        PetaniParseModel modelPt = ptList.get(pos);
        Holder mHolder;
        LayoutInflater layoutInflater;
        if (child == null) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.item_petani, null);
            mHolder = new Holder();
            mHolder.cvklick=child.findViewById(R.id.cvklick);
            mHolder.tvnama = child.findViewById(R.id.tvnama);
            mHolder.tvumur = child.findViewById(R.id.tvumur);
            mHolder.tvjarak = child.findViewById(R.id.tvjarak);

            mHolder.tvspesialis = child.findViewById(R.id.tvspesialis);
            mHolder.imgp = child.findViewById(R.id.imgp);


            child.setTag(mHolder);

        } else {
            mHolder = (Holder) child.getTag();
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

}
