package com.pramudya.e_tani.showdata;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



import com.pramudya.e_tani.petani.PetaniParseModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String DBLOCATION = null;
    public static final String DBNAME = "haversine.sqlite";
    private Context mContext;
    public SQLiteDatabase mDatabase;

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 2);
        this.mContext = context;
        StringBuilder sb = new StringBuilder();
        sb.append("/data/data/");
        sb.append(this.mContext.getPackageName());
        sb.append("/databases/");
        DBLOCATION = sb.toString();
    }

    public void openDatabase() {
        String path = this.mContext.getDatabasePath(DBNAME).getPath();
        if (this.mDatabase == null || !this.mDatabase.isOpen()) {
            this.mDatabase = SQLiteDatabase.openDatabase(path, null, 0);
        }
    }

    public void closeDatabase() {
        if (this.mDatabase != null) {
            this.mDatabase.close();
        }
    }




    public List<PetaniParseModel> listPetani(){
        openDatabase();

        SQLiteDatabase sQLiteDatabase = this.mDatabase;

        List<PetaniParseModel>  listPt = new ArrayList<>();
        String selectQuery = "SELECT  * FROM tbl_petani GROUP BY no_telp ORDER BY jarak ASC";
        Cursor cursor = sQLiteDatabase.rawQuery(selectQuery, null);
        while (cursor.moveToNext()){
            PetaniParseModel pt = new PetaniParseModel();
            pt.setJarak(cursor.getString(cursor.getColumnIndex("jarak")));
            pt.setUmur(cursor.getString(cursor.getColumnIndex("umur")));
            pt.setNama(cursor.getString(cursor.getColumnIndex("nama")));
            pt.setNo_telp(cursor.getString(cursor.getColumnIndex("no_telp")));
            pt.setSpesialis(cursor.getString(cursor.getColumnIndex("spesialis")));
            pt.setFoto(cursor.getString(cursor.getColumnIndex("foto")));


            listPt.add(pt);
        }
        return  listPt;
    }
    public void masukanData(String snama,String sno_telp,String sspesialis,String sjarak,String sfoto,String sumur) {

        openDatabase();
        SQLiteDatabase sQLiteDatabase = this.mDatabase;

        String ROW1 = "INSERT INTO tbl_petani (nama,no_telp,spesialis,jarak,foto,umur) Values ('"+snama+"', '"+sno_telp+"', '"+sspesialis+"', '"+sjarak+"', '"+sfoto+"', '"+sumur+"')";

        Cursor rawQuery = sQLiteDatabase.rawQuery(ROW1, null);
        if (rawQuery.moveToFirst()) {

        }
        rawQuery.close();
        closeDatabase();

    }


}
