import { Storage } from './storage.js';

export const Auth = {
    parseJwt(token) {
        try {
            const base64Url = token.split('.')[1];
            const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            const jsonPayload = decodeURIComponent(
                atob(base64)
                    .split('')
                    .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                    .join('')
            );
            return JSON.parse(jsonPayload);
        } catch (e) {
            return null;
        }
    },

    getUser() {
        const token = Storage.getAccessToken();
        if (!token) return null;
        const payload = this.parseJwt(token);
        if (!payload?.sub) return null;
        const refreshObj = Storage.getRefreshTokenObj();
        return {
            username: payload?.sub || refreshObj?.username || '',
            role: payload?.role || 'USER',
            userId: refreshObj?.userId || null
        };
    },

    isAuthenticated() {
        const payload = this.parseJwt(Storage.getAccessToken());
        if (!payload?.sub || !Number.isFinite(payload.exp)) return false;
        if (payload.exp * 1000 > Date.now()) return true;
        const refresh = Storage.getRefreshTokenObj();
        return !!refresh?.token && new Date(refresh.expiredDate).getTime() > Date.now();
    },

    isAdmin() {
        const user = this.getUser();
        return user && user.role === 'ADMIN';
    },

    safeRedirect(value) {
        if (!value || !value.startsWith('/') || value.startsWith('//')) return '/index.html';
        try {
            const target = new URL(value, window.location.origin);
            return target.origin === window.location.origin
                ? target.pathname + target.search + target.hash : '/index.html';
        } catch {
            return '/index.html';
        }
    },

    logout() {
        Storage.clearAuth();
        window.location.href = '/login.html';
    }
};