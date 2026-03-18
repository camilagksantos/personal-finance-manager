export type AccountType = 'CHECKING' | 'SAVINGS' | 'CREDIT_CARD';

export interface AccountRequest {
    userId: number;
    name: string;
    type: AccountType;
    balance: number;
}

export interface AccountResponse {
    id: string;
    userId: number;
    name: string;
    type: AccountType;
    balance: number;
    createdAt: string;
    updatedAt: string;
}