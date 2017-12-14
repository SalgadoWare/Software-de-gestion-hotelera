package codigo;

public class TransferTrabajador {
String id, nombre, tipo, dni;

public TransferTrabajador(String id, String nombre, String tipo, String dni) {
	this.id = id;
	this.nombre = nombre;
	this.tipo = tipo;
	this.dni = dni;
}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getNombre() {
	return nombre;
}

public void setNombre(String nombre) {
	this.nombre = nombre;
}

public String getTipo() {
	return tipo;
}

public void setTipo(String tipo) {
	this.tipo = tipo;
}

public String getDni() {
	return dni;
}

public void setDni(String dni) {
	this.dni = dni;
}

@Override
public String toString() {
	return "TransferTrabajador [id=" + id + ", nombre=" + nombre + ", tipo=" + tipo + ", dni=" + dni + "]";
}
}
