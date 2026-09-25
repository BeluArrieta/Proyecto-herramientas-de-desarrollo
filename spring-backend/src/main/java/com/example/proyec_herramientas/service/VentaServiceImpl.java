package com.example.proyec_herramientas.service;

import com.example.proyec_herramientas.exception.RecursoNoEncontradoException;
import com.example.proyec_herramientas.exception.StockInsuficienteException;
import com.example.proyec_herramientas.model.CarritoRequestDTO;
import com.example.proyec_herramientas.model.ClienteDTO;
import com.example.proyec_herramientas.persistence.DetalleVenta;
import com.example.proyec_herramientas.persistence.ProductoDocument;
import com.example.proyec_herramientas.persistence.VentaDocument;
import com.example.proyec_herramientas.repository.ProductoRepository;
import com.example.proyec_herramientas.repository.VentaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class VentaServiceImpl implements VentaService {

    private static final String TIPO_BOLETA = "Boleta";

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final ClienteService clienteService;

    public VentaServiceImpl(VentaRepository ventaRepository,
                            ProductoRepository productoRepository,
                            ClienteService clienteService) {
        this.ventaRepository = ventaRepository;
        this.productoRepository = productoRepository;
        this.clienteService = clienteService;
    }

    @Override
    public VentaDocument registrarVenta(CarritoRequestDTO carrito) {
        String tipoDocumento = carrito.getTipoDocumento() == null || carrito.getTipoDocumento().isBlank()
                ? TIPO_BOLETA : carrito.getTipoDocumento();
        String medioPago = carrito.getMedioPago() == null || carrito.getMedioPago().isBlank()
                ? "Efectivo" : carrito.getMedioPago();

        List<DetalleVenta> detalles = new ArrayList<>();

        for (CarritoRequestDTO.Item item : carrito.getItems()) {
            ProductoDocument producto = productoRepository.findById(item.getIdProducto())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "Producto " + item.getIdProducto() + " no existe"));

            if (producto.getStock() < item.getCantidad()) {
                throw new StockInsuficienteException(
                        "Stock insuficiente para " + producto.getNombre()
                                + " (disponible: " + producto.getStock() + ")");
            }

            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            detalles.add(new DetalleVenta(
                    producto.getId(),
                    producto.getNombre(),
                    item.getCantidad(),
                    producto.getPrecio()));
        }

        double total = detalles.stream()
                .mapToDouble(DetalleVenta::getSubtotal)
                .sum();

        VentaDocument venta = new VentaDocument();
        venta.setId(UUID.randomUUID().toString());
        venta.setIdPersona(carrito.getIdCliente());
        venta.setCliente(nombreCliente(carrito.getIdCliente()));
        venta.setTipoDocumento(tipoDocumento);
        venta.setIdTipoDocumento(TIPO_BOLETA.equalsIgnoreCase(tipoDocumento) ? "DOC-01" : "DOC-02");
        venta.setNumeroDocumento(siguienteNumero(tipoDocumento));
        venta.setIdMedioPago(idMedioPago(medioPago));
        venta.setMedioPago(medioPago);
        venta.setFechaEmision(new Date());
        venta.setTotal(total);
        venta.setDetalles(detalles);

        return ventaRepository.save(venta);
    }

    @Override
    public VentaDocument obtenerVentaPorId(String idVenta) {
        return ventaRepository.findById(idVenta)
                .orElseThrow(() -> new RecursoNoEncontradoException("Venta " + idVenta + " no encontrada"));
    }

    @Override
    public List<VentaDocument> obtenerBoletas() {
        return ventaRepository.findByTipoDocumentoOrderByFechaEmisionDesc(TIPO_BOLETA);
    }

    @Override
    public List<VentaDocument> obtenerHistorial(String idCliente) {
        if (idCliente == null || idCliente.isBlank()) {
            return ventaRepository.findAllByOrderByFechaEmisionDesc();
        }
        return ventaRepository.findByIdPersonaOrderByFechaEmisionDesc(idCliente);
    }

    private String nombreCliente(String idCliente) {
        if (idCliente == null || idCliente.isBlank()) {
            return "Invitado";
        }
        ClienteDTO cliente = clienteService.obtenerPorId(idCliente);
        if (cliente != null) {
            return cliente.getNombre() + " " + cliente.getApellido();
        }
        return clienteService.listar().stream()
                .filter(c -> idCliente.equalsIgnoreCase(c.getIdCliente()))
                .map(c -> c.getNombre() + " " + c.getApellido())
                .findFirst()
                .orElse("Cliente " + idCliente);
    }

    private String siguienteNumero(String tipoDocumento) {
        long contador = ventaRepository.count();
        String prefijo = TIPO_BOLETA.equalsIgnoreCase(tipoDocumento) ? "BOL" : "FAC";
        return prefijo + "-" + String.format("%06d", contador + 1);
    }

    private String idMedioPago(String medioPago) {
        switch (medioPago) {
            case "Tarjeta":
            case "Tarjeta de crédito":
            case "Visa":
            case "Mastercard":
                return "MP002";
            case "Tarjeta de débito":
                return "MP003";
            case "Yape":
                return "MP004";
            case "Plin":
                return "MP005";
            case "Transferencia":
            case "Depósito":
                return "MP006";
            default:
                return "MP001";
        }
    }
}