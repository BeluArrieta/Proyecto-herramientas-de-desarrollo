document.addEventListener("DOMContentLoaded", () => {
    mostrarCarrito();
});

async function mostrarCarrito() {
    const contenedor = document.getElementById('contenido-carrito');
    const carrito = JSON.parse(localStorage.getItem('carrito')) || [];

    if (carrito.length === 0) {
        contenedor.innerHTML = "<p>El carrito está vacío por ahora.</p>";
        return;
    }

    try {
        // Volvemos a leer el JSON para obtener los precios y nombres actualizados
        const respuesta = await fetch('./tienda-demo.json');
        const datos = await respuesta.json();
        
        let htmlCarrito = `<table style='width: 100%; border-collapse: collapse; text-align: left; margin-bottom: 20px;'>
            <tr style='border-bottom: 2px solid #ddd;'>
                <th style='padding: 10px;'>Producto</th>
                <th style='padding: 10px;'>Cantidad</th>
                <th style='padding: 10px;'>Subtotal</th>
            </tr>`;
        let total = 0;

        carrito.forEach(item => {
            // Buscamos los detalles originales del producto usando el ID
            const productoOriginal = datos.productos.find(p => p.id === item.id);
            
            if (productoOriginal) {
                const subtotal = productoOriginal.precio * item.cantidad;
                total += subtotal;
                
                htmlCarrito += `
                <tr style='border-bottom: 1px solid #eee;'>
                    <td style='padding: 10px;'>${productoOriginal.nombre}</td>
                    <td style='padding: 10px;'>${item.cantidad}</td>
                    <td style='padding: 10px; font-weight: bold;'>$${subtotal.toFixed(2)}</td>
                </tr>`;
            }
        });

        htmlCarrito += `</table>
            <h3 style="text-align: right; margin-top: 20px;">Total a pagar: $${total.toFixed(2)}</h3>
            <button onclick="irAlCheckout()" style="background-color: ${datos.colorPrimario || '#0071e3'}; color: white; padding: 12px 25px; border: none; cursor: pointer; float: right; font-weight: bold; border-radius: 4px; font-size: 16px;">Proceder al Checkout</button>`;
        
        contenedor.innerHTML = htmlCarrito;

    } catch (error) {
        console.error("Error al cargar los datos para el carrito:", error);
    }
}

function irAlCheckout() {
    window.location.href = "checkout.html";
}