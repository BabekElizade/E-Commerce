import { Auth } from '../core/auth.js';
import { Formatters } from '../core/formatters.js';

export function renderHeader() {
    const headerEl = document.getElementById('main-header');
    if (!headerEl) return;

    const isAuth = Auth.isAuthenticated();
    const user = Auth.getUser();
    const isAdmin = Auth.isAdmin();

    headerEl.innerHTML = `
    <nav class="nav-container">
      <a href="/index.html" class="brand-logo">E-Commerce</a>
      <div class="nav-links">
        <a href="/index.html">Ana Səhifə</a>
        ${isAuth ? `
          <a href="/cart.html">Səbət</a>
          <a href="/orders.html">Sifarişlərim</a>
          <a href="/wallet.html">Pul Qabı</a>
          <a href="/my-reviews.html">Rəylərim</a>
          <a href="/wishlist.html">İstək siyahısı</a>
          <a href="/profile.html">Profil (${Formatters.escapeHtml(user?.username)})</a>
          ${isAdmin ? `<a href="/admin/index.html" class="badge-admin">Admin Panel</a>` : ''}
          <button id="logout-btn" class="btn-link">Çıxış</button>
        ` : `
          <a href="/login.html">Daxil Ol</a>
          <a href="/register.html" class="btn-primary-sm">Qeydiyyat</a>
        `}
      </div>
    </nav>
  `;

    const logoutBtn = document.getElementById('logout-btn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => Auth.logout());
    }
}