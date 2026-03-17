package com.camilagksantos.finance.domain.service;

import com.camilagksantos.finance.domain.exception.ReportGenerationException;
import com.camilagksantos.finance.domain.exception.ResourceNotFoundException;
import com.camilagksantos.finance.domain.model.Account;
import com.camilagksantos.finance.domain.model.Report;
import com.camilagksantos.finance.domain.model.Transaction;
import com.camilagksantos.finance.domain.ports.input.ReportUseCase;
import com.camilagksantos.finance.domain.ports.output.AccountOutputPort;
import com.camilagksantos.finance.domain.ports.output.ReportGeneratorPort;
import com.camilagksantos.finance.domain.ports.output.ReportOutputPort;
import com.camilagksantos.finance.domain.ports.output.TransactionOutputPort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReportService implements ReportUseCase {

    private final ReportOutputPort reportOutputPort;
    private final ReportGeneratorPort reportGeneratorPort;
    private final AccountOutputPort accountOutputPort;
    private final TransactionOutputPort transactionOutputPort;

    public ReportService(ReportOutputPort reportOutputPort,
                         ReportGeneratorPort reportGeneratorPort,
                         AccountOutputPort accountOutputPort,
                         TransactionOutputPort transactionOutputPort) {
        this.reportOutputPort = reportOutputPort;
        this.reportGeneratorPort = reportGeneratorPort;
        this.accountOutputPort = accountOutputPort;
        this.transactionOutputPort = transactionOutputPort;
    }

    @Override
    public Report generate(Report report) {
        try {
            List<Account> accounts = accountOutputPort.findAllByUserId(report.userId());

            List<Transaction> transactions = accounts.stream()
                    .flatMap(account -> transactionOutputPort
                            .findAllByAccountIdAndDateBetween(account.id(), report.startDate(), report.endDate())
                            .stream())
                    .toList();

            Report newReport = new Report(
                    UUID.randomUUID(),
                    report.userId(),
                    report.title(),
                    report.type(),
                    report.startDate(),
                    report.endDate(),
                    LocalDateTime.now()
            );

            byte[] content = reportGeneratorPort.generate(newReport, transactions, accounts);

            return reportOutputPort.save(newReport, content);
        } catch (ReportGenerationException e) {
            throw e;
        } catch (Exception e) {
            throw new ReportGenerationException(e.getMessage());
        }
    }

    @Override
    public Report getById(UUID id) {
        return reportOutputPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
    }

    @Override
    public List<Report> getAllByUserId(Long userId) {
        return reportOutputPort.findAllByUserId(userId);
    }

    @Override
    public void delete(UUID id) {
        getById(id);
        reportOutputPort.deleteById(id);
    }
}