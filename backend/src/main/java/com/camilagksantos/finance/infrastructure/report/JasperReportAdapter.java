package com.camilagksantos.finance.infrastructure.report;

import com.camilagksantos.finance.domain.exception.ReportGenerationException;
import com.camilagksantos.finance.domain.model.Account;
import com.camilagksantos.finance.domain.model.Report;
import com.camilagksantos.finance.domain.model.Transaction;
import com.camilagksantos.finance.domain.ports.output.ReportGeneratorPort;
import com.camilagksantos.finance.infrastructure.report.dto.BalanceEvolutionData;
import com.camilagksantos.finance.infrastructure.report.dto.ExpensesByCategoryData;
import com.camilagksantos.finance.infrastructure.report.dto.MonthlyStatementData;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JasperReportAdapter implements ReportGeneratorPort {

    private static final Logger log = LoggerFactory.getLogger(JasperReportAdapter.class);

    @Override
    public byte[] generate(Report report, List<Transaction> transactions, List<Account> accounts) {
        String templateName = resolveTemplateName(report.type());
        InputStream template = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("reports/" + templateName);

        if (template == null) {
            throw new ReportGenerationException("Template not found for type: " + report.type());
        }

        try {
            JasperReport jasperReport = JasperCompileManager.compileReport(template);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("title", report.title());
            parameters.put("startDate", report.startDate().toString());
            parameters.put("endDate", report.endDate().toString());

            JRDataSource dataSource = resolveDataSource(report.type(), transactions, accounts);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            log.error("Failed to generate report", e);
            throw new ReportGenerationException("Failed to generate report: " + e.getMessage());
        }
    }

    private String resolveTemplateName(Report.ReportType type) {
        return switch (type) {
            case MONTHLY_STATEMENT -> "monthly_statement.jrxml";
            case EXPENSES_BY_CATEGORY -> "expenses_by_category.jrxml";
            case BALANCE_EVOLUTION -> "balance_evolution.jrxml";
        };
    }

    private JRDataSource resolveDataSource(Report.ReportType type, List<Transaction> transactions, List<Account> accounts) {
        return switch (type) {
            case MONTHLY_STATEMENT -> buildMonthlyStatementDataSource(transactions);
            case EXPENSES_BY_CATEGORY -> buildExpensesByCategoryDataSource(transactions);
            case BALANCE_EVOLUTION -> buildBalanceEvolutionDataSource(accounts);
        };
    }

    private JRDataSource buildMonthlyStatementDataSource(List<Transaction> transactions) {
        List<MonthlyStatementData> data = transactions.stream()
                .map(t -> new MonthlyStatementData(
                        t.description(),
                        t.type().name(),
                        t.amount(),
                        t.date()
                ))
                .toList();
        return new JRBeanCollectionDataSource(data);
    }

    private JRDataSource buildExpensesByCategoryDataSource(List<Transaction> transactions) {
        List<ExpensesByCategoryData> data = transactions.stream()
                .filter(t -> t.type() == Transaction.TransactionType.EXPENSE)
                .collect(Collectors.groupingBy(
                        t -> t.categoryId().toString(),
                        Collectors.reducing(
                                java.math.BigDecimal.ZERO,
                                Transaction::amount,
                                java.math.BigDecimal::add
                        )
                ))
                .entrySet().stream()
                .map(e -> new ExpensesByCategoryData(e.getKey(), e.getValue()))
                .toList();
        return new JRBeanCollectionDataSource(data);
    }

    private JRDataSource buildBalanceEvolutionDataSource(List<Account> accounts) {
        List<BalanceEvolutionData> data = accounts.stream()
                .map(a -> new BalanceEvolutionData(
                        a.name(),
                        a.balance(),
                        a.updatedAt().toLocalDate()
                ))
                .toList();
        return new JRBeanCollectionDataSource(data);
    }
}