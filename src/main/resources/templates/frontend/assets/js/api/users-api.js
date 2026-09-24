import { http } from './http.js';

export const UsersApi = {
    getById: (id) => http(`/user/by-id/${id}`),
    getByUsername: (username) => http(`/user/by-username/${encodeURIComponent(username)}`),
    getUsersAdmin: (page = 0, size = 10) => http(`/user/list?page=${page}&size=${size}`),
    update: (id, firstName, lastName) => http(`/user/update/${id}`, { method: 'PUT', body: { firstName, lastName } }),
    delete: (id) => http(`/user/delete/${id}`, { method: 'DELETE' })
};