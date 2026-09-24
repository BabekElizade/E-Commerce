import { http } from './http.js';

export const ReviewsApi = {
    getByProduct: (productId, page = 0, size = 10) => http(`/review/get-by-product-id/${productId}?page=${page}&size=${size}`, { skipAuth: true }),
    getMyReviews: (page = 0, size = 10) => http('/review/get-my-reviews?page=' + page + '&size=' + size),
    create: (productId, rating, comment) => http('/review/create', { method: 'POST', body: { id: productId, rating, comment } }),
    update: (reviewId, data) => http(`/review/update/${reviewId}`, { method: 'PATCH', body: data }),
    delete: (reviewId) => http(`/review/delete/${reviewId}`, { method: 'DELETE' })
};