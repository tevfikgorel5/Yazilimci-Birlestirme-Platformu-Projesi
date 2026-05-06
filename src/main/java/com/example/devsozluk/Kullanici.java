package com.example.devsozluk;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "kullanici")
public class Kullanici {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
    @Column(name = "kullanici_adi", unique = true, nullable = false, length = 50) private String kullaniciAdi;
    @Column(name = "sifre_hash", nullable = false) private String sifreHash;
    @Column(name = "kayit_tarihi") @Temporal(TemporalType.TIMESTAMP) private Date kayitTarihi = new Date();
    @Column(name = "rol", length = 20) private String rol = "STANDART";

    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public String getKullaniciAdi() { return kullaniciAdi; } public void setKullaniciAdi(String kullaniciAdi) { this.kullaniciAdi = kullaniciAdi; }
    public String getSifreHash() { return sifreHash; } public void setSifreHash(String sifreHash) { this.sifreHash = sifreHash; }
    public Date getKayitTarihi() { return kayitTarihi; } public void setKayitTarihi(Date kayitTarihi) { this.kayitTarihi = kayitTarihi; }
    public String getRol() { return rol; } public void setRol(String rol) { this.rol = rol; }
}