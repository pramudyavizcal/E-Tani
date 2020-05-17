package com.pramudya.e_tani.petani;

import java.io.Serializable;

public class Petani implements Serializable {
    private String nama;
    private String umur;
    private String pengamalan;
    private String spesialis;
    private String alamat;
    private String no_telp;
    private String foto;
    private String latitude;
    private String longitude;
    private String key;
public Petani(){

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

    public String getPengamalan() {
        return pengamalan;
    }

    public void setPengamalan(String pengamalan) {
        this.pengamalan = pengamalan;
    }

    public String getSpesialis() {
        return spesialis;
    }

    public void setSpesialis(String spesialis) {
        this.spesialis = spesialis;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return " "+nama+"\n" +
                " "+umur +"\n" +
                " "+pengamalan+"\n" +
                " "+spesialis+"\n" +
                " "+alamat+"\n" +
                " "+no_telp+"\n" +
                " "+foto+"\n" +
                " "+latitude+"\n" +
                " "+longitude;
    }

    public Petani(String snama, String sumur, String spengamalan, String sspesialis, String salamat, String sno_telp, String sfoto, String slatitude, String slongitude){
        nama = snama;
        umur = sumur;
        pengamalan = spengamalan;
        spesialis = sspesialis;
        alamat = salamat;
        no_telp = sno_telp;
        foto = sfoto;
        latitude = slatitude;
        longitude = slongitude;
    }
}
