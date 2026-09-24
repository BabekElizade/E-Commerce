import { Auth } from './auth.js';

export const Guards = {
    requireAuth() {
        if (!Auth.isAuthenticated()) {
            window.location.href = `/login.html?redirect=${encodeURIComponent(window.location.pathname + window.location.search)}`;
            return false;
        }
        return true;
    },

    requireAdmin() {
        if (!this.requireAuth()) return false;
        if (!Auth.isAdmin()) {
            window.location.href = '/index.html';
            return false;
        }
        return true;
    }
};