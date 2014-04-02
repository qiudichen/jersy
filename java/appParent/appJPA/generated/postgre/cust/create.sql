
    create table m_user (
        c_oid varchar(32) not null,
        c_mod timestamp not null,
        c_password varchar(64) not null,
        c_username varchar(256) not null,
        primary key (c_oid)
    );
