package ModeloDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FacturaDTO {

    private String idFactura;
    private Date fechaEmision;
    private double total;

    private ArrayList<VentaDTO> ventas;
    private ClienteDTO cliente;

    // Constructor alternativo para crear FacturaDTO a partir de ventas
    public FacturaDTO(String idFactura, Date fechaEmision, ClienteDTO cliente, List<VentaDTO> ventas) {
        this.idFactura = idFactura;
        this.fechaEmision = fechaEmision;
        this.cliente = cliente;
        this.ventas = new ArrayList<>(ventas);
        this.total = ventas.stream()
                .mapToDouble(v -> v.getCantidad() * v.getPrecioUnitario())
                .sum();
    }

    // Getters
    public String getIdFactura() {
        return idFactura;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public double getTotal() {
        return total;
    }

    public ArrayList<VentaDTO> getVentas() {
        return ventas;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }
}