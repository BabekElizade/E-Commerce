# Backend API İnteqrasiya Boşluqları (Backend Gaps Report)

Aşağıdakı imkanlar backend API spesifikasiyasında çatışmadığı üçün frontend tərəfindən saxta məlumatla gizlədilməmiş, rəsmi backend tələbi kimi qeyd edilmişdir:

1. **Cari İstifadəçi Səbəti (`GET /cart/me`):**
   - **Problem:** `GET /cart/list/{id}` yalnız ADMIN rolu tələb edir. Səbət update/delete/clear funksiyaları da yalnız ADMIN icazəlidir. USER öz səbət maddələrini siyahılaya bilmir.
   - **Təklif:** `GET /cart/me`, `PUT /cart/items/{id}`, `DELETE /cart/items/{id}` endpoint-ləri əlavə olunmalı və istifadəçiyə yalnız öz səbətinə çıxış verilməlidir.

2. **Cari Wishlist və Context ID:**
   - **Problem:** Wishlist siyahısını almaq üçün `wishListId` tələb olunur, lakin istifadəçiyə aid `wishListId` login və ya user cavablarında qaytarılmır. `/wishlist/me` mövcud deyil.
   - **Təklif:** `GET /wishlist/me` endpoint-i tətbiq olunmalıdır.

3. **Hesab Aktivləşdirilməsi (Account Activation):**
   - **Problem:** `POST /register` sonrası yeni istifadəçi `PENDING` statusu alır. Lakin hesabı aktivləşdirmək üçün backend-də daxili və ya e-poçt vasitəsilə aktivasiya endpoint-i yoxdur.
   - **Təklif:** `/auth/activate` və ya token əsaslı aktivasiya keçidi əlavə olunmalıdır.

4. **Product Model Və Şəkillər:**
   - **Problem:** `ProductResponse` DTO-da `stock`, `sku`, `imageUrl` və `rating` sahələri qaytarılmır.
   - **Təklif:** Şəkillərin saxlanması və stok məlumatlarının public DTO-ya (və ya stok üçün ayrı xüsusi sahəyə) daxil edilməsi.

5. **Token Revoke Və Logout Endpoint-i:**
   - **Problem:** Server tərəfdə refresh token-i vaxtından əvvəl ləğv edən `/logout` endpoint-i yoxdur.
   - **Təklif:** `/auth/logout` endpoint-inin yaradılması.