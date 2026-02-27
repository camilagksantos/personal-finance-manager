CREATE TABLE categories (
                            id          UUID            NOT NULL,
                            user_id     BIGINT          NOT NULL,
                            name        VARCHAR(100)    NOT NULL,
                            type        VARCHAR(20)     NOT NULL,
                            created_at  TIMESTAMP       NOT NULL,
                            updated_at  TIMESTAMP       NOT NULL,

                            CONSTRAINT pk_categories PRIMARY KEY (id)
);