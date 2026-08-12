create table user_accounts (
    id bigserial primary key,
    email varchar(254) not null,
    password_hash varchar(100) not null,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    constraint uk_user_accounts_email unique (email)
);
