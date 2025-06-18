CREATE TABLE IF NOT EXISTS taric_cooldowns
(
    id        VARCHAR PRIMARY KEY,
    effect    VARCHAR   NOT NULL,
    last_used TIMESTAMP NOT NULL
);
