document.addEventListener("DOMContentLoaded", () => {
    cargarTienda();
});

async function cargarTienda() {
    try {
        const respuesta = await fetch('./tienda-demo.json');
        const datos = await respuesta.json();

        // 1. Inyectar datos de la marca
        document.getElementById('nombre-tienda').textContent = datos.nombre || "Mi Tienda";
        
        if (datos.logoUrl && datos.logoUrl !== "") {
            const logo = document.getElementById('logo-tienda');
            logo.src = datos.logoUrl;
            logo.style.display = 'block';
        }

        // 2. Aplicar el color primario a la cabecera
        const cabecera = document.getElementById('cabecera-tienda');
        if (datos.colorPrimario) {
            cabecera.style.backgroundColor = datos.colorPrimario;
        } else {
            cabecera.style.backgroundColor = "#333"; // Color por defecto
        }

        // 3. Renderizar los productos
        renderizarProductos(datos.productos);

    } catch (error) {
        console.error("Error cargando el JSON:", error);
        document.getElementById('contenedor-productos').innerHTML = "<p>Error al cargar el catálogo.</p>";
    }
}

function renderizarProductos(productos) {
    const contenedor = document.getElementById('contenedor-productos');
    contenedor.innerHTML = "";

    if (!productos || productos.length === 0) {
        contenedor.innerHTML = "<p>No hay productos disponibles.</p>";
        return;
    }

    productos.forEach(producto => {
        const hayStock = producto.stock > 0;
        
        // Creamos una tarjeta básica. 
        // Si Kiara tiene clases específicas en productos.css, las podemos adaptar luego.
        const tarjeta = document.createElement('div');
        tarjeta.style.cssText = "background: white; border: 1px solid #ddd; border-radius: 8px; padding: 15px; width: 250px; text-align: center; box-shadow: 0 2px 5px rgba(0,0,0,0.1);";
        
        tarjeta.innerHTML = `
            <img src="${producto.imagen}" alt="${producto.nombre}" style="max-width: 100%; height: 200px; object-fit: contain; margin-bottom: 15px;">
            <h3 style="margin: 0 0 10px 0; font-size: 18px;">${producto.nombre}</h3>
            <p style="color: #28a745; font-size: 20px; font-weight: bold; margin: 0 0 10px 0;">$${producto.precio}</p>
            <p style="margin: 0 0 15px 0; font-size: 14px; color: #666;">Stock disponible: ${producto.stock}</p>
            <button 
                onclick="agregarAlCarrito(${producto.id})" 
                ${!hayStock ? 'disabled' : ''}
                style="width: 100%; padding: 10px; border: none; border-radius: 4px; font-weight: bold; cursor: ${hayStock ? 'pointer' : 'not-allowed'}; background-color: ${hayStock ? '#0071e3' : '#ccc'}; color: white;">
                ${hayStock ? 'Agregar al Carrito' : 'Agotado'}
            </button>
        `;
        
        contenedor.appendChild(tarjeta);
    });
}


function agregarAlCarrito(id) {
    // Leemos el carrito actual de localStorage o creamos uno vacío
    let carrito = JSON.parse(localStorage.getItem('carrito')) || [];

    // Verificamos si el producto ya fue agregado antes
    const indice = carrito.findIndex(item => item.id === id);

    if (indice !== -1) {
        // Si ya existe, sumamos 1 a la cantidad
        carrito[indice].cantidad++;
    } else {
        // Si es nuevo, lo agregamos con cantidad 1
        carrito.push({ id: id, cantidad: 1 });
    }

    // Guardamos los cambios en el navegador
    localStorage.setItem('carrito', JSON.stringify(carrito));
    
    // Actualizamos el número visual del carrito en el menú
    actualizarContadorCarrito();
}

// Agrega esta función al final del archivo:
function actualizarContadorCarrito() {
    const carrito = JSON.parse(localStorage.getItem('carrito')) || [];
    const totalItems = carrito.reduce((total, item) => total + item.cantidad, 0);
    const contador = document.getElementById('contador-carrito');
    if (contador) {
        contador.textContent = totalItems;
    }
}