create index idx_login on users (login);

alter table users alter column password type VARCHAR(50);