/* ===== Capa de datos (mock con localStorage) =====
   Toda la UI habla SOLO con este objeto. Cuando exista el backend Spring Boot,
   se reemplaza el cuerpo de cada método por fetch() y la UI no cambia. */
const Store = (() => {
  const K = { tienda: 'tienda', productos: 'productos', umbral: 'umbralStock', sesion: 'adminSesion' };
  const read = (k, fb) => { try { const v = localStorage.getItem(k); return v ? JSON.parse(v) : fb; } catch { return fb; } };
  const write = (k, v) => localStorage.setItem(k, JSON.stringify(v));

  // Contrato fijo de tienda (no renombrar campos)
  const TIENDA_DEFAULT = { nombre: 'Mi Tienda', rubro: 'moda', slug: 'mi-tienda', colorPrimario: '#0071e3', colorSecundario: '#1d1d1f', logoUrl: '' };

  const slugify = (s) => s.normalize('NFD').replace(/[̀-ͯ]/g, '').toLowerCase()
    .replace(/[^a-z0-9]+/g, '-').replace(/^-+|-+$/g, '');

  /* BACKEND: GET /tiendas/{slug}  |  PUT /tiendas/{slug} */
  async function getTienda() {
    const guardada = read(K.tienda, null);
    if (guardada) return guardada;
    try { // tienda-demo.json (falla en file://, por eso hay fallback embebido)
      const r = await fetch('tienda-demo.json');
      if (r.ok) return await r.json();
    } catch { /* sin servidor local */ }
    return { ...TIENDA_DEFAULT };
  }
  /* BACKEND: POST /tiendas (registro) / PUT /tiendas/{slug} (marca) */
  function saveTienda(t) { write(K.tienda, t); return t; }

  /* Productos. Campo idTienda listo para filtrar ?idTienda=... */
  const SEED = (id) => [
    { id: 'p1', idTienda: id, nombre: 'Camiseta básica', categoria: 'Ropa', precio: 24.9, stock: 42 },
    { id: 'p2', idTienda: id, nombre: 'Jean slim', categoria: 'Ropa', precio: 59, stock: 4 },
    { id: 'p3', idTienda: id, nombre: 'Zapatillas urbanas', categoria: 'Calzado', precio: 89.5, stock: 0 },
    { id: 'p4', idTienda: id, nombre: 'Gorra', categoria: 'Accesorios', precio: 15, stock: 18 },
  ];
  /* BACKEND: GET /productos?idTienda=... */
  async function listProductos() {
    let ps = read(K.productos, null);
    if (!ps) { const t = await getTienda(); ps = SEED(t.slug); write(K.productos, ps); }
    return ps;
  }
  /* BACKEND: POST /productos  (body incluye idTienda) */
  async function createProducto(p) { const ps = await listProductos(); const n = { ...p, id: 'p' + Date.now() }; ps.push(n); write(K.productos, ps); return n; }
  /* BACKEND: PUT /productos/{id} */
  async function updateProducto(id, p) { const ps = (await listProductos()).map(x => x.id === id ? { ...x, ...p } : x); write(K.productos, ps); }
  /* BACKEND: DELETE /productos/{id} */
  async function deleteProducto(id) { write(K.productos, (await listProductos()).filter(x => x.id !== id)); }

  /* BACKEND (opcional): campo umbralStock en la tienda */
  const getUmbral = () => read(K.umbral, 5);
  const setUmbral = (n) => write(K.umbral, n);

  /* BACKEND: POST /auth/login → token (JWT). Aquí solo una bandera. */
  const login = (u) => write(K.sesion, { usuario: u });
  const isLogged = () => !!read(K.sesion, null);
  const logout = () => localStorage.removeItem(K.sesion);

  return { slugify, getTienda, saveTienda, listProductos, createProducto, updateProducto, deleteProducto, getUmbral, setUmbral, login, isLogged, logout, TIENDA_DEFAULT };
})();

/* Luminancia para elegir texto legible sobre el color primario */
function textoSobre(hex) {
  const n = parseInt(hex.slice(1), 16), r = n >> 16, g = (n >> 8) & 255, b = n & 255;
  return (0.299 * r + 0.587 * g + 0.114 * b) > 160 ? '#1d1d1f' : '#ffffff';
}
/* Inyecta los colores de la tienda como CSS custom properties */
function applyTheme(t, el = document.documentElement) {
  el.style.setProperty('--color-primario', t.colorPrimario);
  el.style.setProperty('--color-secundario', t.colorSecundario);
  el.style.setProperty('--on-primario', textoSobre(t.colorPrimario));
}
