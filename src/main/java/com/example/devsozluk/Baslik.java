package com.example.devsozluk;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "baslik")
public class Baslik {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
    @Column(name = "ad", nullable = false, length = 255) private String ad;
    @Column(name = "olusturma_tarihi") @Temporal(TemporalType.TIMESTAMP) private Date olusturmaTarihi = new Date();
    @ManyToOne @JoinColumn(name = "kullanici_id", nullable = false) private Kullanici yazar;
    
    // SONSUZ DÖNGÜYÜ KIRAN SİHİRLİ ANOTASYON: @JsonIgnore
    @JsonIgnore
    @OneToMany(mappedBy = "baslik", cascade = CascadeType.ALL, orphanRemoval = true) private List<Cevap> cevaplar;

    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public String getAd() { return ad; } public void setAd(String ad) { this.ad = ad; }
    public Date getOlusturmaTarihi() { return olusturmaTarihi; } public void setOlusturmaTarihi(Date olusturmaTarihi) { this.olusturmaTarihi = olusturmaTarihi; }
    public Kullanici getYazar() { return yazar; } public void setYazar(Kullanici yazar) { this.yazar = yazar; }
    public List<Cevap> getCevaplar() { return cevaplar; } public void setCevaplar(List<Cevap> cevaplar) { this.cevaplar = cevaplar; }
}