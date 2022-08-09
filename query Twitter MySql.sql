DROP DATABASE IF EXISTS twitter;
CREATE DATABASE twitter;
USE twitter;

DROP TABLE IF EXISTS usuario;
CREATE TABLE usuario(
id INT auto_increment PRIMARY KEY,
nombre varchar(20) NOT NULL UNIQUE,
clave varchar(20) NOT NULL);


DROP TABLE IF EXISTS publicaciones;
CREATE TABLE publicaciones(
idUsuario INT,
FOREIGN KEY(idUsuario) REFERENCES usuario(id),
id INT auto_increment PRIMARY KEY,
publicacion varchar(280),
fecha DATETIME);

DROP TABLE IF EXISTS publicacionesXhashtags;
CREATE TABLE publicacionesXhashtags(
idPublicacionHashtag int auto_increment primary key,
idPublicacion int,
idUsuario int,
hashtag varchar(100),
FOREIGN KEY (idPublicacion) REFERENCES publicaciones(id),
FOREIGN KEY (idUsuario) REFERENCES usuario(id));

DROP VIEW IF EXISTS hashtagsPopulares;
CREATE VIEW hashtagsPopulares
AS
select hashtag from publicacionesXhashtags 
group by hashtag
order by count(hashtag) desc
limit 10;

DROP VIEW IF EXISTS vistaPublicacion;
CREATE VIEW vistaPublicacion
AS
select p.id, u.nombre, p.publicacion, p.fecha
FROM publicaciones as p
inner join usuario as u
on u.id = p.idUsuario;

DROP PROCEDURE IF EXISTS verificarUsuario;
DELIMITER &&
CREATE PROCEDURE verificarUsuario(in nUsuario varchar(10))
BEGIN
	SELECT nombre FROM usuario WHERE nombre = nUsuario;
END &&

DROP PROCEDURE IF EXISTS verificarUsuarioYClave;
DELIMITER &&
CREATE PROCEDURE verificarUsuarioYClave(in nUsuario varchar(10), in pass varchar(10))
BEGIN
	SELECT nombre,clave FROM usuario WHERE nombre = nUsuario AND clave = pass;
END &&

DROP PROCEDURE IF EXISTS registrarUsuario;
DELIMITER &&
CREATE PROCEDURE registrarUsuario(in nUsuario varchar(10), in pass varchar(10))
BEGIN
	INSERT INTO usuario(nombre,clave) VALUES(nUsuario,pass);
END &&


DROP PROCEDURE IF EXISTS escribirPost;
DELIMITER &&
CREATE PROCEDURE escribirPost(in id int, in post varchar(180))
BEGIN
	INSERT INTO publicaciones(idUsuario, publicacion, fecha)
    VALUES(id,post,now());
END &&

DROP PROCEDURE IF EXISTS registrarHashtag;
DELIMITER &&
CREATE PROCEDURE registrarHashtag(in idPost int, in tag varchar(100))
BEGIN
	INSERT INTO publicacionesXhashtags(idPublicacion, hashtag)
    VALUES(idPost,tag);
END &&

DROP PROCEDURE IF EXISTS buscarUsuario;
DELIMITER &&
CREATE PROCEDURE buscarUsuario(in nombreUsuario varchar(20))
BEGIN
	SELECT nombre, publicacion, fecha FROM vistaPublicacion
    WHERE nombre LIKE CONCAT('%',nombreUsuario,'%');
END &&

DROP PROCEDURE IF EXISTS buscarHashtag;
DELIMITER &&
CREATE PROCEDURE buscarHashtag(in hashtag varchar(100))
BEGIN
	SELECT DISTINCT(u.nombre), p.publicacion, p.fecha
	FROM publicacionesXhashtags AS ph
	INNER JOIN publicaciones AS p
	ON p.id = ph.idPublicacion
	INNER JOIN usuario AS u
	ON u.id = p.idUsuario
	WHERE ph.hashtag LIKE CONCAT('%', hashtag, '%');
END &&

DROP PROCEDURE IF EXISTS insertarHashtags;
DELIMITER &&
CREATE PROCEDURE insertarHashtags(in idPost int, in idU int , in tag varchar(100))
BEGIN
	INSERT INTO publicacionesXhashtags(idPublicacion, idUsuario, hashtag)
    VALUES(idPost,idU,tag);
END &&

