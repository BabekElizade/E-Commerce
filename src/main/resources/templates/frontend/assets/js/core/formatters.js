import { CONFIG } from '../config.js';

export const Formatters = {
    formatPrice(amount, currency = CONFIG.CURRENCY_SYMBOL) {
        if (amount === null || amount === undefined) return '0.00 ' + currency;
        return `${Number(amount).toFixed(2)} ${currency}`;
    },

    formatDate(dateString) {
        if (!dateString) return '-';
        try {
            const date = new Date(dateString);
            return date.toLocaleString('az-AZ', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit'
            });
        } catch (e) {
            return dateString;
        }
    },

    escapeHtml(str) {
        if (!str) return '';
        return String(str)
            .replace(/&/g, '&amp;')
            .replace(/</g, '&lt;')
            .replace(/>/g, '&gt;')
            .replace(/"/g, '&quot;')
            .replace(/'/g, '&#039;');
    }
};