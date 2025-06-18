CREATE TABLE IF NOT EXISTS taric_cooldowns
(
    id        TEXT PRIMARY KEY,
    effect    TEXT      NOT NULL,
    last_used TIMESTAMP NOT NULL
);