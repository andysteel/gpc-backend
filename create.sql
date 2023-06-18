alter table if exists gpc.contato drop constraint if exists FKqcaicb387g3yd3xfy44qravfh;
drop table if exists gpc.contato cascade;
drop table if exists gpc.pessoa cascade;
drop sequence if exists gpc.seq_contato;
drop sequence if exists gpc.seq_pessoa;
drop schema if exists gpc;
create schema gpc;
create sequence gpc.seq_contato start with 1 increment by 1;
create sequence gpc.seq_pessoa start with 1 increment by 1;
create table gpc.contato (
        versao integer,
        atualizado_em timestamp(6),
        criado_em timestamp(6) not null,
        id bigint not null,
        inativado_em timestamp(6),
        pessoa_id bigint not null,
        atualizado_por varchar(255),
        criado_por varchar(255) not null,
        email varchar(255) not null,
        inativado_por varchar(255),
        nome varchar(255) not null,
        status varchar(255) not null check (status in ('INATIVO','ATIVO')),
        telefone varchar(255) not null,
        primary key (id)
    );
create table gpc.pessoa (
        data_nascimento date not null,
        versao integer,
        atualizado_em timestamp(6),
        criado_em timestamp(6) not null,
        id bigint not null,
        inativado_em timestamp(6),
        cpf varchar(11) not null unique,
        atualizado_por varchar(255),
        criado_por varchar(255) not null,
        inativado_por varchar(255),
        nome varchar(255) not null,
        status varchar(255) not null check (status in ('INATIVO','ATIVO')),
        primary key (id)
    );
alter table if exists gpc.contato
       add constraint FKqcaicb387g3yd3xfy44qravfh
       foreign key (pessoa_id)
       references gpc.pessoa;