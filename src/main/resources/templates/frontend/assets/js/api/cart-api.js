import { http } from './http.js';

export const CartApi = {
    add: (productId, quantity) => http('/cart/create', { method: 'POST', body: { productId, quantity } }),
    getCartAdmin: (cartId) => http(`/cart/list/${cartId}`),
    updateItemAdmin: (cartItemId, quantity) => http(`/cart/update/${cartItemId}`, { method: 'PUT', body: { quantity } }),
    deleteItemAdmin: (cartItemId) => http(`/cart/delete/${cartItemId}`, { method: 'DELETE' }),
    clearCartAdmin: (cartId) => http(`/cart/clear/${cartId}`, { method: 'DELETE' })
};