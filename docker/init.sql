drop table if exists license, organization;

create table organization (
    id varchar primary key,
    name varchar not null,
    contact_name varchar not null,
    contact_email varchar not null,
    contact_phone varchar not null
);

create table license (
    id varchar primary key,
    organization_id varchar not null references organization,
    description varchar,
    product_name varchar not null,
    license_type varchar not null,
    comment varchar
);

