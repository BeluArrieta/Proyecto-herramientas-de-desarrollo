# Modelo de datos MongoDB — Sistema de Facturación

Este documento describe el modelo de base de datos en MongoDB para reemplazar
PostgreSQL en el proyecto. Usa el driver Java síncrono `mongodb-driver-sync 5.2.0`
(BSON 5.2.0 + mongodb-driver-core 5.2.0), incluidos en `lib/`.

- Servidor: `mongodb://localhost:27017`
- Base de datos: `proyecto`
- Clase de conexión: `Config.ConexionMongo`

## Colecciones

Mapeo de las tablas de PostgreSQL a colecciones MongoDB:

| Tabla PostgreSQL  | Colección Mongo   |
|-------------------|-------------------|
| persona           | personas          |
| usuario           | usuarios          |
| cliente           | clientes          |
| empleado          | empleados         |
| producto          | productos         |
| tipo_documento    | tipo_documentos   |
| medio_pago        | medios_pago       |
| venta             | ventas            |
| detalle_venta     | (embebido en ventas.detalles) |
| historial_compras | (vista: ya no se usa)        |

### personas

Documento que reproduce la tabla `persona`. `_id` es el `id_persona` original.

```json
{
  "_id": "PER-0001",
  "nombre": "Juan",
  "apellido": "Pérez",
  "fecha_nacimiento": null,
  "correo": "juan@correo.com",
  "telefono": "987654321",
  "usuario": "juanp"
}
```

### usuarios

Reproduce la tabla `usuario`. `_id` es el nombre de usuario y `contrasena`
almacena el hash.

```json
{
  "_id": "juanp",
  "contrasena": "<hash SHA-256>",
  "id_persona": "PER-0001"
}
```

### clientes

Reproduce la tabla `cliente`. `_id` es el `id_cliente` y `contrasena` tiene el
hash de la contraseña.

```json
{
  "_id": "CLI-0001",
  "nombre": "Juan",
  "apellido": "Pérez",
  "telefono": "987654321",
  "contrasena": "<hash>",
  "correo": "juan@correo.com"
}
```

### empleados

Reproduce la tabla `empleado`.

```json
{
  "_id": "EMP-0001",
  "nombre": "María",
  "apellido": "López",
  "cargo": "Vendedor",
  "telefono": "912345678"
}
```

### productos

```json
{
  "_id": 1,
  "nombre": "Mouse Gamer",
  "stock": 10,
  "precio": 50.0,
  "categoria": "Accesorios"
}
```

### tipo_documentos

```json
{
  "_id": "DOC-01",
  "nombre": "Boleta"
}
```

### medios_pago

```json
{
  "_id": "MP001",
  "descripcion": "Efectivo"
}
```

### ventas (con detalles embebidos)

Reproduce `venta` + `detalle_venta` en un solo documento.

```json
{
  "_id": "uuid-venta",
  "id_persona": "PER-0001",
  "id_tipo_documento": "DOC-01",
  "numero_documento": "BOL-000001",
  "id_medio_pago": "MP001",
  "fecha_emision": {"$date": "2026-09-17T00:00:00Z"},
  "total": 88.5,
  "detalles": [
    {
      "id_producto": 1,
      "producto": "Mouse Gamer",
      "cantidad": 2,
      "precio_unitario": 50.0,
      "subtotal": 100.0
    }
  ]
}
```

## Índices recomendados

```js
db.personas.createIndex({ usuario: 1 }, { unique: true });
db.usuarios.createIndex({ _id: 1 }, { unique: true });
db.clientes.createIndex({ correo: 1 }, { unique: true });
db.productos.createIndex({ nombre: 1 });
db.ventas.createIndex({ id_persona: 1, fecha_emision: -1 });
db.ventas.createIndex({ fecha_emision: -1 });
db.ventas.createIndex({ "detalles.id_producto": 1 });
```

## Carga de datos de ejemplo

El archivo `ops/seed.js` crea las colecciones e inserta documentos de ejemplo
compatibles con las operaciones de los DAOs. Ejecutar con:

```
mongosh "mongodb://localhost:27017/proyecto" ops/seed.js
```
