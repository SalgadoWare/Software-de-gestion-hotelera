package recep_50_alejandro_checkout;

import java.sql.Date;

public class ReservaHabitacion {

	private Date fechaEntrada;
	private Date fechaSalida;
	private String idHabitacion;
	private String modalidad;
	private int tipohabitacion;

	public ReservaHabitacion(Date fechaEntrada, Date fechaSalida, String idHabitacion, String modalidad, int i) {
		this.fechaEntrada = fechaEntrada;
		this.fechaSalida = fechaSalida;
		this.idHabitacion = idHabitacion;
		this.modalidad = modalidad;
		this.setTipohabitacion(i);
	}

	public Date getFechaEntrada() {
		return fechaEntrada;
	}

	public Date getFechaSalida() {
		return fechaSalida;
	}

	public String getIdHabitacion() {
		return idHabitacion;
	}

	public String getModalidad() {
		return modalidad;
	}

	public void setModalidad(String modalidad) {
		this.modalidad = modalidad;
	}

	@Override
	public String toString() {
		return "ReservaHabitacion{" + "fechaEntrada=" + fechaEntrada + ", fechaSalida=" + fechaSalida
				+ ", idHabitacion=" + idHabitacion + ", modalidad=" + modalidad + '}' + " tipo " + this.tipohabitacion;
	}

	public int getTipohabitacion() {
		return tipohabitacion;
	}

	public void setTipohabitacion(int tipohabitacion) {
		this.tipohabitacion = tipohabitacion;
	}

}
