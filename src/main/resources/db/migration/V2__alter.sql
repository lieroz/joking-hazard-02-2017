create index idx_login on users (login);

alter table users drop password;
alter table users add column password VARCHAR(50);