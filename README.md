# 📺 Karadana TV

> **Kotlin + Jetpack Compose** ile geliştirilmiş, Firebase tabanlı video listesi, video oynatma ve canlı yayın özelliklerine sahip Android mobil uygulaması.

---

## 📌 Genel Bakış
- **Mimari:** MVVM (Model–View–ViewModel)
- **Platform:** Android (Jetpack Compose)
- **Dil:** Kotlin
- **Durum Yönetimi:** StateFlow
- **UI:** Material 3 Design

Mobil uygulamanın çalıştırılabilir **APK** dosyası proje zip’inde bulunabilir.  
Android cihazınızda doğrudan test edebilirsiniz.

---

## 🛠 Mimarinin Seçilmesi
MVVM yapısı sayesinde:
- Kod **modüler** ve **test edilebilir**.
- Bakımı kolay.
- View, ViewModel ve Model katmanları net şekilde ayrılmış.

---

## 📂 Katmanlar

### **Model (Data) Katmanı**
`/data`
- **Modeller:** `User.kt`, `Video.kt`
- **Constants & Events**
- Firebase’den gelen veriler doğrudan modellerle eşleşir.

### **ViewModel Katmanı**
`/viewmodels`
- `AuthViewModel` → Firebase Auth giriş/çıkış
- `VideosViewModel` → Firestore’dan video listesi
- `BaseViewModel` → Ortak hata yönetimi

**Sorumluluklar:**
- UI’dan gelen eylemleri işlemek.
- Firebase çağrılarını yapmak.
- StateFlow ile anlık veri göndermek.

### **View (UI) Katmanı**
`/ui/components`
- CustomAppBar, CustomNavigationBar, CustomOutlinedTextField
- VideoCard, VideosGrid, VideoPlayerContent

`/ui/screens`
- LoginScreen, SignUpScreen, VideoListScreen, VideoPlayerScreen, LiveScreen

**Sorumluluklar:**
- Kullanıcıya veriyi göstermek.
- Etkileşimleri ViewModel’e iletmek.

### **Utils**
- `resolvePlayableUrl` → Firebase Storage `gs://` URL’sini HTTP linke çevirir.
- `navigateTo` → Güvenli Navigation geçişi.
- `isShortOrLong` → Metin uzunluğu kontrolü.
- `ObserveErrorMessage` → Hata mesajı gösterimi.
- `CheckSignedIn` / `CheckSignedOut` → Auth durumuna göre yönlendirme.

---

## 🧭 Navigasyon Yapısı
- **NavHost** → `MainActivity.kt` içinde tanımlı.
- **Ekran Akışı:**
    1. Login → `LoginScreen`
    2. Sign-Up → `SignUpScreen`
    3. Video List → `VideoListScreen`
    4. Video Player → `VideoPlayerScreen`
    5. Live Streaming → `LiveScreen` (YouTube – 7/24 “Yeşil Deniz” canlı yayını)

---

## 📚 Kullanılan Kütüphaneler
- **Firebase Auth** → Kullanıcı giriş/çıkış
- **Firebase Firestore** → Video & canlı yayın verileri
- **Firebase Storage** → Thumbnail / video dosyaları
- **Media3 ExoPlayer** → MP4 oynatma
- **Android YouTube Player** → YouTube canlı yayın
- **Coil** → Görsel yükleme (AsyncImage)
- **Navigation Compose** → Ekran geçişleri
- **Material 3** → Modern UI tasarımı

---

## 🔄 Veri Akışı
Kurulum

Projeyi klonla

git clone https://github.com/<kullanici>/<repo>.git
cd <repo>


Android Studio ile aç (Giraffe/Koala+ önerilir).

Google servis dosyası
app/ dizinine google-services.json dosyanı ekle.

Gradle senkronizasyonu nu tamamla.

Firebase Yapılandırması

Aşağıdaki servisleri etkinleştir:

Authentication → Email/Password

Cloud Firestore → (geliştirme için test modunda)

Cloud Storage → (us‑nam5 bölgesi; bazı durumlarda plan yükseltme gerekebilir)

Not: Kişisel projelerde Storage yerine Cloudinary API tercih edildiği raporda belirtilmiş.

Çalıştırma

Debug:

./gradlew :app:assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk


Android Studio’dan Run ▶ ile cihaz/emülatörde başlat.

Veri Akışı

UI (ör. VideoListScreen) kullanıcı etkileşimini ViewModel’e iletir.

ViewModel, Firestore/Storage’dan veriyi çeker.

ViewModel, StateFlow ile UI’a veri gönderir.

UI, collectAsState() ile veriyi gözleyip ekrana yansıtır.

Navigasyon: MainActivity içinde NavHost tanımlıdır ve geçişler ViewModel state’lerine göre yönetilir.

Ekran Görselleri:
<img width="1080" height="2340" alt="Screenshot_20250815_173928" src="https://github.com/user-attachments/assets/44056516-b8e1-4528-823a-685a279c330d" />
<img width="1080" height="2340" alt="Screenshot_20250815_175040" src="https://github.com/user-attachments/assets/0a9849f6-6c8b-47a6-9ae6-21ef03193582" />
<img width="1080" height="2340" alt="Screenshot_20250815_174449" src="https://github.com/user-attachments/assets/481a860e-bf73-4b3b-8763-48e089c2c280" />
<img width="1080" height="2340" alt="Screenshot_20250815_174405" src="https://github.com/user-attachments/assets/91942ffb-f1b2-45ea-b73d-9aeb3d5ad16f" />
<img width="1080" height="2340" alt="Screenshot_20250815_174251" src="https://github.com/user-attachments/assets/fda1c39a-96fa-4a58-b065-68e1dbd7382d" />
<img width="2340" height="1080" alt="Screenshot_20250815_174213" src="https://github.com/user-attachments/assets/4b97142e-3f3b-4d6b-bb33-c97eca9abbef" />
<img width="1080" height="2340" alt="Screenshot_20250815_174127" src="https://github.com/user-attachments/assets/71e46548-9ad7-4f44-a438-e0e80ab8645c" />

Lisans

Bu proje eğitim/demonstrasyon amaçlıdır. Kurum/içerik lisanslarına ve yayın servislerinin koşullarına uymak geliştiricinin sorumluluğundadır.
