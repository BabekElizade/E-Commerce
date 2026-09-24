import { http } from './http.js';

export const WishlistApi = {
    add: (wishListId, productId) => http('/wishlist/add', { method: 'POST', body: { wishListId, productId } }),
    getList: (wishlistId, page = 0, size = 10) => http(`/wishlist/list/${wishlistId}?page=${page}&size=${size}`),
    delete: (itemId) => http(`/wishlist/delete/${itemId}`, { method: 'DELETE' }),
    clear: (wishlistId) => http(`/wishlist/clear/${wishlistId}`, { method: 'DELETE' })
};