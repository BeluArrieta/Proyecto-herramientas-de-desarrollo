const form = document.getElementById('registro-form');
const nombre = document.getElementById('nombre');
const rubro = document.getElementById('rubro');
const slug = document.getElementById('slug');
let slugEditado = false; // si el usuario lo toca, dejamos de autogenerar

function setError(input, msg) {
  const f = input.closest('.field');
  f.classList.toggle('invalid', !!msg);
  f.querySelector('.error').textContent = msg || '';
  return !msg;
}

nombre.addEventListener('input', () => {
  setError(nombre, '');
  if (!slugEditado) slug.value = Store.slugify(nombre.value);
});
slug.addEventListener('input', () => { slugEditado = true; setError(slug, ''); });
slug.addEventListener('blur', () => { slug.value = Store.slugify(slug.value); });
rubro.addEventListener('change', () => setError(rubro, ''));

form.addEventListener('submit', async (e) => {
  e.preventDefault();
  const s = Store.slugify(slug.value);
  const ok = [
    setError(nombre, nombre.value.trim().length >= 2 ? '' : 'Escribe el nombre de tu tienda.'),
    setError(rubro, rubro.value ? '' : 'Elige un rubro.'),
    setError(slug, s ? '' : 'La dirección no puede estar vacía.'),
  ].every(Boolean);
  if (!ok) return;

  /* BACKEND: POST /tiendas con este mismo objeto; manejar 409 si el slug ya existe. */
  Store.saveTienda({
    nombre: nombre.value.trim(),
    rubro: rubro.value,
    slug: s,
    colorPrimario: Store.TIENDA_DEFAULT.colorPrimario,
    colorSecundario: Store.TIENDA_DEFAULT.colorSecundario,
    logoUrl: '',
  });
  localStorage.removeItem('productos'); // tienda nueva = catálogo nuevo (mock)
  Store.login('admin');
  location.href = 'marca.html';
});
