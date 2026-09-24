import { http } from './http.js';

export const CategoriesApi = {
    async getAllParentCategories() {
        const categories = [];
        let page = 0;
        let result;
        do {
            result = await this.getParentCategories(page++, 50);
            categories.push(...(result.content || []));
        } while (page < (result.totalPages || 0));
        return categories;
    },
    getParentCategories: (page = 0, size = 20) => http(`/e-commerce/parent-category?page=${page}&size=${size}`, { skipAuth: true }),
    getById: (id) => http(`/e-commerce/category/${id}`, { skipAuth: true }),
    create: (data) => http('/e-commerce/create', { method: 'POST', body: data }),
    update: (id, data) => http(`/e-commerce/update-category/${id}`, { method: 'PUT', body: data }),
    delete: (id) => http(`/e-commerce/delete/${id}`, { method: 'DELETE' })
};