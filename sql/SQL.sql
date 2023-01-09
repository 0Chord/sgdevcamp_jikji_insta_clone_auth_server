CREATE TABLE user
(
    id           int          NOT NULL auto_increment primary key,
    email        varchar(100) not null,
    password     varchar(100) not null,
    nickname     varchar(20)  not null,
    phone_number varchar(20)  not null,
    email_auth   boolean      not null default 0,
    user_profile_img varchar(250) not null,
    created_time datetime     not null
);

CREATE TABLE user_roles
(
    user_email varchar(100) not null,
    roles      varchar(20)  not null
);

CREATE TABLE mail_auth
(
    id         int          not null auto_increment primary key,
    user_email varchar(100) not null,
    code       varchar(10)  not null
);

CREATE TABLE refresh_token
(
    refresh_token_id int          NOT NULL auto_increment primary key,
    refresh_token    varchar(250) not null,
    user_email       varchar(100)  not null
);