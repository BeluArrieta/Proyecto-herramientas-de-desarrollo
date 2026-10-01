let tienda;
const $ = (id) => document.getElementById(id);
const preview = $('preview');

function pintar() {
  const t = { ...tienda, colorPrimario: $('colorPrimario').value, colorSecundario: $('colorSecundario').value };
  $('hexP').textContent = t.colorPrimario;
  $('hexS').textContent = t.colorSecundario;
  applyTheme(t, preview); // CSS custom properties solo en el mockup
  const logo = $('logoUrl').value.trim();
  const pv = $('pvLogo');
  pv.textContent = '';
  pv.style.backgroundImage = '';
  if (/^https?:\/\//i.test(logo)) {
    pv.style.backgroundImage = `url("${encodeURI(logo)}")`;
  } else {
    pv.textContent = (tienda.nombre || '?').charAt(0).toUpperCase();
  }
}

function cargar(t) {
  $('logoUrl').value = t.logoUrl || '';
  $('colorPrimario').value = t.colorPrimario;
  $('colorSecundario').value = t.colorSecundario;
  $('pvNombre').textContent = t.nombre;
  pintar();
}

document.addEventListener('tienda-lista', (e) => { tienda = e.detail; cargar(tienda); });
['logoUrl', 'colorPrimario', 'colorSecundario'].forEach(id => $(id).addEventListener('input', pintar));

$('reset').addEventListener('click', () => cargar({ ...tienda, colorPrimario: Store.TIENDA_DEFAULT.colorPrimario, colorSecundario: Store.TIENDA_DEFAULT.colorSecundario, logoUrl: '' }));

$('marca-form').addEventListener('submit', (e) => {
  e.preventDefault();
  const logo = $('logoUrl').value.trim();
  if (logo && !/^https?:\/\//i.test(logo)) { toast('El logo debe empezar con http:// o https://'); return; }
  tienda = { ...tienda, logoUrl: logo, colorPrimario: $('colorPrimario').value, colorSecundario: $('colorSecundario').value };
  /* BACKEND: PUT /tiendas/{slug} con el objeto completo (mismo contrato). */
  Store.saveTienda(tienda);
  applyTheme(tienda); // refleja el cambio también en el panel
  toast('Marca guardada');
});
