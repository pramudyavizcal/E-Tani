package com.pramudya.e_tani.pesawah;

import java.io.Serializable;

public class Pesawah implements Serializable {
    private String nama;
    private String umur;
    private String luas_sawah;
    private String alamat;
    private String no_telp;
    private String foto;

    public Pesawah(){

    }
    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUmur() {
        return umur;
    }

    public void setUmur(String umur) {
        this.umur = umur;
    }

    public String getLuas_sawah() {
        return luas_sawah;
    }

    public void setLuas_sawah(String luas_sawah) {
        this.luas_sawah = luas_sawah;
    }



    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNo_telp() {
        return no_telp;
    }

    public void setNo_telp(String no_telp) {
        this.no_telp = no_telp;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }



    @Override
    public String toString() {
        return " "+nama+"\n" +
                " "+umur +"\n" +
                " "+ luas_sawah +"\n" +

                " "+alamat+"\n" +
                " "+no_telp+"\n" +
                " "+foto;

    }

    public Pesawah(String snama, String sumur, String sluas_sawah, String salamat, String sno_telp, String sfoto){
        nama = snama;
        umur = sumur;
        luas_sawah = sluas_sawah;

        alamat = salamat;
        no_telp = sno_telp;
        foto = sfoto;

    }
}
