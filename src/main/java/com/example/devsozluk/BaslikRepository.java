package com.example.devsozluk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BaslikRepository extends JpaRepository<Baslik, Integer> {
    List<Baslik> findByAdContainingIgnoreCase(String kelime);
}