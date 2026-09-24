import { http } from './http.js';

export const WalletApi = {
    getWallet: () => http('/wallet/me/get-my-wallet'),
    getTransactions: (page = 0, size = 10) => http(`/wallet/me/get-my-transactions?page=${page}&size=${size}`),
    initiateTopUp: (amount) => http('/wallet/me/initiate-top-up', { method: 'POST', body: { amount } })
};