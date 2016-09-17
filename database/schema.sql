use @SQL_DB_NAME@;

CREATE TABLE users(
  user_id INTEGER PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(64) NOT NULL,
  email VARCHAR(64) NOT NULL,
  phoneNum VARCHAR(10),
  password BINARY(64)
);

CREATE TABLE events(
  event_id INTEGER PRIMARY KEY AUTO_INCREMENT,
  eventname VARCHAR(64) NOT NULL,
  location VARCHAR(64),
  goTime varchar(64),
  price VARCHAR(64),
  description VARCHAR(512),
  user_id INTEGER REFERENCES users(user_id)
);

CREATE TABLE userInterests(
  user_id INTEGER REFERENCES users(user_id),
  interest VARCHAR(64)
);

CREATE TABLE eventInterests (
  event_id INTEGER REFERENCES events(event_id),
  interest VARCHAR(64)
);

CREATE TABLE attendance(
  event_id INTEGER REFERENCES events(event_id),
  user_id INTEGER REFERENCES users(user_id)
);