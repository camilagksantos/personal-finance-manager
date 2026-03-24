CREATE INDEX idx_transactions_account_id ON transactions(account_id);
CREATE INDEX idx_transactions_date        ON transactions(date);
CREATE INDEX idx_accounts_user_id         ON accounts(user_id);