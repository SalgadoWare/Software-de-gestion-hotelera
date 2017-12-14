/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recep_50_alejandro_checkout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import carlos.historias_8_9_10.BD_jdbc;
import carlos.historias_8_9_10.CrearReservaDialog;
import carlos.historias_8_9_10.RegistrarDatosClienteDialog;
import launcher.RecepcionistaFrame;

/**
 *
 * @author alex
 */
//prerequisito, el cliente solo puede tener una reserva confirmada en la bbdd
public class GUI extends javax.swing.JFrame {
	private boolean exito;

	/**
	 * Creates new form GUI1
	 */
	public GUI() {

		setTitle("Check out");
		initComponents();
		dni = new String[1];

		WindowAdapter exitListener = new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				RecepcionistaFrame.main(null);
			}
		};

		addWindowListener(exitListener);
		jButton1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				dni[0] = jTextField1.getText();
				dni[0] = dni[0].trim();
				char[] chararray = null;

				try {
					Integer.parseInt(dni[0].substring(0, 8));
					chararray = dni[0].substring(8).toCharArray();
					if (dni[0].length() != 9 || !Character.isLetter(chararray[0])) {
						JOptionPane.showMessageDialog(new JFrame(), "Dni incorrecto");
					} else {
						exito = true;

						cliente = BD.readCliente(dni[0]);
						listaDeReservasHabitaciones = BD.readAllReservasHabitacionConfirmadas(cliente.id);

						fillTable();

					}
				} catch (Exception es) {
					JOptionPane.showMessageDialog(new JFrame(), "Dni incorrecto");
				}

			}

			private void fillTable() {
				for (ReservaHabitacion rv : listaDeReservasHabitaciones) {
					String[] o = { rv.getIdHabitacion(), rv.getModalidad(), rv.getFechaEntrada().toString(),
							rv.getFechaSalida().toString() };
					DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
					dtm.addRow(o);
				}
			}
		});

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}

	static Cliente cliente;

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jLabel1 = new javax.swing.JLabel();
		jTextField1 = new javax.swing.JTextField();
		jScrollPane1 = new javax.swing.JScrollPane();
		jTable1 = new javax.swing.JTable();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);

		jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		jLabel1.setText("Introduce el DNI del cliente");

		jTextField1.setText("");

		jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {

		}, new String[] { "Habitacion", "Modalidad", "Fecha de entrada", "Fecha de salida" }) {
			Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class,
					java.lang.String.class };
			boolean[] canEdit = new boolean[] { false, false, false, false };

			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		jScrollPane1.setViewportView(jTable1);

		jButton1.setText("Obtener reserva");

		jButton2.setText("Obtener factura");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(21, 21, 21)
						.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jScrollPane1)
								.addGroup(layout.createSequentialGroup().addComponent(jLabel1).addGap(18, 18, 18)
										.addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 130,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jButton1)))
						.addContainerGap())
				.addGroup(layout.createSequentialGroup().addGap(153, 153, 153)
						.addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 171,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addGap(37, 37, 37)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1)
						.addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(jButton1))
				.addGap(18, 18, 18)
				.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 173,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addGap(18, 18, 18).addComponent(jButton2).addContainerGap(30, Short.MAX_VALUE)));

		pack();
	}// </editor-fold>

	String[] dni;

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed

		if (exito) {
			GUI2Factura.main(cliente, listaDeReservasHabitaciones);
		} else {
			JOptionPane.showMessageDialog(new JFrame(), "Primero busca por el dni");
		}

	}// GEN-LAST:event_jButton2ActionPerformed

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new GUI().setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTable jTable1;
	private javax.swing.JTextField jTextField1;
	private static List<recep_50_alejandro_checkout.ReservaHabitacion> listaDeReservasHabitaciones;
	// End of variables declaration//GEN-END:variables
}