// ============================================================
// SEED MongoDB — Sistema de Facturación
// Ejecutar: mongosh "mongodb://localhost:27017/proyecto" ops/seed.js
// ============================================================

// --- Limpiar colecciones previas ---
db.personas.drop();
db.usuarios.drop();
db.clientes.drop();
db.empleados.drop();
db.productos.drop();
db.tipo_documentos.drop();
db.medios_pago.drop();
db.ventas.drop();

// --- Personas ---
db.personas.insertMany([
  { _id: "PER-0001", nombre: "Juan", apellido: "Pérez", fecha_nacimiento: null, correo: "juan@correo.com", telefono: "987654321", usuario: "juanp" },
  { _id: "PER-0002", nombre: "Ana", apellido: "Gómez", fecha_nacimiento: null, correo: "ana@correo.com", telefono: "912345678", usuario: "anag" }
]);

// --- Usuarios (con hash SHA-256 real de "123456") ---
// hash = SeguridadContrasena.hash("123456")
db.usuarios.insertMany([
  { _id: "juanp", contrasena: "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3", id_persona: "PER-0001" },
  { _id: "anag", contrasena: "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3", id_persona: "PER-0002" }
]);

// --- Clientes ---
db.clientes.insertMany([
  { _id: "CLI-0001", nombre: "Juan", apellido: "Pérez", telefono: "987654321", contrasena: "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3", correo: "juan@correo.com" },
  { _id: "CLI-0002", nombre: "Ana", apellido: "Gómez", telefono: "912345678", contrasena: "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3", correo: "ana@correo.com" }
]);

// --- Empleados ---
db.empleados.insertMany([
  { _id: "EMP-0001", nombre: "María", apellido: "López", cargo: "Vendedor", telefono: "912345678" },
  { _id: "EMP-0002", nombre: "Carlos", apellido: "Ramírez", cargo: "Cajero", telefono: "955112233" }
]);

// --- Productos ---
db.productos.insertMany([
  { _id: 1, nombre: "Mouse Gamer", stock: 8, precio: 50.0, categoria: "Accesorios" },
  { _id: 2, nombre: "Teclado Mecánico", stock: 5, precio: 120.0, categoria: "Accesorios" },
  { _id: 3, nombre: "Webcam HD", stock: 0, precio: 75.0, categoria: "Accesorios" }
]);

// --- Tipos de documento ---
db.tipo_documentos.insertMany([
  { _id: "DOC-01", nombre: "Boleta" },
  { _id: "DOC-02", nombre: "Factura" }
]);

// --- Medios de pago ---
db.medios_pago.insertMany([
  { _id: "MP001", descripcion: "Efectivo" },
  { _id: "MP002", descripcion: "Tarjeta de crédito" },
  { _id: "MP003", descripcion: "Tarjeta de débito" },
  { _id: "MP004", descripcion: "Yape" },
  { _id: "MP005", descripcion: "Plin" },
  { _id: "MP006", descripcion: "Transferencia" }
]);

// --- Ventas (con detalles embebidos) ---
db.ventas.insertMany([
  {
    _id: "v0001",
    id_persona: "PER-0001",
    id_tipo_documento: "DOC-01",
    numero_documento: "BOL-000001",
    id_medio_pago: "MP001",
    fecha_emision: new Date(),
    total: 100.0,
    detalles: [
      { id_producto: 1, producto: "Mouse Gamer", cantidad: 2, precio_unitario: 50.0, subtotal: 100.0 }
    ]
  }
]);

// --- Índices ---
db.personas.createIndex({ usuario: 1 }, { unique: true });
db.usuarios.createIndex({ _id: 1 }, { unique: true });
db.clientes.createIndex({ correo: 1 }, { unique: true });
db.productos.createIndex({ nombre: 1 });
db.ventas.createIndex({ id_persona: 1, fecha_emision: -1 });
db.ventas.createIndex({ fecha_emision: -1 });
db.ventas.createIndex({ "detalles.id_producto": 1 });

print("SEED OK: colecciones creadas en 'proyecto'");
