-- Insercion del usuario administrador por defecto
-- Password: admin123
INSERT INTO Usuarios (username, password, email, rol, estado) 
VALUES ('admin', '$2a$10$6TxEOAev32nRNX8yQaEIwO/iN2lzZzRx602EfnUux6XABxR0CcWua', 'admin@kinal.edu.gt', 'ROLE_ADMIN', 1)
ON DUPLICATE KEY UPDATE username = username;
