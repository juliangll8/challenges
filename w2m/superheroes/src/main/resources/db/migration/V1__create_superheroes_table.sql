CREATE TABLE superheroes (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  name_lowercased VARCHAR(255) NOT NULL,
  superpower VARCHAR(255),
  strength BIGINT NOT NULL,
  active BOOLEAN NOT NULL
);
