import { http } from './http.js';

export const PaymentsApi = {
    payOrder: (orderId) => http(`/payment/pay/${orderId}`, { method: 'POST' }),
    getByOrder: (orderId) => http(`/payment/by-order/${orderId}`)
};