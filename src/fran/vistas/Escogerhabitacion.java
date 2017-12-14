package fran.vistas;

import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;

//import datos.Habitacion;
//import logica.BD_jdbc;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import fran.datos.Reserva;
import fran.datos.ReservaHabitacion;
import fran.logica.BD_jdbc;

public class Escogerhabitacion extends JDialog {
	
	private BuscarHabitacion bh;

	private JPanel contentPane;
	private JLabel lblDnicliente;
	private JTextField txDNI;
	private JLabel lblTarjeta;
	private JTextField txTarjeta;
	private JButton btnAtras;
	private JButton btnAceptar;
	private JScrollPane scrollPane;
	private JTable tableConfirmarReserva;



	/**
	 * Create the frame.
	 * @throws SQLException 
	 */
	public Escogerhabitacion( BuscarHabitacion bh) throws SQLException {
		this.bh = bh;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 701, 436);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getLblDnicliente());
		contentPane.add(getTxDNI());
		contentPane.add(getLblTarjeta());
		contentPane.add(getTxTarjeta());
		contentPane.add(getBtnAtras());
		contentPane.add(getBtnAceptar());
		contentPane.add(getScrollPane());
	}
	
	private java.sql.Date convertirFecha(Date date) {
		java.util.Date utilDate = date;
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		return sqlDate;
	}
	
//	private void llenarCB() {
//		cbHabitacion.removeAll();
//		List<Habitacion> habitaciones;
//		habitaciones = BD_jdbc.mostrarHabitacionesLibres(recepcionista.dcFechaEntrada.getDate(), recepcionista.dcFechaSalida.getDate());
//		
//	//como pasar de date a java,sql.date
//		
//	}
	
	private JLabel getLblDnicliente() {
		if (lblDnicliente == null) {
			lblDnicliente = new JLabel("DNICliente");
			lblDnicliente.setBounds(10, 45, 70, 14);
		}
		return lblDnicliente;
	}
	private JTextField getTxDNI() {
		if (txDNI == null) {
			txDNI = new JTextField();
			txDNI.setBounds(90, 42, 233, 20);
			txDNI.setColumns(10);
		}
		return txDNI;
	}
	private JLabel getLblTarjeta() {
		if (lblTarjeta == null) {
			lblTarjeta = new JLabel("tarjeta");
			lblTarjeta.setBounds(10, 90, 70, 14);
		}
		return lblTarjeta;
	}
	private JTextField getTxTarjeta() {
		if (txTarjeta == null) {
			txTarjeta = new JTextField();
			txTarjeta.setBounds(90, 87, 233, 20);
			txTarjeta.setColumns(10);
		}
		return txTarjeta;
	}
	private JButton getBtnAtras() {
		if (btnAtras == null) {
			btnAtras = new JButton("Atras");
			btnAtras.setBounds(586, 363, 89, 23);
		}
		return btnAtras;
	}
	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.setBounds(487, 363, 89, 23);
			btnAceptar.addActionListener(this::crearReserva);
		}
		return btnAceptar;
	}
	
	private int obtenerIdHab(String cadena) {
		int i = 0;
		String[] c1 = cadena.split(":");
		String c2 = c1[0];
		i = Integer.parseInt(c2);
		return i;
	}
	
	public String convertirFechaString(Date date){
		return new SimpleDateFormat("dd-MM-yyyy").format(date);
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setBounds(90, 149, 248, 145);
			scrollPane.setViewportView(getTableConfirmarReserva());
		}
		return scrollPane;
	}
	private JTable getTableConfirmarReserva() {
		if (tableConfirmarReserva == null) {
			tableConfirmarReserva = new JTable();
			tableConfirmarReserva.setCellSelectionEnabled(true);
			tableConfirmarReserva.setModel(bh.getModeloRH());
		}
		return tableConfirmarReserva;
	}
	
	private void crearReserva(ActionEvent e) {
		
		String tarjeta = txTarjeta.getText();
		int idCliente = Integer.parseInt(txDNI.getText());
		
		Reserva reserva = new Reserva(tarjeta, idCliente);
		List<ReservaHabitacion> rhs = bh.getModeloRH().getReservasHabitacion();
		
		BD_jdbc.crearReserva(reserva, rhs);
		
		JOptionPane.showMessageDialog(this, "Reserva realizada");
		
		dispose();
		this.bh.dispose();
	}
	
}
