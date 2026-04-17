/*
create database Frikilandia;
use Frikilandia;

create table Trabajador(
    Cod_T int primary key,
    nombre varchar(50),
    tipo enum('Admin', 'Trabajador'),
    contrasenia varchar(50) not null);

create table Categoria(
    id_categoria int primary key,
    nom_c varchar(10));

create table Producto(
    cod_P int primary key,
    precio float,
    descripcion varchar(50),
    tipo enum('Cartas', 'Comics'),
    id_categoria int,
    foreign key (id_categoria) references Categoria(id_categoria) on delete cascade on update cascade);

create table Cliente(
    DNI char(9) primary key,
    nombre varchar(10),
    telefono int,
    dinero float,
    contrasenia varchar(50) not null);

create table Compra(
    fecha date,
    cantidad int,
    cod_P int,
    DNI char(9),
    foreign key (cod_P) references Producto(cod_P) on delete cascade on update cascade,
    foreign key (DNI) references Cliente(DNI) on delete cascade on update cascade);
    
Create table Carta(
	id_C varchar(10) primary key,
    nombre varchar(100) not null,
    tipo enum('Pokemon', 'Magic', 'One Piece') not null,
    precio float not null
);

-- Insertar trabajadores
INSERT INTO Trabajador (Cod_T, nombre, tipo, contrasenia) VALUES
(1, 'Mario', 'Admin', 'admin123'),
(2, 'Luigi', 'Trabajador', 'luigi456'),
(3, 'Peach', 'Trabajador', 'peach789');

-- Insertar categorías
INSERT INTO Categoria (id_categoria, nom_c) VALUES
(1, 'Cartas'),
(2, 'Comics');

-- Insertar productos
INSERT INTO Producto (cod_P, precio, descripcion, tipo, id_categoria) VALUES
(101, 5.99, 'Sobre Pokemon', 'Cartas', 1),
(102, 7.50, 'Sobre Magic', 'Cartas', 1),
(103, 4.99, 'Sobre One Piece', 'Cartas', 1),
(201, 12.99, 'Comic Spiderman #1', 'Comics', 2),
(202, 15.50, 'Comic Batman #50', 'Comics', 2);

-- Insertar clientes
INSERT INTO Cliente (DNI, nombre, telefono, contrasenia) VALUES
('12345678A', 'Ekain', 666123456, 'clave123'),
('23456789B', 'Aitor', 677987654, 'pass456'),
('34567890C', 'Iker', 688654321, 'miClave789');

-- Insertar compras
INSERT INTO Compra (fecha, cantidad, cod_P, DNI) VALUES
('2026-03-01', 2, 101, '12345678A'),  -- Ekain compra 2 cartas Pokémon
('2026-03-02', 1, 201, '23456789B'),  -- Aitor compra 1 comic Spiderman
('2026-03-03', 3, 102, '12345678A'),  -- Ekain compra 3 cartas Magic
('2026-03-04', 1, 202, '34567890C');  -- Iker compra 1 comic Batman

DELIMITER $$

CREATE FUNCTION totalGastadoCliente(dni_cliente CHAR(9))
RETURNS FLOAT
DETERMINISTIC
BEGIN
    DECLARE total FLOAT;

    SELECT SUM(c.cantidad * p.precio)
    INTO total
    FROM Compra c
    JOIN Producto p ON c.cod_P = p.cod_P
    WHERE c.DNI = dni_cliente;

    RETURN IFNULL(total, 0);
END $$

DELIMITER ;

DELIMITER $$

CREATE PROCEDURE historialComprasCliente(IN dni_cliente CHAR(9))
BEGIN
    SELECT 
        c.fecha,
        p.descripcion,
        c.cantidad,
        p.precio,
        (c.cantidad * p.precio) AS total
    FROM Compra c
    JOIN Producto p ON c.cod_P = p.cod_P
    WHERE c.DNI = dni_cliente;
END $$

DELIMITER ;


DELIMITER //
CREATE PROCEDURE MEDIA_Y_MAS_CARO()
BEGIN
    DECLARE v_fin INT DEFAULT 0;
    DECLARE v_precio FLOAT;
    DECLARE v_nombre VARCHAR(50);
    DECLARE v_suma FLOAT DEFAULT 0;
    DECLARE v_contador INT DEFAULT 0;
    DECLARE v_max FLOAT DEFAULT 0;
    DECLARE v_nombre_max VARCHAR(50) DEFAULT 'ninguno';
    DECLARE v_media FLOAT DEFAULT 0;
    DECLARE cur CURSOR FOR
        SELECT precio, descripcion FROM Producto;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_fin = 1;

    OPEN cur;
    BUCLE: LOOP
        FETCH cur INTO v_precio, v_nombre;
        IF v_fin = 1 THEN LEAVE BUCLE; END IF;
        SET v_suma = v_suma + v_precio;
        SET v_contador = v_contador + 1;
        IF v_precio > v_max THEN
            SET v_max = v_precio;
            SET v_nombre_max = v_nombre;
        END IF;
    END LOOP;
    CLOSE cur;

    IF v_contador > 0 THEN
        SET v_media = v_suma / v_contador;
    END IF;

    SELECT 
        v_media AS MEDIA,
        v_nombre_max AS PRODUCTO_MAS_CARO,
        v_max AS PRECIO_MAXIMO,
        IF(v_max > 10, 'Premium', 'No Premium') AS CLASIFICACION;
END //
DELIMITER ;



CALL MEDIA_Y_MAS_CARO();


DROP FUNCTION IF EXISTS StockValorTotal;

DELIMITER //
CREATE FUNCTION StockValorTotal()
RETURNS FLOAT
DETERMINISTIC
BEGIN
    DECLARE v_total FLOAT DEFAULT 0;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        RETURN -1;

    SELECT SUM(p.precio * c.cantidad) INTO v_total
    FROM Producto p
    JOIN Compra c ON p.cod_P = c.cod_P;

    RETURN v_total;
END //
DELIMITER ;

SELECT StockValorTotal() AS VALOR_TOTAL_VENDIDO;


DELIMITER //
CREATE PROCEDURE RegistrarCompra(
    IN p_DNI CHAR(9),
    IN p_codP INT,
    IN p_cantidad INT
)
BEGIN
    IF EXISTS (SELECT 1 FROM Cliente WHERE DNI = p_DNI) 
       AND EXISTS (SELECT 1 FROM Producto WHERE cod_P = p_codP) THEN
        
        INSERT INTO Compra(fecha, cantidad, cod_P, DNI)
        VALUES (CURDATE(), p_cantidad, p_codP, p_DNI);
        
    ELSE
        SELECT 'Cliente o producto no existe' AS ERROR;
    END IF;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE IngresosPorCategoria()
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        SELECT 'Error al calcular ingresos por categoría' AS ERROR;
    END;

    SELECT 
        c.nom_c AS CATEGORIA,
        SUM(p.precio * co.cantidad) AS INGRESOS_TOTALES
    FROM Categoria c
    JOIN Producto p ON c.id_categoria = p.id_categoria
    JOIN Compra co ON p.cod_P = co.cod_P
    GROUP BY c.id_categoria;
    
END //
DELIMITER ;

DELIMITER //
CREATE FUNCTION NumProductosCategoria(p_id_categoria INT)
RETURNS INT
DETERMINISTIC
BEGIN
    DECLARE total INT DEFAULT 0;

    DECLARE EXIT HANDLER FOR SQLEXCEPTION
        RETURN -1;

    SELECT COUNT(*) INTO total
    FROM Producto
    WHERE id_categoria = p_id_categoria;

    RETURN total;
END //
DELIMITER ;

UPDATE Carta
SET nombre = 'OP-002 Makino'
WHERE id_C = 'OP-159';

UPDATE Cliente
SET dinero = dinero + 900
WHERE DNI = '79227927B';

SELECT totalGastadoCliente('79227927B');

CALL historialComprasCliente('79227927B');
*/

use Frikilandia;