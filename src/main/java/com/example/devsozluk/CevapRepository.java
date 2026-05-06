package com.example.devsozluk;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CevapRepository extends JpaRepository<Cevap, Integer> {
    // Liste yerine sayfalama (Page) objesi döndüren yeni metodumuz
    Page<Cevap> findByBaslikId(int baslikId, Pageable pageable);
}