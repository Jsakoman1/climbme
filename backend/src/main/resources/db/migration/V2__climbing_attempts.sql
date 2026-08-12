create table climbing_attempts (
    id bigserial primary key,
    user_id bigint not null references user_accounts(id) on delete cascade,
    climbed_on date not null,
    location varchar(120) not null,
    sector varchar(120) not null,
    route_name varchar(160) not null,
    grade varchar(20) not null,
    length_meters integer,
    style varchar(20) not null,
    attempt_number integer not null,
    sent boolean not null,
    time_on_route_minutes integer,
    rpe integer,
    conditions varchar(30),
    partner varchar(120),
    notes varchar(2000)
);

create index idx_climbing_attempts_owner_date on climbing_attempts(user_id, climbed_on desc, id desc);
