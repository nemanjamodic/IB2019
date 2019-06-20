insert into authority (name) values ("regular");
insert into authority (name) values ("admin");

insert into users (email, password, certificate, active, authority) values ("vai@gmail.com", "pass", "cert.jks", true, 2);
insert into users (email, password, certificate, active, authority) values ("nemza@gmail.com", "pass", "cert.jks", true, 1);