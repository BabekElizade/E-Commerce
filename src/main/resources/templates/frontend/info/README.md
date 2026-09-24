# Frontend

Tətbiqi başladıb `http://localhost:8080/` və ya
`http://localhost:8080/login.html` açın. Port dəyişibsə, həmin portdan istifadə edin.
HTML fayllarını `file://` və ya IDE preview serveri ilə açmaq əvəzinə
Spring Boot tətbiqinin ünvanından istifadə edin.

`FrontendConfig` səhifələri və asset fayllarını `templates/frontend` qovluğundan
təqdim edir. `assets/js/config.js` daxilində boş `API_BASE_URL` sorğuları
səhifənin serverinə göndərir. Admin HTML səhifələri açıq təqdim edilir;
məlumat və əməliyyat icazələri backend tərəfindən yoxlanır.

Tokenlər `sessionStorage` daxilində saxlanır. Paralel 401 cavabları bir token
yeniləmə sorğusunu paylaşır. Uğursuz yeniləmə bütün gözləyən sorğuları xəta ilə
tamamlayır. Login yönləndirməsi yalnız eyni serverdəki ünvanlara icazə verir.

Səbətin göstərilməsi, istək siyahısı və hesab aktivləşdirilməsi üzrə backend
məhdudiyyətləri [BACKEND-GAPS.md](BACKEND-GAPS.md) faylında qeyd olunub.
Yeni hesab aktivləşdirilmədən giriş edə bilmir. İstək siyahısı səhifəsi
funksiya əlçatan olmayanda məlumat göstərir.

Resursların təqdim olunması testi: `mvn -Dtest=FrontendConfigTest test`.