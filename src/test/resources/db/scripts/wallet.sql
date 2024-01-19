create table merchants
(
    id         serial primary key,
    username   varchar(64)   not null unique,
    secret_key varchar(1024) not null,
    created_at timestamp,
    updated_at timestamp
);

create table wallet
(
    id          serial primary key,
    balance     bigint,
    delta       bigint,
    currency    varchar(3),
    merchant_id integer,
    updated_at  timestamp,
    constraint fk_merchant foreign key (merchant_id) references merchants (id)
);

INSERT INTO merchants (id, username, secret_key, created_at, updated_at) VALUES (1, 'mamadoo', '$2a$10$detHsGHj.9WTr.7AdUsyA.0H/2aAktXwBQS/RCJJD7jisYhY6XumC', null, null);

INSERT INTO wallet (id, balance, delta, currency, merchant_id, updated_at) VALUES (2, 0, 0, 'EUR', 1, null);
INSERT INTO wallet (id, balance, delta, currency, merchant_id, updated_at) VALUES (3, 0, 0, 'RUR', 1, null);
INSERT INTO wallet (id, balance, delta, currency, merchant_id, updated_at) VALUES (1, 6300, 300, 'USD', 1, '2024-01-15 11:50:03.178047');

