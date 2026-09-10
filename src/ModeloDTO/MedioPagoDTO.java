package ModeloDTO;

public class MedioPagoDTO {
    private String idMedioPago;
    private String descripcion;

    public MedioPagoDTO() { }

    public MedioPagoDTO(String idMedioPago, String descripcion) {
        this.idMedioPago = idMedioPago;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getIdMedioPago() { 
        return idMedioPago; }
    public void setIdMedioPago(String idMedioPago) { 
        this.idMedioPago = idMedioPago; }

    public String getDescripcion() { 
        return descripcion; }
    public void setDescripcion(String descripcion) { 
        this.descripcion = descripcion; }

    @Override
    public String toString() {
        return "MedioPagoDTO{" +
               "idMedioPago='" + idMedioPago + '\'' +
               ", descripcion='" + descripcion + '\'' +
               '}';
    }
}