package fran.vistas.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import fran.datos.Habitacion;

public class HabitacionesTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	private List<Habitacion> habitaciones = new ArrayList<>();

	public void setHabitaciones(List<Habitacion> habitaciones) {
		this.habitaciones = habitaciones;
		fireTableDataChanged(); //avisa de que los datos han cambiado
	}
	
	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		return habitaciones.size();
	}

	@Override
	public Object getValueAt(int fila, int columna) {
		Habitacion habitacion = habitaciones.get(fila);
		switch (columna) {
		case 0: return habitacion.getIdhab();
		case 1: return habitacion.getTipoh();
		}
		throw new RuntimeException("Columna no valida");
	}
	
	@Override
	public String getColumnName(int columna) {
		switch (columna) {
		case 0: return "ID";
		case 1: return "Tipo";
		}
		throw new RuntimeException("Columna no valida");
	}

	public Habitacion getHabitacion(int fila) {
		return habitaciones.get(fila);
	}

}
