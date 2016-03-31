/*
 * MYSQL script.
 * Create the database schema for the application.
 */


DROP TABLE IF EXISTS `Greeting`;

CREATE TABLE Greeting (
  id BIGINT NOT NULL AUTO_INCREMENT,
  text VARCHAR(100) NOT NULL,
  PRIMARY KEY(id)
);

DROP TABLE IF EXISTS `AccountRole`;
DROP TABLE IF EXISTS `Account`;
DROP TABLE IF EXISTS `Role`;

CREATE TABLE Account (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(100) NOT NULL,
  password VARCHAR(200) NOT NULL,
  enabled BOOLEAN DEFAULT true NOT NULL,
  credentialsexpired BOOLEAN DEFAULT false NOT NULL,
  expired BOOLEAN DEFAULT false NOT NULL,
  locked BOOLEAN DEFAULT false NOT NULL,
  version INT NOT NULL,
  createdBy VARCHAR(100) NOT NULL,
  createdAt DATETIME NOT NULL,
  updatedBy VARCHAR(100) DEFAULT NULL,
  updatedAt DATETIME DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT UQ_Account_Username UNIQUE (username)
);

CREATE TABLE Role (
  id BIGINT NOT NULL,
  code VARCHAR(50) NOT NULL,
  label VARCHAR(100) NOT NULL,
  ordinal INT NOT NULL,
  effectiveAt DATETIME NOT NULL,
  expiresAt DATETIME DEFAULT NULL,
  createdAt DATETIME NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT UQ_Role_Code UNIQUE (code)
);

CREATE TABLE AccountRole (
  accountId BIGINT NOT NULL,
  roleId BIGINT NOT NULL,
  PRIMARY KEY (accountId, roleId),
  CONSTRAINT FK_AccountRole_AccountId FOREIGN KEY (accountId) REFERENCES Account (id),
  CONSTRAINT FK_AccountRole_RoleId FOREIGN KEY (roleId) REFERENCES Role (id)
);
