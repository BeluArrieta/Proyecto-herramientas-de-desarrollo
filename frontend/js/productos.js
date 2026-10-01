const $ = (id) => document.getElementById(id);
let productos = [], tienda, editandoId = null, soloBajo = false;
const fmt = new Intl.NumberFormat('es', { style: 'currency', currency: 'USD' });
const esBajo = (p) => p.stock < Store.getUmbral();

/* Crea un nodo con texto seguro (sin innerHTML con datos del usuario) */
function el(tag, cls, txt, attrs = {}) {
  const n = document.createElement(tag);
  if (cls) n.className = cls;
  if (txt !== undefined) n.textContent = txt;
  Object.entries(attrs).forEach(([k, v]) => n.setAttribute(k, v));
  return n;
}

async function cargar() {
  /* BACKEND: GET /productos?idTienda=${tienda.slug} */
  productos = await Store.listProductos();
  render();
}

function render() {
  const umbral = Store.getUmbral();
  const bajos = productos.filter(esBajo);

  // --- Alerta de stock bajo ---
  $('alerta').hidden = bajos.length === 0;
  $('umbralSolo').hidden = bajos.length !== 0;
  $('umbral').value = $('umbral2').value = umbral;
  if (bajos.length) {
    $('alertaTitulo').textContent = bajos.length === 1 ? '1 producto con stock bajo' : `${bajos.length} productos con stock bajo`;
    $('alertaLista').textContent = bajos.map(p => `${p.nombre} (${p.stock})`).join(' · ');
  } else { soloBajo = false; }
  $('soloBajo').setAttribute('aria-pressed', soloBajo);
  $('soloBajo').textContent = soloBajo ? 'Ver todos' : 'Ver solo estos';

  // --- Lista ---
  const cont = $('lista');
  cont.textContent = '';
  const visibles = soloBajo ? bajos : productos;
  if (!visibles.length) { cont.appendChild(el('p', 'vacio', 'Aún no hay productos. Crea el primero.')); }
  else {
    const cab = el('div', 'fila cab');
    ['Nombre', 'Categoría', 'Precio', 'Stock', ''].forEach(h => cab.appendChild(el('span', '', h)));
    cont.appendChild(cab);
  }
  visibles.forEach(p => {
    const bajo = esBajo(p);
    const f = el('div', 'fila' + (bajo ? ' bajo' : ''));
    f.appendChild(el('span', 'nombre', p.nombre));
    f.appendChild(el('span', '', p.categoria, { 'data-label': 'Categoría' }));
    f.appendChild(el('span', '', fmt.format(p.precio), { 'data-label': 'Precio' }));

    const st = el('span', '', undefined, { 'data-label': 'Stock' });
    const chip = el('span', 'chip' + (p.stock === 0 ? ' agotado' : ''));
    if (bajo) { // ícono de advertencia + texto: no depende solo del color
      chip.innerHTML = '<svg viewBox="0 0 24 24" width="14" height="14" aria-hidden="true"><path fill="currentColor" d="M12 2 1 21h22L12 2Zm1 15h-2v-2h2v2Zm0-4h-2V9h2v4Z"/></svg>';
    }
    chip.appendChild(document.createTextNode(p.stock === 0 ? 'Agotado' : bajo ? `${p.stock} · Stock bajo` : String(p.stock)));
    st.appendChild(chip); f.appendChild(st);

    const ac = el('span', 'acciones');
    const be = el('button', 'btn small secondary', 'Editar', { type: 'button' });
    const bd = el('button', 'btn small ghost', 'Eliminar', { type: 'button' });
    be.onclick = () => abrir(p);
    bd.onclick = async () => {
      if (!confirm(`¿Eliminar "${p.nombre}"?`)) return;
      /* BACKEND: DELETE /productos/${p.id} */
      await Store.deleteProducto(p.id); toast('Producto eliminado'); cargar();
    };
    ac.append(be, bd); f.appendChild(ac);
    cont.appendChild(f);
  });

  // Categorías existentes como sugerencias
  const dl = $('cats'); dl.textContent = '';
  [...new Set(productos.map(p => p.categoria))].forEach(c => dl.appendChild(el('option', '', undefined, { value: c })));
}

/* ----- Modal ----- */
const dlg = $('dlg');
const campos = ['nombre', 'categoria', 'precio', 'stock'];
function abrir(p) {
  editandoId = p ? p.id : null;
  $('dlgTitulo').textContent = p ? 'Editar producto' : 'Nuevo producto';
  campos.forEach(c => { $('f-' + c).value = p ? p[c] : ''; $('f-' + c).closest('.field').classList.remove('invalid'); $('f-' + c).closest('.field').querySelector('.error').textContent = ''; });
  dlg.showModal(); $('f-nombre').focus();
}
$('nuevo').onclick = () => abrir(null);
$('cancelar').onclick = () => dlg.close();

$('pform').addEventListener('submit', async (e) => {
  e.preventDefault();
  const v = { nombre: $('f-nombre').value.trim(), categoria: $('f-categoria').value.trim(), precio: parseFloat($('f-precio').value), stock: parseInt($('f-stock').value, 10) };
  const reglas = {
    nombre: v.nombre ? '' : 'Requerido.',
    categoria: v.categoria ? '' : 'Requerida.',
    precio: v.precio >= 0 ? '' : 'Precio inválido.',
    stock: Number.isInteger(v.stock) && v.stock >= 0 ? '' : 'Stock inválido.',
  };
  let ok = true;
  campos.forEach(c => { const f = $('f-' + c).closest('.field'); f.classList.toggle('invalid', !!reglas[c]); f.querySelector('.error').textContent = reglas[c]; if (reglas[c]) ok = false; });
  if (!ok) return;

  if (editandoId) { /* BACKEND: PUT /productos/${editandoId} */ await Store.updateProducto(editandoId, v); }
  else { /* BACKEND: POST /productos */ await Store.createProducto({ ...v, idTienda: tienda.slug }); }
  dlg.close(); toast(editandoId ? 'Producto actualizado' : 'Producto creado'); cargar();
});

/* ----- Umbral configurable ----- */
function cambiarUmbral(valor) {
  const n = parseInt(valor, 10);
  if (Number.isInteger(n) && n >= 0) { Store.setUmbral(n); render(); }
}
$('umbral').addEventListener('change', (e) => cambiarUmbral(e.target.value));
$('umbral2').addEventListener('change', (e) => cambiarUmbral(e.target.value));
$('soloBajo').onclick = () => { soloBajo = !soloBajo; render(); };

document.addEventListener('tienda-lista', (e) => { tienda = e.detail; cargar(); });
