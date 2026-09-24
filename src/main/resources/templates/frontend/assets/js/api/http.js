import { CONFIG } from '../config.js';
import { Storage } from '../core/storage.js';
import { Auth } from '../core/auth.js';

let refreshPromise = null;

function refreshSession() {
    if (!refreshPromise) {
        refreshPromise = (async () => {
            const refreshToken = Storage.getRefreshTokenObj()?.token;
            if (!refreshToken) throw new Error('Sessiyanın vaxtı bitib. Yenidən daxil olun.');
            const response = await fetch(`${CONFIG.API_BASE_URL}/refresh_token`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ refreshToken })
            });
            const data = await handleResponse(response);
            Storage.setSession(data);
            return data.access_token;
        })().catch(error => {
            Auth.logout();
            throw error;
        }).finally(() => {
            refreshPromise = null;
        });
    }
    return refreshPromise;
}

export async function http(endpoint, options = {}) {
    const { skipAuth = false, ...requestOptions } = options;
    const url = `${CONFIG.API_BASE_URL}${endpoint}`;
    const headers = { 'Content-Type': 'application/json', ...requestOptions.headers };
    const token = Storage.getAccessToken();
    if (token && !skipAuth) headers.Authorization = `Bearer ${token}`;
    const config = { ...requestOptions, headers };
    if (config.body && typeof config.body === 'object') {
        config.body = JSON.stringify(config.body);
    }

    try {
        let response = await fetch(url, config);
        if (response.status === 401 && !skipAuth) {
            // A concurrent request may already have refreshed this token.
            const currentToken = Storage.getAccessToken();
            const newToken = currentToken && currentToken !== token
                ? currentToken : await refreshSession();
            config.headers.Authorization = `Bearer ${newToken}`;
            response = await fetch(url, config);
            if (response.status === 401) Auth.logout();
        }
        return await handleResponse(response);
    } catch (error) {
        if (error.name === 'TypeError') {
            throw new Error('Şəbəkə bağlantısı xətası. Yenidən cəhd edin.');
        }
        throw error;
    }
}

async function handleResponse(response) {
    if (response.status === 204) return null;
    const contentType = response.headers.get('content-type') || '';
    const text = await response.text();
    let data = text || null;
    if (text && (contentType.includes('application/json') || contentType.includes('+json'))) {
        try {
            data = JSON.parse(text);
        } catch {
            throw new Error('Serverin cavabı oxuna bilmədi.');
        }
    }
    if (!response.ok) {
        let message = data && typeof data === 'object'
            ? data.exception?.message || data.message || data.detail
            : null;
        if (message && typeof message === 'object') {
            message = Object.values(message).flat().join(' ');
        }
        const error = new Error(message || `Sorğu uğursuz oldu (${response.status}).`);
        error.status = response.status;
        throw error;
    }
    return data;
}