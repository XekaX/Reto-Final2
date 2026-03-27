drop database frikilandia;
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
    nombre varchar(40) not null,
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
