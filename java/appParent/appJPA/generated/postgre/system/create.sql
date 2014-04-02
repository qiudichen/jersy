
    create table R_SYSPROPS (
        c_oid varchar2(32 char) not null,
        c_mod timestamp not null,
        C_NAME varchar2(255 char) not null,
        C_SUBSYSTEM varchar2(32 char) not null,
        C_VAL varchar2(255 char),
        primary key (c_oid),
        unique (C_SUBSYSTEM, C_NAME)
    );
