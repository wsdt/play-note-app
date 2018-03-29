# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table category (
  title                         varchar(255) not null,
  constraint pk_category primary key (title)
);

create table note (
  id                            integer auto_increment not null,
  category_title                varchar(255),
  title                         varchar(255),
  description                   varchar(255),
  last_edited                   integer not null,
  constraint pk_note primary key (id)
);

alter table note add constraint fk_note_category_title foreign key (category_title) references category (title) on delete restrict on update restrict;
create index ix_note_category_title on note (category_title);


# --- !Downs

alter table note drop constraint if exists fk_note_category_title;
drop index if exists ix_note_category_title;

drop table if exists category;

drop table if exists note;

