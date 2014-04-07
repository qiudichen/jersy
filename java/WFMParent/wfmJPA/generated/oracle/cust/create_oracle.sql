
    create table ADDRESS (
        ADDRESS_ID number(19,0) not null,
        CITY varchar2(40 char) not null,
        COUNTRY varchar2(40 char) not null,
        STATE varchar2(2 char) not null,
        STREET varchar2(40 char) not null,
        ZIP varchar2(5 char) not null,
        AGENT number(19,0) not null,
        primary key (ADDRESS_ID)
    );

    create table AGENT (
        AGENT_ID number(19,0) not null,
        CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
        FIRSTNAME varchar2(40 char) not null,
        LASTNAME varchar2(40 char) not null,
        MIDDLENAME varchar2(40 char) not null,
        START_DATE date not null,
        primary key (AGENT_ID)
    );

    create table AGT_SKILL (
        AGENT number(19,0) not null,
        SKILL number(19,0) not null
    );

    create table EMPLOYEE (
        EMPNUM number(19,0) not null,
        CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
        BIRTHDATE date not null,
        FIRSTNAME varchar2(40 char) not null,
        GENDER varchar2(255 char) not null,
        HIREDATE date not null,
        LASTNAME varchar2(40 char) not null,
        primary key (EMPNUM)
    );

    create table PHONE (
        ID number(19,0) not null,
        AGENT number(19,0) not null unique,
        PHONENUM varchar2(40 char) not null,
        primary key (ID)
    );

    create table SKILL (
        SKILL_ID number(19,0) not null,
        SKILLNAME varchar2(40 char) not null unique,
        primary key (SKILL_ID)
    );

    alter table ADDRESS 
        add constraint FKE66327D4279CA3F3 
        foreign key (AGENT) 
        references AGENT;

    alter table AGT_SKILL 
        add constraint FK773047A0299BB28B 
        foreign key (SKILL) 
        references SKILL;

    alter table AGT_SKILL 
        add constraint FK773047A0279CA3F3 
        foreign key (AGENT) 
        references AGENT;

    alter table PHONE 
        add constraint FK489454E279CA3F3 
        foreign key (AGENT) 
        references AGENT;

    create sequence SEQ_ADDR_ID;

    create sequence SEQ_AGT_ID;

    create sequence SEQ_EMP_ID;

    create sequence SEQ_PHONE_ID;

    create sequence SEQ_SKILL_ID;
