CREATE TABLE accounts (
                          id          UUID            NOT NULL,
                          user_id     BIGINT          NOT NULL,
                          name        VARCHAR(100)    NOT NULL,
                          type        VARCHAR(20)     NOT NULL,
                          balance     NUMERIC(19, 2)  NOT NULL DEFAULT 0,
                          created_at  TIMESTAMP       NOT NULL,
                          updated_at  TIMESTAMP       NOT NULL,

                          CONSTRAINT pk_accounts PRIMARY KEY (id)
);