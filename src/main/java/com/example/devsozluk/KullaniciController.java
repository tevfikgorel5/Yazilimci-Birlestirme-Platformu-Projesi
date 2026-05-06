package com.example.devsozluk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kullanici")
public class KullaniciController {
    
    @Autowired 
    private KullaniciRepository kullaniciRepository;

    @PostMapping("/kayit")
    public ResponseEntity<String> kayitOl(@RequestBody Kullanici kullanici) {
        if(kullaniciRepository.findByKullaniciAdi(kullanici.getKullaniciAdi()) != null) {
            return ResponseEntity.badRequest().body("Bu kullanıcı adı zaten alınmış!");
        }
        kullanici.setSifreHash(kullanici.getSifreHash()); 
        kullaniciRepository.save(kullanici);
        return ResponseEntity.ok("Kullanıcı kaydı başarıyla oluşturuldu.");
    }

    // GÜNCELLENEN KISIM: Artık giriş yapınca sadece metin değil, kullanıcının kendisini dönüyoruz
    @PostMapping("/giris")
    public ResponseEntity<?> girisYap(@RequestBody Kullanici loginBilgileri) {
        Kullanici dbKullanici = kullaniciRepository.findByKullaniciAdi(loginBilgileri.getKullaniciAdi());
        
        if (dbKullanici != null && dbKullanici.getSifreHash().equals(loginBilgileri.getSifreHash())) {
            // Güvenlik için şifreyi arayüze göndermeden önce gizliyoruz (null yapıyoruz)
            dbKullanici.setSifreHash(null);
            
            // Başarılı girişte kullanıcının ID'si, Adı ve Rolü (ADMIN/STANDART) arayüze JSON olarak gidecek
            return ResponseEntity.ok(dbKullanici);
        }
        
        return ResponseEntity.status(401).body("Hata: Geçersiz kullanıcı adı veya şifre!");
    }
}