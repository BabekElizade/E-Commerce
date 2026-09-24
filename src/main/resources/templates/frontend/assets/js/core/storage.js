const ACCESS_TOKEN_KEY = 'app_access_token';
const REFRESH_TOKEN_KEY = 'app_refresh_token_obj';

export const Storage = {
    getAccessToken: () => sessionStorage.getItem(ACCESS_TOKEN_KEY),
    setAccessToken: (token) => sessionStorage.setItem(ACCESS_TOKEN_KEY, token),

    getRefreshTokenObj: () => {
        const data = sessionStorage.getItem(REFRESH_TOKEN_KEY);
        try {
            return data ? JSON.parse(data) : null;
        } catch {
            sessionStorage.removeItem(REFRESH_TOKEN_KEY);
            return null;
        }
    },
    setRefreshTokenObj: (tokenObj) => sessionStorage.setItem(REFRESH_TOKEN_KEY, JSON.stringify(tokenObj)),

    setSession: (data) => {
        if (typeof data?.access_token !== 'string' || !data.access_token ||
            typeof data?.refresh_token?.token !== 'string' || !data.refresh_token.token) {
            throw new Error('Server etibarlı sessiya qaytarmadı. Yenidən daxil olun.');
        }
        sessionStorage.setItem(ACCESS_TOKEN_KEY, data.access_token);
        sessionStorage.setItem(REFRESH_TOKEN_KEY, JSON.stringify(data.refresh_token));
    },

    clearAuth: () => {
        sessionStorage.removeItem(ACCESS_TOKEN_KEY);
        sessionStorage.removeItem(REFRESH_TOKEN_KEY);
    }
};