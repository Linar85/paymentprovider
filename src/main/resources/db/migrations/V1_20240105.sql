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

create table customers
(
    external_id integer primary key,
    first_name  varchar,
    last_name   varchar,
    country     varchar
);

create table transactions
(
    id                      varchar primary key DEFAULT gen_random_uuid(),
    external_transaction_id varchar unique,
    transaction_type        varchar,
    status                  varchar,
    message_tr              varchar,
    created_at              timestamp,
    updated_at              timestamp,
    payment_method          varchar,
    amount                  bigint,
    currency                varchar,
    external_created_at     timestamp,
    language_tr             varchar,
    notification_url        varchar,
    customer_id             integer,
    constraint fk_customer foreign key (customer_id) references customers (external_id)
);

create table webhooks
(
    id                      varchar primary key DEFAULT gen_random_uuid(),
    provider_transaction_id varchar unique,
    external_transaction_id varchar unique,
    transaction_type        varchar,
    status                  varchar,
    message_tr              varchar,
    created_at              timestamp,
    updated_at              timestamp,
    payment_method          varchar,
    amount                  bigint,
    currency                varchar,
    external_created_at     timestamp,
    language_tr             varchar
);

create table webhook_responses
(
    id         serial primary key,
    webhook_id varchar,
    body       varchar,
    created_at timestamp,
    constraint fk_webhook_id foreign key (webhook_id) references webhooks (id)
);

create table cards
(
    id          serial primary key,
    card_number bigint unique,
    exp_date    varchar,
    cvv         integer,
    customer_id integer,
    constraint fk_customer foreign key (customer_id) references customers (external_id)
);

insert into merchants (username, secret_key)
VALUES ('mamadoo', '$2a$10$detHsGHj.9WTr.7AdUsyA.0H/2aAktXwBQS/RCJJD7jisYhY6XumC');

insert into wallet(balance, delta, currency, merchant_id)
VALUES (0, 0, 'USD', 1);
insert into wallet(balance, delta, currency, merchant_id)
VALUES (0, 0, 'EUR', 1);
insert into wallet(balance, delta, currency, merchant_id)
VALUES (0, 0, 'RUR', 1);




