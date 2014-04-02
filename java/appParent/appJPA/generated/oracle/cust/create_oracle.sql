
    create table m_user (
        c_oid varchar2(32 char) not null,
        c_mod timestamp not null,
        c_password varchar2(64 char) not null,
        c_username varchar2(256 char) not null,
        primary key (c_oid)
    );
