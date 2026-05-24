package com.example.devsozluk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cevap")
public class CevapController {

    @Autowired private CevapRepository cevapRepository;
    @Autowired private BaslikRepository baslikRepository;
    @Autowired private KullaniciRepository kullaniciRepository;

    @PostMapping("/yaz")
    public ResponseEntity<String> cevapYaz(@RequestParam int baslikId, @RequestParam int kullaniciId, @RequestBody Cevap cevap) {
        Optional<Baslik> baslikOpt = baslikRepository.findById(baslikId);
        Optional<Kullanici> yazarOpt = kullaniciRepository.findById(kullaniciId);

        if (baslikOpt.isPresent() && yazarOpt.isPresent()) {
            if(cevap.getMetin() == null || cevap.getMetin().trim().isEmpty()) return ResponseEntity.badRequest().body("Metin boş olamaz!");
            cevap.setBaslik(baslikOpt.get());
            cevap.setYazar(yazarOpt.get());
            cevapRepository.save(cevap);
            return ResponseEntity.ok("Entry eklendi.");
        }
        return ResponseEntity.badRequest().body("Başlık veya Kullanıcı bulunamadı!");
    }

    // GÜNCELLEME: Repository'e az önce eklediğimiz "Beğeniye Göre Sırala" metodunu List olarak çağırıyoruz
    @GetMapping("/baslik/{baslikId}")
    public ResponseEntity<List<Cevap>> cevaplariGetir(@PathVariable int baslikId) {
        return ResponseEntity.ok(cevapRepository.findByBaslikIdOrderByBegeniSayisiDesc(baslikId));
    }

    // YENİ EKLENEN METOT: Beğeni artırma işlemi
    @PostMapping("/begen/{cevapId}")
    public ResponseEntity<String> cevapBegen(@PathVariable int cevapId) {
        Optional<Cevap> cevapOpt = cevapRepository.findById(cevapId);
        if (cevapOpt.isPresent()) {
            Cevap cevap = cevapOpt.get();
            cevap.setBegeniSayisi(cevap.getBegeniSayisi() + 1);
            cevapRepository.save(cevap);
            // Başarılı olursa yeni beğeni sayısını geri dönüyoruz ki ekranda anında güncelleyelim
            return ResponseEntity.ok(String.valueOf(cevap.getBegeniSayisi()));
        }
        return ResponseEntity.status(404).body("Entry bulunamadı!");
    }

    @DeleteMapping("/sil/{cevapId}")
    public ResponseEntity<String> cevapSil(@PathVariable int cevapId, @RequestParam int adminId) {
        Optional<Kullanici> adminOpt = kullaniciRepository.findById(adminId);
        if (adminOpt.isPresent() && "ADMIN".equals(adminOpt.get().getRol())) {
            if (cevapRepository.existsById(cevapId)) {
                cevapRepository.deleteById(cevapId);
                return ResponseEntity.ok("Entry silindi.");
            }
            return ResponseEntity.status(404).body("Entry bulunamadı.");
        }
        return ResponseEntity.status(403).body("Admin yetkiniz yok!");
    }
}