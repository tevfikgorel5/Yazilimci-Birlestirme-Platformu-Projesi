package com.example.devsozluk;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;

@Entity
@Table(name = "cevap")
public class Cevap {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int id;
    @Column(name = "metin", nullable = false, columnDefinition = "TEXT") private String metin;
    @Column(name = "yazilma_tarihi") @Temporal(TemporalType.TIMESTAMP) private Date yazilmaTarihi = new Date();
    
    // BURADAKİ DÖNGÜYÜ DE KIRIYORUZ
    @JsonIgnore
    @ManyToOne @JoinColumn(name = "baslik_id", nullable = false) private Baslik baslik;
    
    @ManyToOne @JoinColumn(name = "kullanici_id", nullable = false) private Kullanici yazar;

    public int getId() { return id; } public void setId(int id) { this.id = id; }
    public String getMetin() { return metin; } public void setMetin(String metin) { this.metin = metin; }
    public Date getYazilmaTarihi() { return yazilmaTarihi; } public void setYazilmaTarihi(Date yazilmaTarihi) { this.yazilmaTarihi = yazilmaTarihi; }
    public Baslik getBaslik() { return baslik; } public void setBaslik(Baslik baslik) { this.baslik = baslik; }
    public Kullanici getYazar() { return yazar; } public void setYazar(Kullanici yazar) { this.yazar = yazar; }
}