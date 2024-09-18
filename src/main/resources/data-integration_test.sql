INSERT INTO `card_cost` (`cost`, `country`) VALUES ('5.0', 'US');

INSERT INTO `role` (`description`, `name`) VALUES ('ADMINISTRATOR ROLE', 'ADMIN');
INSERT INTO `role` (`description`, `name`) VALUES ('USER ROLE', 'USER');

INSERT INTO `users` (`password`, `username`) VALUES ('$2a$10$rOsj3JBuoKvewZ/1rwSN0OQfqowAdzTN1ymMXwfZFLSysCrvHHw5q', 'admin');
INSERT INTO `users` (`password`, `username`) VALUES ('$2a$10$rOsj3JBuoKvewZ/1rwSN0OQfqowAdzTN1ymMXwfZFLSysCrvHHw5q', 'user');

INSERT INTO `users_role` (`id_user`, `id_role`) VALUES (1, 1);
INSERT INTO `users_role` (`id_user`, `id_role`) VALUES (1, 2);
INSERT INTO `users_role` (`id_user`, `id_role`) VALUES (2, 2);