-- creates the table 'users' and stores
-- usernames and passwords
CREATE TABLE users (
	username varchar(30) NOT NULL,
	encrypted_password varbinary(30)
);

-- alters the table so that username is primary key
ALTER TABLE users
ADD CONSTRAINT PK_username PRIMARY KEY (username);