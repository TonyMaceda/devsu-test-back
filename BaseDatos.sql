CREATE TABLE persona (
                         id BIGSERIAL PRIMARY KEY,
                         nombre VARCHAR(100) NOT NULL,
                         genero VARCHAR(20) NOT NULL,
                         edad INT NOT NULL,
                         identificacion VARCHAR(20) NOT NULL UNIQUE,
                         direccion VARCHAR(200) NOT NULL,
                         telefono VARCHAR(20) NOT NULL
);

CREATE TABLE cliente (
                         id BIGINT PRIMARY KEY REFERENCES persona(id),
                         cliente_id VARCHAR(20) NOT NULL UNIQUE,
                         contrasena VARCHAR(100) NOT NULL,
                         estado BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE cuenta (
                        numero_cuenta BIGINT PRIMARY KEY,
                        tipo_cuenta VARCHAR(20) NOT NULL,
                        saldo_inicial NUMERIC(15,2) NOT NULL,
                        estado BOOLEAN NOT NULL DEFAULT TRUE,
                        cliente_id BIGINT NOT NULL REFERENCES cliente(id)
);

CREATE TABLE movimiento (
                            id BIGSERIAL PRIMARY KEY,
                            fecha DATE NOT NULL,
                            tipo_movimiento VARCHAR(20) NOT NULL,
                            valor NUMERIC(15,2) NOT NULL,
                            saldo_disponible NUMERIC(15,2) NOT NULL,
                            cuenta_numero BIGINT NOT NULL REFERENCES cuenta(numero_cuenta)
);

-- Datos de prueba casos de uso
INSERT INTO persona (nombre, genero, edad, identificacion, direccion, telefono) VALUES
        ('Jose Lema', 'M', 30, '1234567890', 'Otavalo sn y principal', '098254785'),
        ('Marianela Montalvo', 'F', 28, '1234567891', 'Amazonas y NNUU', '097548965'),
        ('Juan Osorio', 'M', 35, '1234567892', '13 junio y Equinoccial', '098874587');

INSERT INTO cliente (id, cliente_id, contrasena, estado) VALUES
        (1, 'CL001', '1234', TRUE),
        (2, 'CL002', '5678', TRUE),
        (3, 'CL003', 'password789', TRUE);