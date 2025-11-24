
CREATE TABLE users (
                       id          BIGSERIAL PRIMARY KEY,
                       username    VARCHAR(100) NOT NULL UNIQUE,
                       enabled     BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                       updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_users_username ON users (username);


CREATE TABLE roles (
                       id          BIGSERIAL PRIMARY KEY,
                       code        VARCHAR(50)  NOT NULL UNIQUE,
                       name        VARCHAR(100) NOT NULL,
                       description VARCHAR(255),
                       created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                       updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);


CREATE TABLE permissions (
                             id          BIGSERIAL PRIMARY KEY,
                             code        VARCHAR(100) NOT NULL UNIQUE,
                             description VARCHAR(255),
                             created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                             updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW()
);


CREATE TABLE user_roles (
                            id          BIGSERIAL PRIMARY KEY,
                            user_id     BIGINT NOT NULL,
                            role_id     BIGINT NOT NULL,
                            start_date  DATE,
                            end_date    DATE,
                            created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                            CONSTRAINT fk_user_roles_user
                                FOREIGN KEY (user_id) REFERENCES users (id),
                            CONSTRAINT fk_user_roles_role
                                FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE INDEX idx_user_roles_user_id ON user_roles (user_id);
CREATE INDEX idx_user_roles_role_id ON user_roles (role_id);

CREATE UNIQUE INDEX uq_user_roles_user_role_start
    ON user_roles (user_id, role_id, start_date);


CREATE TABLE role_permissions (
                                  id             BIGSERIAL PRIMARY KEY,
                                  role_id        BIGINT NOT NULL,
                                  permission_id  BIGINT NOT NULL,
                                  created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                  CONSTRAINT fk_role_permissions_role
                                      FOREIGN KEY (role_id) REFERENCES roles (id),
                                  CONSTRAINT fk_role_permissions_permission
                                      FOREIGN KEY (permission_id) REFERENCES permissions (id)
);

CREATE INDEX idx_role_permissions_role_id ON role_permissions (role_id);
CREATE INDEX idx_role_permissions_permission_id ON role_permissions (permission_id);

CREATE UNIQUE INDEX uq_role_permissions_role_perm
    ON role_permissions (role_id, permission_id);


CREATE TABLE user_identities (
                                 id             BIGSERIAL PRIMARY KEY,
                                 user_id        BIGINT NOT NULL,
                                 provider       VARCHAR(50)  NOT NULL,
                                 external_id    VARCHAR(200) NOT NULL,
                                 access_token   TEXT,
                                 refresh_token  TEXT,
                                 created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                 updated_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                 CONSTRAINT fk_user_identities_user
                                     FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX idx_user_identities_user_id ON user_identities (user_id);

CREATE UNIQUE INDEX uq_user_identities_provider_external
    ON user_identities (provider, external_id);


CREATE TABLE user_credentials (
                                  id           BIGSERIAL PRIMARY KEY,
                                  user_id      BIGINT NOT NULL,
                                  type         VARCHAR(50)  NOT NULL,
                                  secret_hash  VARCHAR(255) NOT NULL,
                                  created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                                  revoked      BOOLEAN NOT NULL DEFAULT FALSE,
                                  valid_to     TIMESTAMPTZ,
                                  CONSTRAINT fk_user_credentials_user
                                      FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX idx_user_credentials_user_id ON user_credentials (user_id);
CREATE INDEX idx_user_credentials_type ON user_credentials (type);


