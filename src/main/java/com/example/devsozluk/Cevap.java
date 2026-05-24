package com.example.devsozluk;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;

@Entity
@Table(name = "cevap")
public class Cevap {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int id;
    
    @Column(name = "metin", nullable = false, columnDefinition = "TEXT") 
    private String metin;
    
    @Column(name = "yazilma_tarihi") 
    @Temporal(TemporalType.TIMESTAMP) 
    private Date yazilmaTarihi = new Date();
    
    // YENİ EKLENEN KISIM: Beğeni Sayısı (Varsayılan olarak 0'dan başlar)
    @Column(name = "begeni_sayisi", nullable = false)
    private int begeniSayisi = 0;
    
    @JsonIgnore
    @ManyToOne 
    @JoinColumn(name = "baslik_id", nullable = false) 
    private Baslik baslik;
    
    @ManyToOne 
    @JoinColumn(name = "kullanici_id", nullable = false) 
    private Kullanici yazar;

    // Getter ve Setter'lar
    public int getId() { return id; } 
    public void setId(int id) { this.id = id; }
    
    public String getMetin() { return metin; } 
    public void setMetin(String metin) { this.metin = metin; }
    
    public Date getYazilmaTarihi() { return yazilmaTarihi; } 
    public void setYazilmaTarihi(Date yazilmaTarihi) { this.yazilmaTarihi = yazilmaTarihi; }
    
    // YENİ EKLENEN GETTER VE SETTER: begeniSayisi için
    public int getBegeniSayisi() { return begeniSayisi; }
    public void setBegeniSayisi(int begeniSayisi) { this.begeniSayisi = begeniSayisi; }
    
    public Baslik getBaslik() { return baslik; } 
    public void setBaslik(Baslik baslik) { this.baslik = baslik; }
    
    public Kullanici getYazar() { return yazar; } 
    public void setYazar(Kullanici yazar) { this.yazar = yazar; }
}