package carlos.historias_8_9_10;

import java.util.Date;

public class Cliente {
    
    private int idCliente;
    private String nombre;
    private String apellidos;
    private String dni;
    private String pasaporte;
    private String tarjeta;
    private String email;
    private java.util.Date fechaNacimiento;
    private String movil;
    private String telefonoFijo;
    private String direccion;
    private String poblacion;
    private String cp;

    public Cliente(int idCliente, String nombre, String apellidos, String dni, String pasaporte, String tarjeta, String email, Date fechaNacimiento, String movil, String telefonoFijo, String direccion, String poblacion, String cp) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.pasaporte = pasaporte;
        this.tarjeta = tarjeta;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.movil = movil;
        this.telefonoFijo = telefonoFijo;
        this.direccion = direccion;
        this.poblacion = poblacion;
        this.cp = cp;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getDni() {
        return dni;
    }

    public String getPasaporte() {
        return pasaporte;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public String getEmail() {
        return email;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getMovil() {
        return movil;
    }

    public String getTelefonoFijo() {
        return telefonoFijo;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public String getCp() {
        return cp;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setPasaporte(String pasaporte) {
        this.pasaporte = pasaporte;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public void setTelefonoFijo(String telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }
    
    
	
}
