package com.example.devsozluk;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CevapRepository extends JpaRepository<Cevap, Integer> {
    
    // Entry'leri başlığa göre bul ve beğeni sayısına (begeniSayisi) göre en yüksekten en düşüğe (Desc) sırala
    List<Cevap> findByBaslikIdOrderByBegeniSayisiDesc(int baslikId);
    
}