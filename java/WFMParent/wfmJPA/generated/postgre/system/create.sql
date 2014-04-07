
    create table R_SYSPROPS (
        c_oid varchar(32) not null,
        c_mod timestamp not null,
        C_NAME varchar(255) not null,
        C_SUBSYSTEM varchar(32) not null,
        C_VAL varchar(255),
        primary key (c_oid),
        unique (C_SUBSYSTEM, C_NAME)
    );
