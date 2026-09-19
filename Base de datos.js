// ============================================================
// BASE DE DATOS — MongoDB (Sistema de Facturación)
// Crea la base "proyecto" con todas sus colecciones, datos y
// nombres de campos del modelo actual (migrado a MongoDB).
//
// Ejecutar (mongosh):
//   mongosh "mongodb://admin:123@localhost:27017/?authSource=admin" "Base de datos.js"
// ============================================================

const db = db.getSiblingDB("proyecto");

// Hash SHA-256 de "123456" (Modelo.SeguridadContrasena)
const HASH_123456 = "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3";

// ------------------------------------------------------------
// 1. LIMPIAR BASE
// ------------------------------------------------------------
db.personas.drop();
db.usuarios.drop();
db.clientes.drop();
db.empleados.drop();
db.productos.drop();
db.tipo_documentos.drop();
db.medios_pago.drop();
db.ventas.drop();

// ------------------------------------------------------------
// 2. PERSONAS  (_id = codigo de persona)
// ------------------------------------------------------------
db.personas.insertMany([
  // Administrador (no tiene documento en "clientes" => tipo ADMIN)
  { _id: "PER-0000", nombre: "Admin", apellido: "Sistema", fecha_nacimiento: null, correo: "admin@sistema.com", telefono: "999000111", usuario: "admin" },
  { _id: "PER-0001", nombre: "Juan", apellido: "Pérez", fecha_nacimiento: null, correo: "juan@correo.com", telefono: "987654321", usuario: "juanp" },
  { _id: "PER-0002", nombre: "Ana", apellido: "Gómez", fecha_nacimiento: null, correo: "ana@correo.com", telefono: "912345678", usuario: "anag" }
]);

// ------------------------------------------------------------
// 3. USUARIOS  (_id = nombre de usuario)
//    Campos: _id, contrasena (hash), id_persona
// ------------------------------------------------------------
db.usuarios.insertMany([
  { _id: "admin", contrasena: HASH_123456, id_persona: "PER-0000" },
  { _id: "juanp", contrasena: HASH_123456, id_persona: "PER-0001" },
  { _id: "anag",  contrasena: HASH_123456, id_persona: "PER-0002" }
]);

// ------------------------------------------------------------
// 4. CLIENTES  (_id = codigo de cliente)
//    Campos: _id, nombre, apellido, telefono, contrasena, correo
// ------------------------------------------------------------
db.clientes.insertMany([
  { _id: "CLI-0001", nombre: "Juan", apellido: "Pérez", telefono: "987654321", contrasena: HASH_123456, correo: "juan@correo.com" },
  { _id: "CLI-0002", nombre: "Ana", apellido: "Gómez", telefono: "912345678",  contrasena: HASH_123456, correo: "ana@correo.com" }
]);

// ------------------------------------------------------------
// 5. EMPLEADOS  (_id = codigo de empleado)
// ------------------------------------------------------------
db.empleados.insertMany([
  { _id: "EMP-0001", nombre: "María", apellido: "López", cargo: "Vendedor", telefono: "912345678" },
  { _id: "EMP-0002", nombre: "Carlos", apellido: "Ramírez", cargo: "Cajero", telefono: "955112233" }
]);

// ------------------------------------------------------------
// 6. PRODUCTOS  (_id = número)
//    Campos: _id, nombre, stock, precio, categoria
// ------------------------------------------------------------
db.productos.insertMany([
  { _id: 1, nombre: "Mouse Gamer", stock: 8, precio: 50.0, categoria: "Accesorios" },
  { _id: 2, nombre: "Teclado Mecánico", stock: 5, precio: 120.0, categoria: "Accesorios" },
  { _id: 3, nombre: "Webcam HD", stock: 0, precio: 75.0, categoria: "Accesorios" },
  { _id: 4, nombre: "Audífonos BT", stock: 12, precio: 89.9, categoria: "Audio" }
]);

// ------------------------------------------------------------
// 7. TIPOS DE DOCUMENTO  (_id = DOC-01 / DOC-02)
// ------------------------------------------------------------
db.tipo_documentos.insertMany([
  { _id: "DOC-01", nombre: "Boleta" },
  { _id: "DOC-02", nombre: "Factura" }
]);

// ------------------------------------------------------------
// 8. MEDIOS DE PAGO  (_id = MP001 ... MP006)
// ------------------------------------------------------------
db.medios_pago.insertMany([
  { _id: "MP001", descripcion: "Efectivo" },
  { _id: "MP002", descripcion: "Tarjeta de crédito" },
  { _id: "MP003", descripcion: "Tarjeta de débito" },
  { _id: "MP004", descripcion: "Yape" },
  { _id: "MP005", descripcion: "Plin" },
  { _id: "MP006", descripcion: "Transferencia" }
]);

// ------------------------------------------------------------
// 9. VENTAS  (_id = id de venta)
//    Campos: id_persona, cliente, id_tipo_documento, tipo_documento,
//            numero_documento, id_medio_pago, medio_pago, fecha_emision,
//            total, detalles[] (id_producto, producto, cantidad,
//                              precio_unitario, subtotal)
// ------------------------------------------------------------
db.ventas.insertMany([
  {
    _id: "v0001",
    id_persona: "PER-0001",
    cliente: "Juan Pérez",
    id_tipo_documento: "DOC-01",
    tipo_documento: "Boleta",
    numero_documento: "BOL-000001",
    id_medio_pago: "MP001",
    medio_pago: "Efectivo",
    fecha_emision: new Date(Date.now() - 24 * 60 * 60 * 1000),
    total: 100.0,
    detalles: [
      { id_producto: 1, producto: "Mouse Gamer", cantidad: 2, precio_unitario: 50.0, subtotal: 100.0 }
    ]
  },
  {
    _id: "v0002",
    id_persona: "PER-0002",
    cliente: "Ana Gómez",
    id_tipo_documento: "DOC-01",
    tipo_documento: "Boleta",
    numero_documento: "BOL-000002",
    id_medio_pago: "MP004",
    medio_pago: "Yape",
    fecha_emision: new Date(),
    total: 240.0,
    detalles: [
      { id_producto: 2, producto: "Teclado Mecánico", cantidad: 2, precio_unitario: 120.0, subtotal: 240.0 }
    ]
  }
]);

// ------------------------------------------------------------
// 10. ÍNDICES
// ------------------------------------------------------------
db.personas.createIndex({ usuario: 1 }, { unique: true });      // usuario único
db.clientes.createIndex({ correo: 1 }, { unique: true });       // correo único
db.productos.createIndex({ nombre: 1 });                        // búsqueda por nombre
db.ventas.createIndex({ id_persona: 1, fecha_emision: -1 });    // historial por cliente
db.ventas.createIndex({ fecha_emision: -1 });                   // historial general
db.ventas.createIndex({ "detalles.id_producto": 1 });           // producto más vendido

// ------------------------------------------------------------
// 11. VERIFICACIÓN
// ------------------------------------------------------------
print("\nColecciones creadas en 'proyecto':");
db.getCollectionNames().forEach(function (nombre) {
  print("  - " + nombre + " (" + db.getCollection(nombre).countDocuments() + " documentos)");
});
print("BASE DE DATOS OK: 'proyecto' creada con sus 8 colecciones.");
print("Usuarios de prueba: admin/123456 (ADMIN), juanp/123456 y anag/123456 (CLIENTE)");