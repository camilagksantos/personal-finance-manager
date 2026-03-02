package com.camilagksantos.finance.domain.exception;

public class ReportGenerationException extends BusinessRuleException {
    public ReportGenerationException(String message) {
        super("Failed to generate report: " + message);
    }
}
