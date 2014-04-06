
    create table R_ADDR (
        C_ID int8 not null,
        C_CREATE_DATE timestamp not null,
        C_ADDRS1 varchar(40) not null,
        C_ADDRS2 varchar(40),
        C_CITY varchar(40) not null,
        C_STATE varchar(2) not null,
        C_ZIP varchar(5),
        C_AGT int8,
        primary key (C_ID)
    );

    create table R_AGT (
        C_ID int8 not null,
        C_CREATE_DATE timestamp not null,
        C_FIRSTNAME varchar(40) not null,
        C_LASTNAME varchar(40) not null,
        C_START_DATE date not null,
        primary key (C_ID)
    );

    create table R_AGT_SKILL (
        C_AGT int8 not null,
        C_SKILL int8 not null
    );

    create table R_SKILL (
        C_ID int8 not null,
        C_SKILLNAME varchar(40) not null unique,
        primary key (C_ID)
    );

    alter table R_ADDR 
        add constraint FK9146AFBEC82A4424 
        foreign key (C_AGT) 
        references R_AGT;

    alter table R_AGT_SKILL 
        add constraint FKCFB283F3C82A4424 
        foreign key (C_AGT) 
        references R_AGT;

    alter table R_AGT_SKILL 
        add constraint FKCFB283F3E13623B3 
        foreign key (C_SKILL) 
        references R_SKILL;

    create sequence SEQ_ADDR_ID;

    create sequence SEQ_AGT_ID;

    create sequence SEQ_SKILL_ID;
