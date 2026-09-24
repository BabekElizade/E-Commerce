import { http } from './http.js';

export const AuthApi = {
    login: (data) => http('/login', { method: 'POST', body: data, skipAuth: true }),
    register: (data) => http('/register', { method: 'POST', body: data, skipAuth: true })
};