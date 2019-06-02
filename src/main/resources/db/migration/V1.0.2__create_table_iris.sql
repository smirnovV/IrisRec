create table iris
(
  id        bigint primary key,
  person_id bigint references person (id) on delete cascade,
  iris_code bytea
);

create sequence iris_sequence;