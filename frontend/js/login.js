const form = document.getElementById('login-form');
const usuario = document.getElementById('usuario');
const clave = document.getElementById('clave');

function setError(input, msg) {
  const f = input.closest('.field');
  f.classList.toggle('invalid', !!msg);
  f.querySelector('.error').textContent = msg || '';
  return !msg;
}

[usuario, clave].forEach(i => i.addEventListener('input', () => setError(i, '')));

form.addEventListener('submit', (e) => {
  e.preventDefault();
  const u = usuario.value.trim(), c = clave.value;
  const okU = setError(usuario, u ? '' : 'Ingresa tu usuario o correo.');
  const okC = setError(clave, c.length >= 1 ? '' : 'Ingresa tu contraseña.');
  if (!okU || !okC) return;

  /* BACKEND: POST /auth/login { usuario, clave } → { token }.
     Guardar el token y redirigir solo si responde 200. */
  Store.login(u);
  location.href = 'productos.html'; // "dashboard" simulado: el panel de gestión
});
