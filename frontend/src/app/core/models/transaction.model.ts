export type TransactionType = 'INCOME' | 'EXPENSE';

export interface TransactionRequest {
    accountId: string;
    categoryId: string;
    amount: number;
    type: TransactionType;
    description?: string;
    date: string;
}

export interface TransactionResponse {
    id: string;
    accountId: string;
    categoryId: string;
    amount: number;
    type: TransactionType;
    description?: string;
    date: string;
    createdAt: string;
    updatedAt: string;
}