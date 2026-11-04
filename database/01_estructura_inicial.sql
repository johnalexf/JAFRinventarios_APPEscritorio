
CREATE TABLE Empresa (
   id_empresa INT PRIMARY KEY AUTO_INCREMENT,
   nombre_empresa VARCHAR(100) NOT NULL,  
   codigo_registro_usuario_vendedor VARCHAR(10) 
);

CREATE TABLE Roles (
    id_rol INT PRIMARY KEY AUTO_INCREMENT, 
    nombre_rol VARCHAR(30) NOT NULL 
);

CREATE TABLE Usuarios(
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    id_empresa INT NOT NULL,
    alias_usuario VARCHAR(20) NOT NULL UNIQUE,
    telefono_usuario VARCHAR(10) NOT NULL UNIQUE,
    correo_usuario VARCHAR(100) NOT NULL UNIQUE,
    primer_nombre_usuario VARCHAR(30) NOT NULL,
    segundo_nombre_usuario VARCHAR(30),
    primer_apellido_usuario VARCHAR(30) NOT NULL,
    segundo_apellido_usuario VARCHAR(30),
    contrasena_usuario VARCHAR(100) NOT NULL,
    id_rol_usuario INT NOT NULL,
    habilitado TINYINT(1) NOT NULL DEFAULT 1,

    FOREIGN KEY (id_empresa) REFERENCES Empresa(id_empresa),
    FOREIGN KEY (id_rol_usuario) REFERENCES Roles(id_rol)
);

CREATE TABLE Clientes (
    id_cliente INT PRIMARY KEY AUTO_INCREMENT,
    nombre_negocio VARCHAR(150) NOT NULL UNIQUE,
    primer_nombre_contacto VARCHAR(30) NOT NULL,
    segundo_nombre_contacto VARCHAR(30),
    primer_apellido_contacto VARCHAR(30) NOT NULL,
    segundo_apellido_contacto VARCHAR(30),
    telefono_contacto VARCHAR(10) NOT NULL UNIQUE,
    direccion_cliente VARCHAR(150) NOT NULL,
    correo_cliente  VARCHAR(100) NOT NULL UNIQUE,
    habilitado TINYINT(1) NOT NULL DEFAULT 1

);

CREATE TABLE Proveedores(
    id_proveedor INT PRIMARY KEY AUTO_INCREMENT,
    nombre_comercial VARCHAR(150) NOT NULL UNIQUE,
    primer_nombre_contacto VARCHAR(30) NOT NULL,
    segundo_nombre_contacto VARCHAR(30),
    primer_apellido_contacto VARCHAR(30) NOT NULL,
    segundo_apellido_contacto VARCHAR(30),
    telefono_contacto VARCHAR(10) NOT NULL UNIQUE,
    direccion_proveedor VARCHAR(150) NOT NULL,
    correo_proveedor VARCHAR(100) NOT NULL UNIQUE,
    habilitado TINYINT(1) NOT NULL DEFAULT 1
);

CREATE TABLE Productos(
    id_producto INT PRIMARY KEY AUTO_INCREMENT,
    id_proveedor INT NOT NULL,
    nombre_producto VARCHAR(50) NOT NULL UNIQUE,
    precio_compra DECIMAL(10, 2) NOT NULL,
    precio_venta DECIMAL(10, 2) NOT NULL,
    cantidad_minima_stock INT(4) NOT NULL,
    cantidad_disponible INT(4) NOT NULL,
    habilitado TINYINT(1) NOT NULL DEFAULT 1,

    FOREIGN KEY (id_proveedor) REFERENCES Proveedores(id_proveedor)
);

CREATE TABLE Compras (
    id_compra INT PRIMARY KEY AUTO_INCREMENT,
    fecha_hora_compra DATETIME NOT NULL,
    total_compra DECIMAL(10, 2) NOT NULL,
    id_proveedor INT NOT NULL,
    id_usuario INT NOT NULL,

    FOREIGN KEY (id_proveedor) REFERENCES Proveedores(id_proveedor),
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

CREATE TABLE Detalle_de_compras(
    id_detalle_compra INT PRIMARY KEY AUTO_INCREMENT,
    id_compra INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad_producto INT(4) NOT NULL,
    precio_unitario_producto DECIMAL(10, 2) NOT NULL,
    precio_total_producto DECIMAL(10, 2) NOT NULL,

    FOREIGN KEY (id_compra) REFERENCES Compras(id_compra),
    FOREIGN KEY (id_producto) REFERENCES Productos(id_producto)
);

CREATE TABLE Ventas(
    id_venta INT PRIMARY KEY AUTO_INCREMENT,
    fecha_hora_venta DATETIME NOT NULL,
    total_venta DECIMAL(10, 2) NOT NULL, 
    id_cliente INT NOT NULL,
    id_usuario INT NOT NULL,

    FOREIGN KEY (id_cliente) REFERENCES Clientes(id_cliente),
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

CREATE TABLE Detalle_de_ventas(
    id_detalle_venta INT PRIMARY KEY AUTO_INCREMENT,
    id_venta INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad_producto INT(4) NOT NULL,
    precio_unitario_producto DECIMAL(10, 2) NOT NULL,
    precio_total_producto DECIMAL(10, 2) NOT NULL,

    FOREIGN KEY (id_venta) REFERENCES Ventas(id_venta),
    FOREIGN KEY (id_producto) REFERENCES Productos(id_producto)
);

INSERT INTO 
    Roles (id_rol, nombre_rol)
    VALUES
    (1, "Administrador"),
    (2, "Vendedor")