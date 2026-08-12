create table route_status_overrides (
    id bigserial primary key,
    user_id bigint not null references user_accounts(id) on delete cascade,
    route_key varchar(450) not null,
    status varchar(20) not null,
    constraint uk_route_status_overrides_owner_route unique (user_id, route_key)
);
