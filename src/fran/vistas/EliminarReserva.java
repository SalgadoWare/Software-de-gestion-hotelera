package fran.vistas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import fran.logica.BD_jdbc;
import launcher.RecepcionistaFrame;

public class EliminarReserva extends JDialog{
	
	RecepcionistaFrame pr;

	
	private JPanel contentPane;
	private JButton btnCancelar;
	private JButton btnEliminar;
	private JScrollPane scrollPane;
	public static JTable table;
	public static DefaultTableModel modelo;


	/**
	 * Create the frame.
	 * @throws SQLException 
	 */
	public EliminarReserva(RecepcionistaFrame pr) throws SQLException {
		this.pr = pr;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 977, 589);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getBtnCancelar());
		contentPane.add(getBtnEliminar());
		contentPane.add(getScrollPane());
	}
	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			btnCancelar.setBounds(862, 516, 89, 23);
		}
		return btnCancelar;
	}
	private JButton getBtnEliminar() {
		if (btnEliminar == null) {
			btnEliminar = new JButton("Eliminar");
			btnEliminar.setBounds(763, 516, 89, 23);
		}
		return btnEliminar;
	}
	private JScrollPane getScrollPane() throws SQLException {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 11, 941, 494);
			scrollPane.setViewportView(getTable());
		}
		return scrollPane;
	}
	private JTable getTable() throws SQLException {
		if (table == null) {
			modelo = new DefaultTableModel();
			table = new JTable(modelo);			
			modelo.addColumn("IdReserva");
			modelo.addColumn("IdHabitacion");
			modelo.addColumn("IdCliente");
			BD_jdbc.rellenarTabla();
		}
		return table;
	}
}
