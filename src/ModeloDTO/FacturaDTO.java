package ModeloDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FacturaDTO {

    private String idFactura;
    private String clienteId;    // Foreign Key a Cliente
    private String empleadoId;   // Foreign Key a Empleado
    private String idMedioPago;  // Foreign Key a MedioPago
    private Date fechaEmision;
    private double total;

    private ArrayList<VentaDTO> ventas;
    private ClienteDTO cliente;
    private EmpleadoDTO empleado;
    private MedioPagoDTO medioPago;

    public FacturaDTO() {
        this.ventas = new ArrayList<>();
    }

        // Constructor alternativo para crear FacturaDTO a partir de BoletaDTO
    public FacturaDTO(String idFactura, Date fechaEmision, ClienteDTO cliente, List<VentaDTO> ventas) {
        this.idFactura = idFactura;
        this.fechaEmision = fechaEmision;
        this.cliente = cliente;
        this.ventas = new ArrayList<>(ventas);
        this.total = ventas.stream()
                           .mapToDouble(v -> v.getCantidad() * v.getPrecioUnitario())
                           .sum();
    }

    // Getters y Setters
    public String getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(String idFactura) {
        this.idFactura = idFactura;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getEmpleadoId() {
        return empleadoId;
    }

    public void setEmpleadoId(String empleadoId) {
        this.empleadoId = empleadoId;
    }

    public String getIdMedioPago() {
        return idMedioPago;
    }

    public void setIdMedioPago(String idMedioPago) {
        this.idMedioPago = idMedioPago;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public ArrayList<VentaDTO> getVentas() {
        return ventas;
    }

    public void setVentas(ArrayList<VentaDTO> ventas) {
        this.ventas = ventas;
    }

    public void addVenta(VentaDTO venta) {
        this.ventas.add(venta);
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public EmpleadoDTO getEmpleado() {
        return empleado;
    }

    public void setEmpleado(EmpleadoDTO empleado) {
        this.empleado = empleado;
    }

    public MedioPagoDTO getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(MedioPagoDTO medioPago) {
        this.medioPago = medioPago;
    }

    @Override
    public String toString() {
        return "FacturaDTO{"
                + "idFactura='" + idFactura + '\''
                + ", clienteId='" + clienteId + '\''
                + ", empleadoId='" + empleadoId + '\''
                + ", idMedioPago='" + idMedioPago + '\''
                + ", fechaEmision=" + fechaEmision
                + ", total=" + total
                + ", ventas=" + ventas.size() + " items"
                + '}';
    }
}
