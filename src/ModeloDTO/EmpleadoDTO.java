package ModeloDTO;

public class EmpleadoDTO {
    private String idEmpleado;
    private String nombre;
    private String apellido;
    private String cargo;
    private String telefono;

    public EmpleadoDTO() { }

    public EmpleadoDTO(String idEmpleado, String nombre, String apellido, String cargo, String telefono) {
        this.idEmpleado = idEmpleado;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cargo = cargo;
        this.telefono = telefono;
    }

    // Getters y Setters
    public String getIdEmpleado() { 
        return idEmpleado; }
    public void setIdEmpleado(String idEmpleado) { 
        this.idEmpleado = idEmpleado; }

    public String getNombre() { 
        return nombre; }
    public void setNombre(String nombre) { 
        this.nombre = nombre; }

    public String getApellido() { 
        return apellido; }
    public void setApellido(String apellido) { 
        this.apellido = apellido; }

    public String getCargo() { 
        return cargo; }
    public void setCargo(String cargo) { 
        this.cargo = cargo; }

    public String getTelefono() { 
        return telefono; }
    public void setTelefono(String telefono) { 
        this.telefono = telefono; }

    @Override
    public String toString() {
        return "EmpleadoDTO{" +
               "idEmpleado='" + idEmpleado + '\'' +
               ", nombre='" + nombre + '\'' +
               ", apellido='" + apellido + '\'' +
               ", cargo='" + cargo + '\'' +
               ", telefono='" + telefono + '\'' +
               '}';
    }
}