package recep_52_alejandro_checkin;

import java.util.Date;

public class TransferAcompanante {
	public TransferAcompanante(String nombre, String apellidos, String dni, String pasaporte, Date nace) {
		super();
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.dni = dni;
		this.pasaporte = pasaporte;
		this.nace = nace;
	}
	String nombre, apellidos, dni, pasaporte;
	Date nace;
	
}
