-- ===========================
-- Script SQL - Famisanar_Tienda_Jimy_Peña
-- ===========================

-- Tabla Producto
CREATE TABLE Producto (
    id_producto       BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre            VARCHAR(100) NOT NULL,
    tipo              VARCHAR(20) NOT NULL, -- PAPELERIA | SUPERMERCADO | DROGUERIA
    precio_base       DECIMAL(12,2) NOT NULL,
    iva               DECIMAL(5,2) NOT NULL,
    stock_actual      INT NOT NULL,
    umbral_pedido     INT NOT NULL,
    vendidas          INT DEFAULT 0
);

-- Tabla Venta
CREATE TABLE Venta (
    id_venta  BIGINT PRIMARY KEY AUTO_INCREMENT,
    fecha     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabla VentaItem (detalle de cada venta)
CREATE TABLE VentaItem (
    id_item           BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_venta          BIGINT NOT NULL,
    id_producto       BIGINT NOT NULL,
    cantidad          INT NOT NULL,
    precio_final_unit DECIMAL(12,2) NOT NULL,
    subtotal          DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (id_venta) REFERENCES Venta(id_venta),
    FOREIGN KEY (id_producto) REFERENCES Producto(id_producto)
);

-- Tabla PedidoProveedor (opcional)
CREATE TABLE PedidoProveedor (
    id_pedido    BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_producto  BIGINT NOT NULL,
    fecha        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    cantidad     INT NOT NULL,
    estado       VARCHAR(20) NOT NULL, -- SOLICITADO | RECIBIDO
    FOREIGN KEY (id_producto) REFERENCES Producto(id_producto)
);
