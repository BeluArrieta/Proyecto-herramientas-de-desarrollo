package ModeloDTO;

public class ClienteDTO {

    private String idCliente;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private String password;

    // =========================
    // CONSTRUCTOR VACÍO
    // =========================
    public ClienteDTO() {
    }

    // =========================
    // CONSTRUCTOR COMPLETO
    // =========================
    public ClienteDTO(String idCliente, String nombre, String apellido,
                      String telefono, String correo, String password) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.correo = correo;
        this.password = password;
    }

    // =========================
    // CONSTRUCTOR PARA BOLETA / CONSULTAS
    // =========================
    public ClienteDTO(String idCliente, String nombre, String apellido) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    // =========================
    // GETTERS Y SETTERS
    // =========================
    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}