export type CategoryType = 'INCOME' | 'EXPENSE';

export interface CategoryRequest {
    userId: number;
    name: string;
    type: CategoryType;
}

export interface CategoryResponse {
    id: string;
    userId: number;
    name: string;
    type: CategoryType;
    createdAt: string;
    updatedAt: string;
}