package ModeloDTO;

import java.util.Date;
import java.util.List;

public class BoletaDTO {

    private Long idBoleta;
    private String numeroDocumento;
    private Date fechaEmision;
    private ClienteDTO cliente;
    private List<VentaDTO> ventas;

    public BoletaDTO(String idBoleta, Date fechaEmision,
            ClienteDTO cliente, List<VentaDTO> ventas) {
        asignarNumero(idBoleta);
        this.fechaEmision = fechaEmision;
        this.cliente = cliente;
        this.ventas = ventas;
    }

    private void asignarNumero(String numero) {
        this.numeroDocumento = numero;
        try {
            this.idBoleta = Long.parseLong(numero);
        } catch (Exception e) {
            this.idBoleta = null;
        }
    }

    public String getNumeroDocumento() {
        if (numeroDocumento != null && !numeroDocumento.trim().isEmpty()) {
            return numeroDocumento;
        }
        if (idBoleta != null) {
            return "BOL-" + String.format("%06d", idBoleta);
        }
        return "BOL-PENDIENTE";
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public List<VentaDTO> getVentas() {
        return ventas;
    }

    public double getTotal() {
        if (ventas == null) {
            return 0;
        }
        return ventas.stream()
                .mapToDouble(v -> v.getCantidad() * v.getPrecioUnitario())
                .sum();
    }
}
