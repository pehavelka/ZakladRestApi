--liquibase formatted sql

-- ChangeSet petr.havelka:1 runAlways:true
INSERT INTO roles (rolename, roledescription) VALUES	
	('USER', 'Standardní uživatelská role'),
	('ADMIN', 'Administrátorská role'),
	('SUPER_ADMIN', 'Super Administrátorská role');

-- userpassword: password123
INSERT INTO users (email,full_name,userpassword) VALUES
	('user@example.com','Uživatel','$2a$10$LU/D/PK8372N.uI7IYdS4eaiUIfhbiQPcq8.k.Y3LJXce7WOgR0ty'),
	('admin@example.com','Admininstrátor','$2a$10$LU/D/PK8372N.uI7IYdS4eaiUIfhbiQPcq8.k.Y3LJXce7WOgR0ty'),
	('super.admin@email.com','Super Administrátor','$2a$10$aBY11XrYM0KNhHQpm5k/XupHlvmWNOYBi6uGXEtcxwwQGeaFOE7ra');
	 

INSERT INTO roleuser (user_id, role_id) VALUES
	((SELECT id FROM users WHERE email = 'user@example.com'), (SELECT id FROM roles WHERE rolename = 'USER')),
	((SELECT id FROM users WHERE email = 'admin@example.com'), (SELECT id FROM roles WHERE rolename = 'ADMIN')),
	((SELECT id FROM users WHERE email = 'super.admin@email.com'), (SELECT id FROM roles WHERE rolename = 'SUPER_ADMIN'));
	