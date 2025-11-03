CREATE TABLE task (
                      id            BIGSERIAL PRIMARY KEY,
                      title         VARCHAR(200) NOT NULL,
                      description   TEXT,
                      created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);
