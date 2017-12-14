/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gert_44_alejandro;

import java.util.Date;

/**
 *
 * @author alex
 */
public class TransferTarifa {

	private Date fechaInicio, fechaFin;
	int tipo, id;
	private double precio;

	public TransferTarifa(Date fechaInicio, Date fechaFin, int i, double precio, int id) {
		this.fechaFin = fechaFin;
		this.fechaInicio = fechaInicio;
		this.precio = precio;
		this.tipo = i;
		this.id = id;
	}

	public String toString() {
		return fechaInicio.toString() + " hasta " + fechaFin.toString() + "  de  " + tipo + "  habitaciones " + precio
				+ "  euros por noche";
	}

	public int getId() {
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

}
