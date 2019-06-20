insert into authority (name) values ("regular");
insert into authority (name) values ("admin");

insert into users (email, password, certificate, active, authority) values ("vai@gmail.com", "$2a$10$j4Q0v8wxz1finWEksndsLOF0dhQDojMi/RSCA0q.yihiDweUvLUtm", "cert.jks", true, 2);
insert into users (email, password, certificate, active, authority) values ("nemza@gmail.com", "$2a$10$j4Q0v8wxz1finWEksndsLOF0dhQDojMi/RSCA0q.yihiDweUvLUtm", "cert.jks", true, 1);