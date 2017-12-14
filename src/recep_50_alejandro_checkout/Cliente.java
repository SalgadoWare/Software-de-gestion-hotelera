package recep_50_alejandro_checkout;

public class Cliente {


	public Cliente(String id, String targeta, String nombre, String apellidos) {
		super();
		this.id = id;
		this.targeta = targeta;
		this.nombre = nombre;
		this.apellidos = apellidos;
	}

	public String id;
	String targeta;
	String nombre;
	String apellidos;

	@Override
	public String toString() {
		return "Cliente con id " + id + ",  su targeta es " + targeta + ", su nombre es " + nombre + ", " + apellidos;
	}
	
	
}
