/*Crear nuevo esquema llamado inventario*/
CREATE DATABASE inventario_tienda;

/*Seleccionar esquema inventario*/
USE inventario_tienda;

/*Crear tabla Productos*/
CREATE TABLE Productos (
	codigo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(40),
    precio DECIMAL(20, 1),
    inventario INT
);

/*Insertar elementos a la tabla*/
INSERT INTO Productos VALUES (1, "Naranjas", 7000.0, 35);
INSERT INTO Productos VALUES (2, "Limones", 2500.0, 15);
INSERT INTO Productos VALUES (3, "Peras", 2700.0, 65);
INSERT INTO Productos VALUES (4, "Arandanos", 9300.0, 34);
INSERT INTO Productos VALUES (5, "Tomates", 2100.0, 42);
INSERT INTO Productos VALUES (6, "Fresas", 9100.0, 20);
INSERT INTO Productos VALUES (7, "Helado", 4500.0, 41);
INSERT INTO Productos VALUES (8, "Galletas", 500.0, 8);
INSERT INTO Productos VALUES (9, "Chocolates", 4500.0, 80);
INSERT INTO Productos VALUES (10, "Jamon", 17000.0, 48);

/*Recuperar todos los elementos de la tabla Productos*/
SELECT * FROM Productos;

DROP TABLE Productos;