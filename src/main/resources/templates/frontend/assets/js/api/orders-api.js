import { http } from './http.js';

export const OrdersApi = {
    createOrder: () => http('/orders/create', { method: 'POST' }),
    getMyOrders: (page = 0, size = 10) => http(`/orders/me?page=${page}&size=${size}`),
    getMyOrderById: (orderId) => http(`/orders/me/${orderId}`),
    cancelOrder: (orderId) => http(`/orders/${orderId}/cancel`, { method: 'PATCH' })
};