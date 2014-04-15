
    create table ADDRESS (
        ADDRESS_ID int8 not null,
        CITY varchar(40) not null,
        COUNTRY varchar(40) not null,
        STATE varchar(2) not null,
        STREET varchar(40) not null,
        ZIP varchar(5) not null,
        AGENT int8 not null,
        primary key (ADDRESS_ID)
    );

    create table AGENT (
        AGENT_ID int8 not null,
        CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
        MODIFIED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
        FIRSTNAME varchar(40) not null,
        LASTNAME varchar(40) not null,
        MIDDLENAME varchar(40),
        START_DATE date not null,
        primary key (AGENT_ID)
    );

    create table AGENT_DETAIL (
        AGENT_DETAIL_ID int8 not null,
        DESCRIPTION varchar(255),
        primary key (AGENT_DETAIL_ID)
    );

    create table AGT_SKILL (
        AGENT int8 not null,
        SKILL int8 not null,
        primary key (AGENT, SKILL)
    );

    create table EMPLOYEE (
        EMPNUM int8 not null,
        BIRTHDATE date not null,
        FIRSTNAME varchar(40) not null,
        GENDER varchar(255) not null,
        HIREDATE date not null,
        LASTNAME varchar(40) not null,
        primary key (EMPNUM)
    );

    create table PHONE (
        ID int8 not null,
        AGENT int8,
        PHONENUM varchar(40) not null,
        primary key (ID)
    );

    create table SKILL (
        SKILL_ID int8 not null,
        CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
        MODIFIED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
        SKILLNAME varchar(40) not null unique,
        primary key (SKILL_ID)
    );

    alter table ADDRESS 
        add constraint FKE66327D460AC6D2D 
        foreign key (AGENT) 
        references AGENT;

    alter table AGENT_DETAIL 
        add constraint FK737DA2EB58D25137 
        foreign key (AGENT_DETAIL_ID) 
        references AGENT;

    alter table AGT_SKILL 
        add constraint FK773047A062AB7BC5 
        foreign key (SKILL) 
        references SKILL;

    alter table AGT_SKILL 
        add constraint FK773047A060AC6D2D 
        foreign key (AGENT) 
        references AGENT;

    alter table PHONE 
        add constraint FK489454E60AC6D2D 
        foreign key (AGENT) 
        references AGENT;

    create sequence SEQ_ADDR_ID start 100 increment 5;

    create sequence SEQ_AGT_ID start 100 increment 5;

    create sequence SEQ_EMP_ID start 100 increment 5;

    create sequence SEQ_PHONE_ID start 1 increment 5;

    create sequence SEQ_SKILL_ID start 100 increment 5;
