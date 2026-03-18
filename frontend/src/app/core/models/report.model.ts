export type ReportType = 'MONTHLY_STATEMENT' | 'EXPENSES_BY_CATEGORY' | 'BALANCE_EVOLUTION';

export interface ReportRequest {
    userId: number;
    title: string;
    type: ReportType;
    startDate: string;
    endDate: string;
}

export interface ReportResponse {
    id: string;
    userId: number;
    title: string;
    type: ReportType;
    startDate: string;
    endDate: string;
    generatedAt: string;
}