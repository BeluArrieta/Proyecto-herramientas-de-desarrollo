/* Nav: blur al hacer scroll + guarda de sesión + marca de la tienda en el panel */
(async function () {
  const nav = document.querySelector('.nav');
  const onScroll = () => nav.classList.toggle('scrolled', window.scrollY > 8);
  onScroll(); window.addEventListener('scroll', onScroll, { passive: true });

  if (document.body.dataset.panel !== undefined) {
    if (!Store.isLogged()) { location.replace('login.html'); return; }
    const t = await Store.getTienda();
    applyTheme(t);
    const b = document.querySelector('.nav-brand');
    if (b) {
      b.textContent = '';
      if (t.logoUrl) {
        const img = new Image(); img.alt = ''; img.src = t.logoUrl; img.onerror = () => img.remove();
        b.appendChild(img);
      }
      const s = document.createElement('span'); s.textContent = t.nombre; b.appendChild(s);
    }
    document.getElementById('salir')?.addEventListener('click', (e) => { e.preventDefault(); Store.logout(); location.href = 'login.html'; });
    document.dispatchEvent(new CustomEvent('tienda-lista', { detail: t }));
  }
})();

function toast(msg) {
  let el = document.querySelector('.toast');
  if (!el) { el = document.createElement('div'); el.className = 'toast'; document.body.appendChild(el); }
  el.textContent = msg; el.classList.add('show');
  clearTimeout(el._t); el._t = setTimeout(() => el.classList.remove('show'), 2200);
}
