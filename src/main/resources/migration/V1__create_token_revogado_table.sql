create table if not exists token_revogado (
  id uuid primary key,
  created_at timestamp with time zone not null,
  updated_at timestamp with time zone not null,
  version bigint not null,
  jti varchar(255) not null,
  revogado_em timestamp with time zone not null
);

create unique index if not exists ux_token_revogado_jti on token_revogado (jti);
create index if not exists idx_token_revogado_revogado_em on token_revogado (revogado_em);
