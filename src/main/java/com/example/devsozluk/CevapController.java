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

    // GÜNCELLENEN KISIM: Gelen ID'leri int yerine String alıp, boşluklarını temizleyip içeride güvenle çeviriyoruz.
    @PostMapping("/yaz")
    public ResponseEntity<String> cevapYaz(@RequestParam String baslikId, @RequestParam String kullaniciId, @RequestBody Cevap cevap) {
        try {
            int bId = Integer.parseInt(baslikId.trim());
            int kId = Integer.parseInt(kullaniciId.trim());

            Optional<Baslik> baslikOpt = baslikRepository.findById(bId);
            Optional<Kullanici> yazarOpt = kullaniciRepository.findById(kId);

            if (baslikOpt.isPresent() && yazarOpt.isPresent()) {
                if(cevap.getMetin() == null || cevap.getMetin().trim().isEmpty()) {
                    return ResponseEntity.badRequest().body("Metin boş olamaz!");
                }
                cevap.setBaslik(baslikOpt.get());
                cevap.setYazar(yazarOpt.get());
                cevapRepository.save(cevap);
                return ResponseEntity.ok("Entry eklendi.");
            }
            return ResponseEntity.badRequest().body("Başlık veya Kullanıcı bulunamadı!");
        } catch (NumberFormatException e) {
            // Artık gizli 400 hatası yerine sorunun nereden kaynaklandığını açıkça göreceğiz.
            return ResponseEntity.badRequest().body("HATA: ID'ler bozuk geldi! Başlık: [" + baslikId + "], Kullanıcı: [" + kullaniciId + "]");
        }
    }

    @GetMapping("/baslik/{baslikId}")
    public ResponseEntity<List<Cevap>> cevaplariGetir(@PathVariable int baslikId) {
        return ResponseEntity.ok(cevapRepository.findByBaslikIdOrderByBegeniSayisiDesc(baslikId));
    }

    @PostMapping("/begen/{cevapId}")
    public ResponseEntity<String> cevapBegen(@PathVariable int cevapId) {
        Optional<Cevap> cevapOpt = cevapRepository.findById(cevapId);
        if (cevapOpt.isPresent()) {
            Cevap cevap = cevapOpt.get();
            cevap.setBegeniSayisi(cevap.getBegeniSayisi() + 1);
            cevapRepository.save(cevap);
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