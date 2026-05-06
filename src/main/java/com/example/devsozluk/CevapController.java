package com.example.devsozluk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // UC12: Sayfalama (Pagination) mantığı eklendi
    @GetMapping("/baslik/{baslikId}")
    public ResponseEntity<Page<Cevap>> cevaplariGetir(@PathVariable int baslikId, @RequestParam(defaultValue = "0") int sayfa) {
        // Her sayfada 5 entry gösterilecek ve yazılma tarihine göre eskiden yeniye sıralanacak
        Pageable sayfalama = PageRequest.of(sayfa, 5, Sort.by("yazilmaTarihi").ascending());
        return ResponseEntity.ok(cevapRepository.findByBaslikId(baslikId, sayfalama));
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