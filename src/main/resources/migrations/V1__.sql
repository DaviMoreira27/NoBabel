-- Grant usage on schema public to your user
GRANT USAGE ON SCHEMA public TO pgdefault_user;

SELECT current_user;

-- Grant create on schema public to your user
GRANT CREATE ON SCHEMA public TO pgdefault_user;

CREATE TABLE "user"
(
    id              VARCHAR(255) NOT NULL,
    username        VARCHAR(255) NOT NULL,
    email           VARCHAR(255) NOT NULL,
    google_auth_key VARCHAR(255) NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE user_history
(
    id                UUID         NOT NULL,
    user_id           VARCHAR(255) NOT NULL,
    event_date        TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    mistakes_numbers  INTEGER      NOT NULL,
    successes_numbers INTEGER      NOT NULL,
    task_payload      JSONB        NOT NULL,
    CONSTRAINT pk_user_history PRIMARY KEY (id)
);

ALTER TABLE "user"
    ADD CONSTRAINT uc_user_email UNIQUE (email);

ALTER TABLE "user"
    ADD CONSTRAINT uc_user_google_auth_key UNIQUE (google_auth_key);

ALTER TABLE user_history
    ADD CONSTRAINT FK_USER_HISTORY_ON_USER FOREIGN KEY (user_id) REFERENCES "user" (id);