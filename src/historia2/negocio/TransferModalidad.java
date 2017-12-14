/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package historia2.negocio;
import java.util.Date;
/**
 *
 * @author alex
 */
public class TransferModalidad {

	private Date fechaInicio, fechaFin;
	private String tipo;
	private double precio;
	private int tipohabitacion;

	public TransferModalidad(Date fechaInicio, Date fechaFin, String tipo, double precio, int tipohabitacion) {
		this.fechaFin = fechaFin;
		this.fechaInicio = fechaInicio;
		this.precio = precio;
		this.tipo = tipo;
		this.setTipohabitacion(tipohabitacion);
	}

	public String toString() {
		return fechaInicio.toString() + " hasta " + fechaFin.toString() + "  modalidad  " + tipo + "  cuesta " + precio
				+ "  euros por noche";
	}

	public String getId() {
		return tipo;
	}

	public Date getFechaIni() {
		return fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	void setDates(Date fechaIni, Date fechaFin) {
		this.fechaInicio = fechaIni;
		this.fechaFin = fechaFin;
	}

	void setPrecio(double precio) {
		this.precio = precio;
	}

	public double getPrecio() {
		return precio;
	}

	public int getTipohabitacion() {
		return tipohabitacion;
	}

	public void setTipohabitacion(int tipohabitacion) {
		this.tipohabitacion = tipohabitacion;
	}


    
    
    
    
}
