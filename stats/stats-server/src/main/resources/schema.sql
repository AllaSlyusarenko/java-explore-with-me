create TABLE IF NOT EXISTS stats(
  id      BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  app     VARCHAR(100) NOT NULL,
  uri     VARCHAR(100) NOT NULL,
  ip      VARCHAR(50) NOT NULL,
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL
);