--Create and fill test database script 
CREATE DATABASE test_rsocket_images TEMPLATE=template0 ENCODING='UTF8' LC_COLLATE='C' LC_CTYPE = 'ru_RU.UTF-8';

CONNECT TO test_rsocket_images;

CREATE TABLE images (
	id INT PRIMARY KEY,
	name VARCHAR(120),
	file_name VARCHAR(1024)
);

INSERT INTO images (id, name, file_name)
VALUES (1, '1', '1.jpg'), (2, '2', '2.jpg'), (3, '3', '3.jpg'),
(4, '4', '4.jpg'), (5, '5', '5.jpg'), (6, '6', '6.jpg'),
(7, '7', '7.jpg'), (8, '8', '8.jpg'), (9, '9', '9.jpg'),
(10, '10', '10.jpg'), (11, '11', '11.jpg'), (12, '12', '12.jpg'),
(13, '13', '13.jpg'), (14, '14', '14.jpg'), (15, '15', '15.jpg'),
(16, '16', '16.jpg'), (17, '17', '17.jpg'), (18, '18', '18.jpg'),
(19, '19', '19.jpg'), (20, '20', '20.jpg'), (21, '21', '21.jpg'), 
(22, '22', '22.jpg'), (23, '23', '23.jpg');
