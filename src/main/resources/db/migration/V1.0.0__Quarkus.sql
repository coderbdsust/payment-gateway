CREATE SEQUENCE public.payment_history_seq
    INCREMENT BY 10
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;


CREATE TABLE public.payment_history (
    id BIGINT NOT NULL DEFAULT nextval('public.payment_history_seq'),
    amount NUMERIC(38, 2),
    currency VARCHAR(255),
    country VARCHAR(255),
    gateway_status VARCHAR(255),
    gateway_txn_id VARCHAR(255),
    provider_txn_id VARCHAR(255),
    provider_status VARCHAR(255),
    created_time TIMESTAMP(6),
    txn_time TIMESTAMP(6),
    updated_time TIMESTAMP(6),
    industry VARCHAR(255),
    provider VARCHAR(255),
    provider_response TEXT,
    "source" VARCHAR(255),
    reference VARCHAR(255),
    CONSTRAINT payment_history_pkey PRIMARY KEY (id)
);

