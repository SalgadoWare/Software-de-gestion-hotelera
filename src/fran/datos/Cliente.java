package fran.datos;

public class Cliente {
	private String idCliente;
	private String nombre;
	public Cliente(String idCliente, String nombre) {
		super();
		this.idCliente = idCliente;
		this.nombre = nombre;
	}
	public Cliente() {
		super();
	}
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
	
	
	

}
