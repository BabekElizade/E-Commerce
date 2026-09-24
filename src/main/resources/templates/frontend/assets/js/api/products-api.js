import { http } from './http.js';

export const ProductsApi = {
    getList: (page = 0, size = 10) => http(`/product/list?page=${page}&size=${size}`, { skipAuth: true }),
    getById: (id) => http(`/product/list/${id}`, { skipAuth: true }),
    getByCategory: (catId, page = 0, size = 10) => http(`/product/get-by-category/${catId}?page=${page}&size=${size}`, { skipAuth: true }),
    search: (query, page = 0, size = 10, signal) => http(`/product/search?query=${encodeURIComponent(query)}&page=${page}&size=${size}`, { skipAuth: true, signal }),
    create: (data) => http('/product/create', { method: 'POST', body: data }),
    update: (id, data) => http(`/product/update/${id}`, { method: 'PUT', body: data }),
    changeStatus: (id, status) => http(`/product/change-status/${id}`, { method: 'PUT', body: { status } }),
    delete: (id) => http(`/product/delete/${id}`, { method: 'DELETE' })
};