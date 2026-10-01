document.addEventListener("DOMContentLoaded", () => {
    // 1. Validar la sesión (Si no hay token, redirigir al login)
    const token = localStorage.getItem("tokenCliente");
    
    if (!token) {
        alert("Debes iniciar sesión para ver esta página.");
        window.location.href = "login-cliente.html";
        return;
    }

    // 2. Lógica para cerrar sesión
    const btnCerrar = document.getElementById("btn-cerrar-sesion");
    btnCerrar.addEventListener("click", (e) => {
        e.preventDefault();
        localStorage.removeItem("tokenCliente"); // Borramos el token
        window.location.href = "index.html"; // Lo mandamos al inicio
    });
});