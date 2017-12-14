/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recep_52_alejandro_checkin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import launcher.RecepcionistaFrame;
import recep_50_alejandro_checkout.Cliente;
import recep_50_alejandro_checkout.ReservaHabitacion;
import recep_52_alejandro_checkin.carlos.BD_jdbc;
import recep_52_alejandro_checkin.carlos.CrearReservaDialog;

/**
 *
 * @author alex
 */
// prerequisito, el cliente solo puede tener una reserva confirmada en la bbdd
public class GUI extends javax.swing.JFrame {
	private boolean exito;

	/**
	 * Creates new form GUI1
	 */
	public GUI() {

		setTitle("Check in");
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

						if (cliente == null) {

							JOptionPane.showMessageDialog(new JFrame(),
									"Es un cliente nuevo, se procede a hacer la reserva y después guardar sus datos");

							// nuevo
							// mostraraltacliente
							// mostraranadirreserva
							// anadirAcompanantes

							mostrarCrearReservaCarlos();
							JOptionPane.showMessageDialog(new JFrame(),
									"Reserva confirmada con exito, se procede a registrar a los acompanantes");
							anadirAcompanantes(null);

						}

						else {

							System.out.println(cliente);
							listaDeReservasHabitaciones = BD.readAllReservasHabitacionSinConfirmadas(cliente.id);

							if (listaDeReservasHabitaciones == null || listaDeReservasHabitaciones.isEmpty()) {
								JOptionPane.showMessageDialog(new JFrame(),
										"Cliente existente en el sistema, se procede a realizar la reserva\n"
												+ cliente);

								// nuevo
								// mostraranadirreserva
								// anadirAcompanantes

								mostrarCrearReserva();
								jButton2ActionPerformed(null);

							} else {
								JOptionPane.showMessageDialog(new JFrame(),
										"El cliente ya tiene una reserva, se muestra en la tabla\n" + cliente);
								fillTable();
								showButton();
							}

						}

					}
				} catch (Exception es) {
					JOptionPane.showMessageDialog(new JFrame(), "DNI incorrecto");
					System.out.println(es);
				}

			}

			private void showButton() {
				jButton2.setEnabled(true);
				jButton2.setVisible(true);
				jButton3.setEnabled(true);
				jButton3.setVisible(true);
			}

			private void fillTable() {
				for (ReservaHabitacion rv : listaDeReservasHabitaciones) {
					String[] o = { rv.getIdHabitacion(), rv.getModalidad(), rv.getFechaEntrada().toString(),
							rv.getFechaSalida().toString() };
					DefaultTableModel dtm = (DefaultTableModel) jTable1.getModel();
					dtm.addRow(o);
					System.out.println(rv.getTipohabitacion());
				}
			}
		});

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		jButton2.setEnabled(false);
		jButton2.setVisible(false);
		jButton3.setEnabled(false);
		jButton3.setVisible(false);
	}

	static Cliente cliente;

	private void initComponents() {

		jScrollPane1 = new javax.swing.JScrollPane();
		jTable1 = new javax.swing.JTable();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jLabel1 = new javax.swing.JLabel();
		jTextField1 = new javax.swing.JTextField();
		jButton3 = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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

		jButton1.setText("Buscar");

		jButton2.setText("Confirmar reserva");
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});

		jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		jLabel1.setText("Introduce el DNI del cliente");

		jTextField1.setText("");

		jButton3.setText("Actualizar Reserva");
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton3ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap(30, Short.MAX_VALUE).addGroup(layout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(layout.createSequentialGroup()
												.addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 180,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 181,
														javax.swing.GroupLayout.PREFERRED_SIZE))
										.addComponent(jScrollPane1)
										.addGroup(layout.createSequentialGroup().addComponent(jLabel1)
												.addGap(18, 18, 18)
												.addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 130,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(jButton1)))
								.addContainerGap(30, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addGap(37, 37, 37)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1)
						.addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addComponent(jButton1))
				.addGap(18, 18, 18)
				.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 171,
						javax.swing.GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton2)
						.addComponent(jButton3))
				.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		pack();
	}

	JButton jButton3 = new JButton("Actualizar reserva");
	String[] dni;

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton2ActionPerformed
		BD.updateReservaConfirmada(cliente.id);
		JOptionPane.showMessageDialog(new JFrame(),
				"Reserva confirmada con exito, se procede a registrar a los acompanantes");

		dispose();
		// String[] args = new String[1];
		// args[0] = listaDeReservasHabitaciones.get(0).getIdHabitacion();
		anadirAcompanantes(null);

	}// GEN-LAST:event_jButton2ActionPerformed

	private void anadirAcompanantes(String[] args) {
		RegistrarAcompanantes.main(args);
	}

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

	int idReserva;

	private void jButton3ActionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		idReserva = BD.readIDReserva(cliente.id);
		BD.deleteReservasHabitacion(idReserva);
		System.out.println(idReserva);

		mostrarUpdateReserva(listaDeReservasHabitaciones, idReserva);
		jButton2ActionPerformed(null);

		// nuevo
		// mostrarActualizarReserva(listadereservasHabitaciones, idReserva);
		// jButton2ActionPerfermed
	}

	private void mostrarUpdateReserva(List<ReservaHabitacion> lista, int idReserva) {
		UpdateReservaDialog crearReservaDialog = new UpdateReservaDialog(lista, idReserva);
		// Lanzamos el diálogo
		if (crearReservaDialog.showDialog()) {
			// Si se ha pulsado aceptar tenemos que guardar toda la info en la
			// BD

			// Sacamos los datos
			int idCliente = crearReservaDialog.getIdCliente();
			String tarjeta = crearReservaDialog.getTarjeta();

			List<recep_52_alejandro_checkin.carlos.ReservaHabitacion> listaReservaHabitacion = crearReservaDialog
					.getListaReservaHabitacion();

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

			// Traza
			System.out.println("\nDatos para crear la reserva:");
			System.out.println("IdCliente: " + idCliente);
			System.out.println("Tarjeta: " + tarjeta);

			System.out.println("Datos de las reservaHabitacion: ");
			for (recep_52_alejandro_checkin.carlos.ReservaHabitacion reservaHabitacion : listaReservaHabitacion) {
				System.out.println(reservaHabitacion.toString());
			}

			// Guardamos la info en la BD

			boolean creada = BD_jdbc.updateReserva(idReserva, listaReservaHabitacion);
			if (creada)
				System.out.println("Se ha creado la reserva");
			else
				System.out.println("Error creando la reserva");

		}
	}

	private void mostrarCrearReservaCarlos() {

		carlos.historias_8_9_10.CrearReservaDialog crearReservaDialog = new carlos.historias_8_9_10.CrearReservaDialog();
		// Lanzamos el diálogo
		if (crearReservaDialog.showDialog()) {
			// Si se ha pulsado aceptar tenemos que guardar toda la info en la
			// BD

			// Sacamos los datos
			String nombre = crearReservaDialog.getNombre();
			String apellidos = crearReservaDialog.getApellidos();
			String dni = crearReservaDialog.getDni();
			String pasaporte = crearReservaDialog.getPasaporte();
			String tipoTarjeta = crearReservaDialog.getTipoTarjeta();
			String numeroTarjeta = crearReservaDialog.getNumeroTarjeta();
			String direccion = crearReservaDialog.getDireccion();
			String poblacion = crearReservaDialog.getPoblacion();
			String cp = crearReservaDialog.getCP();
			String telefonoFijo = crearReservaDialog.getTelefonoFijo();
			String telefonoMovil = crearReservaDialog.getTelefonoMovil();
			String email = crearReservaDialog.getEmail();
			LocalDate fechaNacimiento = crearReservaDialog.getFechaNacimiento();
			boolean clienteYaExiste = crearReservaDialog.clienteYaExiste();
			boolean modificarDatosCliente = crearReservaDialog.modificarDatosCliente();
			boolean nuevoCliente = crearReservaDialog.nuevoCliente();

			System.out.println("nclienteYaExiste: " + clienteYaExiste);
			System.out.println("modificarDatosCliente: " + modificarDatosCliente);
			System.out.println("nuevoCliente: " + nuevoCliente);

			// Necesitamos el idCliente para crear la reserva
			int idCliente;

			// Si el cliente es nuevo
			if (nuevoCliente) {
				// Creamos el cliente (y que nos retorne su idCliente en ese
				// metodo???)
				idCliente = carlos.historias_8_9_10.BD_jdbc.crearCliente(nombre, apellidos, dni, pasaporte, tipoTarjeta,
						numeroTarjeta, direccion, poblacion, cp, telefonoFijo, telefonoMovil, email, fechaNacimiento);// insert
			}

			else {
				// Si ya existe

				// Sacamos su idCliente (usando el dni o el pasaporte)
				Map<String, Object> datosCliente;

				if (dni != null)// Si no es nulo sacamos los datos con el dni
					datosCliente = carlos.historias_8_9_10.BD_jdbc.dameDatosClienteConDni(dni);
				else// Si el dni es nulo, sacamos los datos del pasaporte (no
					// pueden ser ambos nulos, asi que si el dni es nulo, el
					// pasaporte tiene que tener valor)
					datosCliente = carlos.historias_8_9_10.BD_jdbc.dameDatosClienteConPasaporte(pasaporte);

				idCliente = (Integer) datosCliente.get("idCliente");

				// Si se modifican sus datos
				if (modificarDatosCliente) {
					// Actualizamos los datos en la BD
					// Le pasamos toda la info y que la sobrescriba (si no
					// cambio no va a pasar nada)
					carlos.historias_8_9_10.BD_jdbc.modificarDatosCliente(idCliente, nombre, apellidos, dni, pasaporte,
							tipoTarjeta, numeroTarjeta, direccion, poblacion, cp, telefonoFijo, telefonoMovil, email,
							fechaNacimiento);// update
				}

			}

			List<carlos.historias_8_9_10.ReservaHabitacion> listaReservaHabitacion = crearReservaDialog
					.getListaReservaHabitacion();

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

			// Traza
			System.out.println("\nDatos para crear la reserva:");
			System.out.println("\nDatos del cliente:");
			System.out.println("IdCliente: " + idCliente);
			System.out.println("Nombre: " + nombre);
			System.out.println("Apellidos: " + apellidos);
			System.out.println("dni: " + dni);
			System.out.println("pasaporte: " + pasaporte);
			System.out.println("tipoTarjeta: " + tipoTarjeta);
			System.out.println("numeroTarjeta: " + numeroTarjeta);

			System.out.println("Datos de las reservaHabitacion: ");
			for (carlos.historias_8_9_10.ReservaHabitacion reservaHabitacion : listaReservaHabitacion) {
				System.out.println(reservaHabitacion.toString());
			}

			// Guardamos la info en la BD

			boolean creada = carlos.historias_8_9_10.BD_jdbc.crearReserva(idCliente, listaReservaHabitacion);
			if (creada)
				System.out.println("Se ha creado la reserva");
			else
				System.out.println("Error creando la reserva");

		}
	}

	private void mostrarCrearReserva() {
		CrearReservaDialog crearReservaDialog = new CrearReservaDialog(Integer.valueOf(cliente.id));
		// Lanzamos el diálogo
		if (crearReservaDialog.showDialog()) {
			// Si se ha pulsado aceptar tenemos que guardar toda la info en la
			// BD

			// Sacamos los datos
			int idCliente = crearReservaDialog.getIdCliente();
			String tarjeta = crearReservaDialog.getTarjeta();

			List<recep_52_alejandro_checkin.carlos.ReservaHabitacion> listaReservaHabitacion = crearReservaDialog
					.getListaReservaHabitacion();

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

			// Traza
			System.out.println("\nDatos para crear la reserva:");
			System.out.println("IdCliente: " + idCliente);
			System.out.println("Tarjeta: " + tarjeta);

			System.out.println("Datos de las reservaHabitacion: ");
			for (recep_52_alejandro_checkin.carlos.ReservaHabitacion reservaHabitacion : listaReservaHabitacion) {
				System.out.println(reservaHabitacion.toString());
			}

			// Guardamos la info en la BD

			boolean creada = BD_jdbc.crearReserva(idCliente, listaReservaHabitacion);
			if (creada)
				System.out.println("Se ha creado la reserva");
			else
				System.out.println("Error creando la reserva");

		}
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
