package edu.khiemnhg.TongHopGK.models;

public class SinhVien {
    String mssv;
    String hoTen;
    float diemTB;

    public SinhVien(String mssv, String hoTen, float diemTB) {
        this.mssv = mssv;
        this.hoTen = hoTen;
        this.diemTB = diemTB;
    }

    // Getter
    public String getMssv() {
        return mssv;
    }

    public String getHoTen() {
        return hoTen;
    }

    public float getDiemTB() {
        return diemTB;
    }
}