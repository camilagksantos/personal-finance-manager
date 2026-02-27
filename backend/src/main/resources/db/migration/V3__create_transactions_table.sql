CREATE TABLE transactions (
                              id          UUID            NOT NULL,
                              account_id  UUID            NOT NULL,
                              category_id UUID            NOT NULL,
                              amount      NUMERIC(19, 2)  NOT NULL,
                              type        VARCHAR(20)     NOT NULL,
                              description VARCHAR(255),
                              date        DATE            NOT NULL,
                              created_at  TIMESTAMP       NOT NULL,
                              updated_at  TIMESTAMP       NOT NULL,

                              CONSTRAINT pk_transactions     PRIMARY KEY (id),
                              CONSTRAINT fk_transactions_account  FOREIGN KEY (account_id)  REFERENCES accounts(id),
                              CONSTRAINT fk_transactions_category FOREIGN KEY (category_id) REFERENCES categories(id)
);