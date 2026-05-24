package com.example.devsozluk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@RestController
@RequestMapping("/api/kullanici")
public class KullaniciController {
    
    @Autowired 
    private KullaniciRepository kullaniciRepository;

    // Şifreyi SHA-256 ile geri döndürülemez şekilde şifreleyen (hashleyen) yardımcı metodumuz
    private String sifreHashle(String gercekSifre) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(gercekSifre.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Şifreleme sırasında bir hata oluştu!");
        }
    }

    @PostMapping("/kayit")
    public ResponseEntity<String> kayitOl(@RequestBody Kullanici kullanici) {
        if(kullaniciRepository.findByKullaniciAdi(kullanici.getKullaniciAdi()) != null) {
            return ResponseEntity.badRequest().body("Bu kullanıcı adı zaten alınmış!");
        }
        
        // Kullanıcının girdiği saf şifreyi (örn: "1234") alıp, hashlenmiş versiyonuyla değiştiriyoruz
        String guvenliSifre = sifreHashle(kullanici.getSifreHash());
        kullanici.setSifreHash(guvenliSifre); 
        
        kullaniciRepository.save(kullanici);
        return ResponseEntity.ok("Kullanıcı kaydı başarıyla oluşturuldu.");
    }

    @PostMapping("/giris")
    public ResponseEntity<?> girisYap(@RequestBody Kullanici loginBilgileri) {
        Kullanici dbKullanici = kullaniciRepository.findByKullaniciAdi(loginBilgileri.getKullaniciAdi());
        
        // Kullanıcı giriş yaparken girdiği şifreyi de hashliyoruz ve veritabanındaki hash ile karşılaştırıyoruz
        if (dbKullanici != null) {
            String girilenSifreHashli = sifreHashle(loginBilgileri.getSifreHash());
            
            if (dbKullanici.getSifreHash().equals(girilenSifreHashli)) {
                // Güvenlik için şifreyi arayüze (tarayıcıya) göndermeden önce gizliyoruz (null yapıyoruz)
                dbKullanici.setSifreHash(null);
                return ResponseEntity.ok(dbKullanici);
            }
        }
        
        return ResponseEntity.status(401).body("Hata: Geçersiz kullanıcı adı veya şifre!");
    }
}