package fran.datos;

import java.util.Date;

public class ReservaHabitacion {

	private Date fechaEntrada;
	private Date fechaSalida;
	private int idHabitacion;
	private String modalidad;
	
	public ReservaHabitacion(Date fechaEntrada, Date fechaSalida, int idHabitacion) {
		super();
		this.fechaEntrada = fechaEntrada;
		this.fechaSalida = fechaSalida;
		this.idHabitacion = idHabitacion;
		this.modalidad = null;
	}

	public Date getFechaEntrada() {
		return fechaEntrada;
	}

	public Date getFechaSalida() {
		return fechaSalida;
	}

	public int getIdHabitacion() {
		return idHabitacion;
	}
	
	public String getModalidad() {
		return modalidad;
	}

	public void setModalidad(String modalidad) {
		this.modalidad = modalidad;
	}

}
