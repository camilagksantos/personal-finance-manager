package com.camilagksantos.finance.domain.ports.output;

import com.camilagksantos.finance.domain.model.Account;
import com.camilagksantos.finance.domain.model.Report;
import com.camilagksantos.finance.domain.model.Transaction;

import java.util.List;

public interface ReportGeneratorPort {
    byte[] generate(Report report, List<Transaction> transactions, List<Account> accounts);
}
