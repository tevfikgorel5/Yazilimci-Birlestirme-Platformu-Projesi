package com.example.devsozluk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/baslik")
public class BaslikController {
    
    @Autowired private BaslikRepository baslikRepository;
    @Autowired private KullaniciRepository kullaniciRepository;

    @PostMapping("/olustur")
    public ResponseEntity<String> baslikAc(@RequestParam int kullaniciId, @RequestBody Baslik baslik) {
        Optional<Kullanici> yazarOpt = kullaniciRepository.findById(kullaniciId);
        if (yazarOpt.isPresent()) {
            baslik.setYazar(yazarOpt.get());
            baslikRepository.save(baslik);
            return ResponseEntity.ok("Başlık başarıyla oluşturuldu.");
        }
        return ResponseEntity.status(401).body("Yetkisiz işlem!");
    }

    // GÜNCELLEDİĞİMİZ KISIM BURASI
    @GetMapping("/ara")
    public ResponseEntity<List<Baslik>> baslikAra(@RequestParam(defaultValue = "") String kelime) {
        // Eğer arama kelimesi boşsa (sayfa ilk yüklendiğinde), tüm başlıkları getir
        if (kelime.trim().isEmpty()) {
            return ResponseEntity.ok(baslikRepository.findAll());
        }
        // Eğer bir kelime yazıldıysa, o kelimeyi içerenleri getir
        return ResponseEntity.ok(baslikRepository.findByAdContainingIgnoreCase(kelime));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> baslikGetir(@PathVariable int id) {
        Optional<Baslik> baslikOpt = baslikRepository.findById(id);
        if (baslikOpt.isPresent()) return ResponseEntity.ok(baslikOpt.get());
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/sil/{baslikId}")
    public ResponseEntity<String> baslikSil(@PathVariable int baslikId, @RequestParam int adminId) {
        Optional<Kullanici> adminOpt = kullaniciRepository.findById(adminId);
        if (adminOpt.isPresent() && "ADMIN".equals(adminOpt.get().getRol())) {
            if (baslikRepository.existsById(baslikId)) {
                baslikRepository.deleteById(baslikId); 
                return ResponseEntity.ok("Başlık silindi.");
            }
            return ResponseEntity.status(404).body("Başlık bulunamadı.");
        }
        return ResponseEntity.status(403).body("Admin yetkiniz yok!");
    }
}