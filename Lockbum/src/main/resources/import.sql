insert into authority (name) values ("regular");
insert into authority (name) values ("admin");

insert into users (email, password, certificate, active, authority) values ("vai@gmail.com", "$2a$10$/DmhDfnGZO2tU4i8G1quF.zHDJN4i0nQVft5CwwdbmT2LJeFjzIUC", "cert.jks", true, 2);
insert into users (email, password, certificate, active, authority) values ("nemza@gmail.com", "$2a$10$/DmhDfnGZO2tU4i8G1quF.zHDJN4i0nQVft5CwwdbmT2LJeFjzIUC", "cert.jks", true, 1);