package fran.vistas.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import fran.datos.ReservaHabitacion;

public class ReservaHabitacionTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	private List<ReservaHabitacion> reservasHabitacion = new ArrayList<>();

	public void aniadir(ReservaHabitacion r) {
		reservasHabitacion.add(r);
		fireTableDataChanged();
	}
	
	public void quitar(int posicion) {
		reservasHabitacion.remove(posicion);
		fireTableDataChanged();
	}
	
	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public int getRowCount() {
		return reservasHabitacion.size();
	}

	@Override
	public Object getValueAt(int fila, int columna) {
		ReservaHabitacion rh = reservasHabitacion.get(fila);
		switch (columna) {
		case 0: return rh.getIdHabitacion();
		case 1: return rh.getFechaEntrada();
		case 2: return rh.getFechaSalida();
		case 3: return rh.getModalidad() == null ? "<sin seleccionar>" : rh.getModalidad();
		}
		throw new RuntimeException();
	}
	
	@Override
	public String getColumnName(int columna) {
		switch (columna) {
		case 0: return "Habitacion";
		case 1: return "Fecha entrada";
		case 2: return "Fecha salida";
		case 3: return "Modalidad";
		}
		throw new RuntimeException();
	}

	public List<ReservaHabitacion> getReservasHabitacion() {
		return reservasHabitacion;
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex == 3) {
			ReservaHabitacion rh = reservasHabitacion.get(rowIndex);
			rh.setModalidad((String)aValue);
		}
	}

}
