package fran.vistas.editores;

import java.awt.Component;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import fran.logica.BD_jdbc;

public class SeleccionModalidadCellEditor extends AbstractCellEditor
	implements TableCellEditor {
	
	private static final long serialVersionUID = 1L;
	private JComboBox<String> comboModalidades;
	
	public SeleccionModalidadCellEditor() {
		List<String> modalidades = BD_jdbc.getTiposModalidad();
		comboModalidades = new JComboBox<>(modalidades.toArray(new String[0]));
	}

	@Override
	public Object getCellEditorValue() {
		return comboModalidades.getSelectedItem();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		return comboModalidades;
	}
	

}
