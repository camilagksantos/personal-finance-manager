CREATE TABLE reports (
                         id           UUID         NOT NULL,
                         user_id      BIGINT       NOT NULL,
                         title        VARCHAR(255) NOT NULL,
                         type         VARCHAR(50)  NOT NULL,
                         generated_at TIMESTAMP    NOT NULL,
                         content      BYTEA        NOT NULL,

                         CONSTRAINT pk_reports PRIMARY KEY (id)
);