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
    external_transaction_id varchar,
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

INSERT INTO customers (external_id, first_name, last_name, country) VALUES (15, 'John', 'Doe', 'BR');

INSERT INTO transactions (id, external_transaction_id, transaction_type, status, message_tr, created_at, updated_at, payment_method, amount, currency, external_created_at, language_tr, notification_url, customer_id) VALUES ('eb36b6e4-d72e-4db2-b3ca-4ef400005a5a', '13546854387', 'TOP_UP', 'FAILED', 'OK', '2024-01-9 16:12:21.554258', '2024-01-12 16:12:24.624370', 'CARD', 300, 'USD', null, 'EN', 'https://proselyte.net/webhook/transaction', 15);
INSERT INTO transactions (id, external_transaction_id, transaction_type, status, message_tr, created_at, updated_at, payment_method, amount, currency, external_created_at, language_tr, notification_url, customer_id) VALUES ('c1c2a40e-8263-4d68-a7ee-cb7a1c38916f', '13546854387', 'PAY_OUT', 'IN_PROGRESS', 'OK', '2024-01-10 16:12:35.505517', '2024-01-12 16:12:44.629441', 'CARD', 300, 'USD', null, 'EN', 'https://proselyte.net/webhook/transaction', 15);
INSERT INTO transactions (id, external_transaction_id, transaction_type, status, message_tr, created_at, updated_at, payment_method, amount, currency, external_created_at, language_tr, notification_url, customer_id) VALUES ('81ceb646-b0f4-4913-8124-bb442dc1133c', '13546854387', 'TOP_UP', 'COMPLETED', 'OK', '2024-01-12 16:15:11.073096', '2024-01-12 16:30:45.008489', 'CARD', 300, 'USD', null, 'EN', 'https://proselyte.net/webhook/transaction', 15);
INSERT INTO transactions (id, external_transaction_id, transaction_type, status, message_tr, created_at, updated_at, payment_method, amount, currency, external_created_at, language_tr, notification_url, customer_id) VALUES ('de2b5660-f2d6-4b62-9f09-51067e48b993', '13546854387', 'PAY_OUT', 'COMPLETED', 'OK', '2024-01-12 16:32:21.469393', '2024-01-12 16:32:25.031396', 'CARD', 300, 'USD', null, 'EN', 'https://proselyte.net/webhook/transaction', 15);
INSERT INTO transactions (id, external_transaction_id, transaction_type, status, message_tr, created_at, updated_at, payment_method, amount, currency, external_created_at, language_tr, notification_url, customer_id) VALUES ('7f5626ff-fca6-4aa2-a0ae-57d601b919ca', '13546854387', 'TOP_UP', 'IN_PROGRESS', 'OK', '2024-01-12 16:33:04.391695', '2024-01-12 16:33:08.183953', 'CARD', 300, 'USD', null, 'EN', 'https://proselyte.net/webhook/transaction', 15);
