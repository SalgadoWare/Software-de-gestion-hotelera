package gert_41_alejandro;


import java.util.Date;

public class TrabajadoresConEstado {
	public TrabajadoresConEstado( String nombre, String dni, String tipo, String estado,
			Date inicio, Date fin) {
		this.nombre = nombre;
		this.dni = dni;
		this.tipo = tipo;
		this.estado = estado;
		this.inicio = inicio;
		this.fin = fin;
	}
	@Override
	public String toString() {
		return  nombre.toUpperCase() + ", con DNI " + dni + " (" + tipo + ") esta en estado " + estado.toUpperCase()
				;
	}
	public String getId_trabajador() {
		return id_trabajador;
	}
	public void setId_trabajador(String id_trabajador) {
		this.id_trabajador = id_trabajador;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Date getInicio() {
		return inicio;
	}
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	public Date getFin() {
		return fin;
	}
	public void setFin(Date fin) {
		this.fin = fin;
	}
	String id_trabajador, nombre, dni, tipo, estado;
	Date inicio, fin;

}
