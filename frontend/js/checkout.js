document.addEventListener("DOMContentLoaded", () => {
    const formulario = document.getElementById("formulario-checkout");

    // Escuchamos cuando el usuario intente enviar el formulario
    formulario.addEventListener("submit", function(evento) {
        evento.preventDefault(); // Evitamos que la página se recargue

        // Aquí más adelante se enviarán los datos al Backend (Spring)
        console.log("Procesando pago ficticio...");

        // Como la compra fue un "éxito", vaciamos el carrito del navegador
        localStorage.removeItem("carrito");

        // Redirigimos a la pantalla de confirmación
        window.location.href = "confirmacion.html";
    });
});