document.addEventListener("DOMContentLoaded", () => {
    const formulario = document.getElementById("formulario-login");

    formulario.addEventListener("submit", function(evento) {
        evento.preventDefault();

        // En un proyecto real, aquí harías un fetch() al backend de Spring.
        // Por ahora, simulamos que el backend nos respondió con un token de éxito:
        const tokenSimulado = "abc123tokenfalso_cliente";
        
        // Guardamos el token en localStorage para mantener la sesión
        localStorage.setItem("tokenCliente", tokenSimulado);

        alert("¡Sesión iniciada correctamente!");
        
        // Redirigimos al historial de compras
        window.location.href = "mis-compras.html";
    });
});