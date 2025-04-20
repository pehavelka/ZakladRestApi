--liquibase formatted sql

-- ChangeSet petr.havelka:1 runAlways:true

DROP TABLE IF EXISTS roleuser;
DROP TABLE IF EXISTS roleuser_arch;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS users_arch;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS roles_arch;
DROP TABLE IF EXISTS tab1;

CREATE TABLE roles (
	id serial NOT NULL,
	roledescription varchar(255) NOT NULL,
	rolename varchar(255) NOT NULL,
	platnost bool NOT NULL DEFAULT true,
	zmenacas timestamp(6) NOT NULL DEFAULT now(),
	zmenauzivatel varchar(255) NOT NULL DEFAULT current_user,
	CONSTRAINT roles_pkey PRIMARY KEY (id),
	CONSTRAINT rolename_u1 UNIQUE (rolename)
);

CREATE TABLE roles_arch (
	idarch serial not null,
	id int4 NOT NULL,
	roledescription varchar(255) NOT NULL,
	rolename varchar(255) NOT NULL,
	platnost bool NOT NULL,
	zmenacas timestamp(6) NOT NULL,
	zmenauzivatel varchar(255) NOT NULL,
	CONSTRAINT roles_arch_pkey PRIMARY KEY (idarch)
);

  
CREATE TABLE users (
	id serial NOT NULL,
	email varchar(100) NOT NULL,
	full_name varchar(255) NOT NULL,
	userpassword varchar(255),
	platnost bool NOT NULL DEFAULT true,
	zmenacas timestamp(6) NOT NULL DEFAULT now(),
	zmenauzivatel varchar(255) NOT NULL DEFAULT current_user,
	CONSTRAINT users_u1 UNIQUE (email),
	CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE users_arch (
	idarch serial not null,
	id int4 NOT NULL,
	email varchar(100) NOT NULL,
	full_name varchar(255) NOT NULL,
	userpassword varchar(255),
	platnost bool NOT NULL,
	zmenacas timestamp(6) NOT NULL,
	zmenauzivatel varchar(255) NOT NULL DEFAULT current_user,
	CONSTRAINT users_arch_pkey PRIMARY KEY (idarch)
);

CREATE TABLE roleuser (
	id serial NOT NULL,
	role_id int4 NOT NULL,
	user_id int4 NOT NULL,
	platnost bool NOT NULL DEFAULT true,
	zmenacas timestamp(6) NOT NULL DEFAULT now(),
	zmenauzivatel varchar(255) NOT NULL DEFAULT current_user,
	CONSTRAINT roleuser_pkey PRIMARY KEY (id)
);
ALTER TABLE roleuser ADD CONSTRAINT roleuser_f1 FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE roleuser ADD CONSTRAINT roleuser_f2 FOREIGN KEY (role_id) REFERENCES roles(id);

CREATE TABLE roleuser_arch (
	idarch serial not null,
	id int4 NOT NULL,
	role_id int4 NOT NULL,
	user_id int4 NOT NULL,
	platnost bool NOT NULL,
	zmenacas timestamp(6) NOT NULL,
	zmenauzivatel varchar(255) NOT NULL DEFAULT current_user,
	CONSTRAINT roleuser_arch_pkey PRIMARY KEY (idarch)
);

CREATE TABLE tab1 (
	id serial NOT NULL,
	celejmeno varchar(255) NULL,
	platnost bool NOT NULL DEFAULT true,
	zmenacas timestamp(6) NOT NULL DEFAULT now(),
	zmenauzivatel varchar(255) NOT NULL DEFAULT current_user,
	CONSTRAINT tab1_pkey PRIMARY KEY (id)
);


-- ChangeSet petr.havelka:2 runAlways:true splitStatements:false
CREATE OR REPLACE FUNCTION archive_roles_changes()
RETURNS TRIGGER AS $$
BEGIN
    -- Vložení původních hodnot (OLD) do tabulky roles_arch
    INSERT INTO roles_arch (id, roledescription, rolename, platnost, zmenacas, zmenauzivatel)
    VALUES (OLD.id, OLD.roledescription, OLD.rolename, OLD.platnost, OLD.zmenacas, OLD.zmenauzivatel);
    
    NEW.zmenacas := NOW();
    NEW.zmenauzivatel := SESSION_USER;
    
    -- Vrátí nový řádek (pro standardní UPDATE operaci)
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- ChangeSet petr.havelka:3 runAlways:true splitStatements:false
CREATE TRIGGER roles_update_trigger
BEFORE UPDATE ON roles
FOR EACH ROW
EXECUTE FUNCTION archive_roles_changes();


-- ChangeSet petr.havelka:4 runAlways:true splitStatements:false
CREATE OR REPLACE FUNCTION archive_users_changes()
RETURNS TRIGGER AS $$
BEGIN
    -- Vložení původních hodnot (OLD) do tabulky users_arch
    INSERT INTO users_arch (id, email, full_name, userpassword, platnost, zmenacas, zmenauzivatel)
    VALUES (OLD.id, OLD.email, OLD.full_name, OLD.userpassword, OLD.platnost, OLD.zmenacas, OLD.zmenauzivatel);
    
    NEW.zmenacas := NOW();
    NEW.zmenauzivatel := SESSION_USER;
    
    -- Vrátí nový řádek (pro standardní UPDATE operaci)
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- ChangeSet petr.havelka:5 runAlways:true splitStatements:false
CREATE TRIGGER users_update_trigger
BEFORE  UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION archive_users_changes();

-- ChangeSet petr.havelka:6 runAlways:true splitStatements:false
CREATE OR REPLACE FUNCTION archive_roleuser_changes()
RETURNS TRIGGER AS $$
BEGIN
    -- Vložení původních hodnot (OLD) do tabulky roleuser_arch
    INSERT INTO roleuser_arch (id, role_id, user_id, platnost, zmenacas, zmenauzivatel)
    VALUES (OLD.id, OLD.role_id, OLD.user_id, OLD.platnost, OLD.zmenacas, OLD.zmenauzivatel);
    
    NEW.zmenacas := NOW();
    NEW.zmenauzivatel := SESSION_USER;
    
    -- Vrátí nový řádek (pro standardní UPDATE operaci)
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- ChangeSet petr.havelka:7 runAlways:true splitStatements:false
CREATE TRIGGER roleuser_update_trigger
BEFORE UPDATE ON roleuser
FOR EACH ROW
EXECUTE FUNCTION archive_roleuser_changes();
