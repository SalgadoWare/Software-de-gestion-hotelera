package fran.vistas;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

import fran.datos.Habitacion;
import fran.datos.ReservaHabitacion;
import fran.logica.BD_jdbc;
import fran.vistas.editores.SeleccionModalidadCellEditor;
import fran.vistas.modelo.HabitacionesTableModel;
import fran.vistas.modelo.ReservaHabitacionTableModel;
import launcher.RecepcionistaFrame;

public class BuscarHabitacion extends JDialog {
	
	RecepcionistaFrame pr;
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnCancelar;
	private JLabel lblFechaEntrada;
	private JLabel lblFechaSalida;
	static JDateChooser dcFechaEntrada;
	static JDateChooser dcFechaSalida;
	String [] titulos = {"Id-cliente", "Id-habitacion", "Modalidad", "Fecha de entrada", "Fecha de salida"};
	DefaultTableModel modelo = new DefaultTableModel(null, titulos);

	private JLabel lblEscojaLasFechas;
	private JButton btnConfirmar;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private JTable tableHab;
	private JTable tableRes;
	private JButton btnReservar;
	private JButton btnEliminar;
	
	private HabitacionesTableModel modeloHab = new HabitacionesTableModel();
	private ReservaHabitacionTableModel modeloRH = new ReservaHabitacionTableModel();
	
	/**
	 * Create the frame.
	 */
	public BuscarHabitacion(RecepcionistaFrame pr) {
		this.pr = pr;
		setForeground(Color.GRAY);
		setTitle("Reserva de Habitacion");
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 840, 483);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getBtnCancelar());
		contentPane.add(getLblFechaEntrada());
		contentPane.add(getLblFechaSalida());
		contentPane.add(getDcFechaEntrada());
		contentPane.add(getDcFechaSalida());
		contentPane.add(getLblEscojaLasFechas());
		contentPane.add(getBtnConfirmar());
		contentPane.add(getScrollPane());
		contentPane.add(getScrollPane_1());
		contentPane.add(getBtnReservar());
		contentPane.add(getBtnEliminar());
	}
	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dispose();
				}
			});
			btnCancelar.setBounds(674, 420, 89, 23);
		}
		return btnCancelar;
	}
	
	private boolean estanVacios() {
		if(dcFechaEntrada.getDate() == null || dcFechaSalida.getDate() == null){
			return true;
		}
		return false;
		
	}
	private JLabel getLblFechaEntrada() {
		if (lblFechaEntrada == null) {
			lblFechaEntrada = new JLabel("Fecha de Entrada:");
			lblFechaEntrada.setBounds(10, 102, 104, 14);
		}
		return lblFechaEntrada;
	}
	private JLabel getLblFechaSalida() {
		if (lblFechaSalida == null) {
			lblFechaSalida = new JLabel("Fecha de Salida:");
			lblFechaSalida.setBounds(0, 178, 104, 14);
		}
		return lblFechaSalida;
	}
	private JDateChooser getDcFechaEntrada() {
		if (dcFechaEntrada == null) {
			dcFechaEntrada = new JDateChooser();
			dcFechaEntrada.setBounds(108, 96, 138, 30);
			dcFechaEntrada.addPropertyChangeListener("date", this::cambianFechasReserva);
			//dcFechaSalida.setMinSelectableDate(fecha);
		}
		return dcFechaEntrada;
	}
	private JDateChooser getDcFechaSalida() {
		if (dcFechaSalida == null) {
			dcFechaSalida = new JDateChooser();
			//dcFechaSalida.setMinSelectableDate(fecha);
			dcFechaSalida.setBounds(97, 162, 138, 30);
			dcFechaSalida.addPropertyChangeListener("date", this::cambianFechasReserva);
		}
		return dcFechaSalida;
	}

	
	private boolean comprovarFecha(Date fe, Date fs) {
		if (fe == null || fs == null) {
			return false;
		}
		
		if(fe.compareTo(fs) <= 0) {
			return true;
		} else 
			return false;
		
	}
	
//	if(comprovarFecha(dcFechaEntrada.getDate(), dcFechaSalida.getDate())) {
//		JOptionPane.showMessageDialog(null, "muy bien campeon");
//		//buscarHabitacionesLibres();
//	}
//	else 
//		JOptionPane.showMessageDialog(null, "error en las fechas");
	
	private JLabel getLblEscojaLasFechas() {
		if (lblEscojaLasFechas == null) {
			lblEscojaLasFechas = new JLabel("Escoja las fechas en las que desea efectuar su reserva");
			lblEscojaLasFechas.setFont(new Font("Tahoma", Font.PLAIN, 18));
			lblEscojaLasFechas.setHorizontalAlignment(SwingConstants.CENTER);
			lblEscojaLasFechas.setBounds(10, 31, 538, 50);
		}
		return lblEscojaLasFechas;
	}
	private JButton getBtnConfirmar() {
		if (btnConfirmar == null) {
			btnConfirmar = new JButton("Confirmar");
			btnConfirmar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(estanVacios())
						JOptionPane.showMessageDialog(null, "Hay datos vacios");
					else {
						if(comprovarFecha(dcFechaEntrada.getDate(), dcFechaSalida.getDate())) {
							try {
								mostrarVentanaEscogerHabitacion();
							} catch (SQLException e) {

								e.printStackTrace();
							}

						}
							
						else JOptionPane.showMessageDialog(null, "Error en las fechas");
					}

					
				}
			});
			btnConfirmar.setBounds(575, 420, 89, 23);
		}
		return btnConfirmar;
	}
	
	
	
	private void mostrarVentanaEscogerHabitacion() throws SQLException{
		Escogerhabitacion eh = new Escogerhabitacion(this);
		eh.setLocationRelativeTo(this); //Centra la ventana en funcion a la ventana anterior
		eh.setModal(true);
		eh.setVisible(true);
		
	}


	private void mostrarHabitaciones() {
		
	}
	
	private void cambianFechasReserva(PropertyChangeEvent ev) {
		Date fechaEntrada = dcFechaEntrada.getDate();
		Date fechaSalida = dcFechaSalida.getDate();
		if (comprovarFecha(fechaEntrada, fechaSalida)) {
			try {
				List<Habitacion> habitaciones = BD_jdbc.mostrarHabitacionesLibres(fechaEntrada, fechaSalida);
				//DefaultTableModel DTM = (DefaultTableModel) tableHab.getModel();
				modeloHab.setHabitaciones(habitaciones);
				/*
				for (Habitacion habitacion : habitaciones) {
					String tipo=""; 
					if(habitacion.getTipoh()==1)
						tipo="sencilla";
					else if(habitacion.getTipoh()==2)
						tipo="doble";
					else if(habitacion.getTipoh()==3)
					tipo="triple";
					
					String[] s = {""+habitacion.getIdhab(),""+tipo};
	                añadirFila(DTM, s);
				}*/
				
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void añadirFila(javax.swing.table.DefaultTableModel DTM, String[] s){
        DTM.addRow(s);
    }
	
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setBounds(293, 102, 183, 242);
			scrollPane.setViewportView(getTableHab());
		}
		return scrollPane;
	}
	private JScrollPane getScrollPane_1() {
		if (scrollPane_1 == null) {
			scrollPane_1 = new JScrollPane();
			scrollPane_1.setBounds(542, 102, 265, 242);
			scrollPane_1.setViewportView(getTableRes());
		}
		return scrollPane_1;
	}
	private JTable getTableHab() {
		if (tableHab == null) {
			tableHab = new JTable();
			tableHab.setModel(modeloHab);
		}
		return tableHab;
	}
	private JTable getTableRes() {
		if (tableRes == null) {
			tableRes = new JTable() {

				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return column == 3;
				}
			};
			tableRes.setModel(modeloRH);
			tableRes.getColumnModel().getColumn(3).setCellEditor(new SeleccionModalidadCellEditor());
		}
		return tableRes;
	}
	private JButton getBtnReservar() {
		if (btnReservar == null) {
			btnReservar = new JButton("Reservar");
			btnReservar.setBounds(157, 269, 89, 23);
			btnReservar.addActionListener(this::reservar);
		}
		return btnReservar;
	}
	private JButton getBtnEliminar() {
		if (btnEliminar == null) {
			btnEliminar = new JButton("Eliminar");
			btnEliminar.setBounds(157, 333, 89, 23);
			btnEliminar.addActionListener(this::eliminar);
		}
		return btnEliminar;
	}
	
	private void reservar(ActionEvent ev) {
		int fila = tableHab.getSelectedRow();
		Date fechaEntrada = dcFechaEntrada.getDate();
		Date fechaSalida = dcFechaSalida.getDate();
		if (fila != -1 && fechaEntrada != null && fechaSalida != null)  {
			Habitacion hab = modeloHab.getHabitacion(fila);
			ReservaHabitacion rh = new ReservaHabitacion(fechaEntrada, fechaSalida, hab.getIdhab());
			modeloRH.aniadir(rh);
		}
	}

	private void eliminar(ActionEvent ev) {
		int fila = tableRes.getSelectedRow();
		if (fila != -1) {
			modeloRH.quitar(fila);
		}
		
	}
	
	public ReservaHabitacionTableModel getModeloRH() {
		return modeloRH;
	}
}
