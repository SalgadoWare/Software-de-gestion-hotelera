package launcher;

import carlos.historias_8_9_10.AnularReservaDialog;
import carlos.historias_8_9_10.BD_jdbc;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import carlos.historias_8_9_10.CrearReservaDialog;
import carlos.historias_8_9_10.ReservaHabitacion;
import carlos_historias_4_5_6_7.ConsultarTareasLimpiezaDialog;
import carlos_historias_4_5_6_7.CrearTareaLimpiezaDialog;
import fran.vistas.BuscarHabitacion;
import fran.vistas.EliminarReserva;
import jdbc.BD;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class RecepcionistaFrame extends JFrame {

	private JPanel contentPane;
	private JButton bAnularReserva;
	private JButton bCrearTareaLimpieza;
	
	//Creamos los dialogos
    private  CrearTareaLimpiezaDialog crearTareaLimpiezaDialog;
    private  CrearReservaDialog crearReservaDialog;
    private  AnularReservaDialog anularReservaDialog;
    
    private JButton bAtras;
    private JButton bAniadirReserva;
    private JButton btnCheckIn;
    private JButton btnCheckOut;
    
    public void iniciarComponentesCarlos(){
        crearTareaLimpiezaDialog = new CrearTareaLimpiezaDialog();
        crearReservaDialog = new CrearReservaDialog();
        anularReservaDialog=new AnularReservaDialog();
    }

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RecepcionistaFrame frame = new RecepcionistaFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RecepcionistaFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 612, 262);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.add(getBAnularReserva());
	//	contentPane.add(getBtnAñadir());
//<<<<<<< HEAD
//	//	contentPane.add(getBtnEliminar());
//=======
////<<<<<<< HEAD
////	//	contentPane.add(getBtnEliminar());
////=======
////		//contentPane.add(getBtnEliminar());
////>>>>>>> branch 'master' of https://uo250707@bitbucket.org/uo250707/ips-pl3-2.git
//>>>>>>> refs/heads/alex_remoto
		contentPane.add(getBCrearTareaLimpieza());
		contentPane.add(getBAtras());
		contentPane.add(getBAniadirReserva());
		contentPane.add(getBtnCheckIn());
		contentPane.add(getBtnCheckOut());
		
		iniciarComponentesCarlos();
	}
	
	private JButton getBAnularReserva() {
		if (bAnularReserva == null) {
			bAnularReserva = new JButton("Anular Reserva");
			bAnularReserva.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					mostrarAnularReserva();
				}
			});
			bAnularReserva.setBounds(227, 32, 164, 63);
		}
		return bAnularReserva;
	}
	
	private void mostrarEliminarReserva() throws SQLException {
		EliminarReserva er = new EliminarReserva(this);
		er.setLocationRelativeTo(this); //Centra la ventana en funcion a la ventana anterior
		er.setModal(true);
		er.setVisible(true);
	}
	private JButton getBCrearTareaLimpieza() {
		if (bCrearTareaLimpieza == null) {
			bCrearTareaLimpieza = new JButton("Crear Tarea de Limpieza");
			bCrearTareaLimpieza.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					mostrarCrearTareaLimpieza();
				}
			});
			bCrearTareaLimpieza.setBounds(403, 32, 164, 63);
		}
		return bCrearTareaLimpieza;
	}
	
	private void mostrarCrearTareaLimpieza(){
		//Lanzamos el diálogo
        if(crearTareaLimpiezaDialog.showDialog()){
            //Si se ha pulsado aceptar tenemos que guardar toda la info en la BD
        	
            //Sacamos los datos
            java.sql.Timestamp momento_informada = crearTareaLimpiezaDialog.getMomentoInformada();
            java.sql.Timestamp fechaTarea = crearTareaLimpiezaDialog.getFechaTarea();
            //Si el timestamp anterior es null es que el usuario no ha introducido ningun valor en el campo fechaTarea,
            //por lo que este ha de tomar el mismo valor que momento_informada
            if(fechaTarea==null)
                fechaTarea=momento_informada;
            
            String numHabitacion = crearTareaLimpiezaDialog.getHabitacion();
            String tipo = crearTareaLimpiezaDialog.getTipoTarea();
            String observaciones = crearTareaLimpiezaDialog.getObservaciones();
            boolean prioritaria = crearTareaLimpiezaDialog.isPrioritaria();
                        
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            //Traza
            System.out.println("\nDatos para crear tarea de limpieza:");
            System.out.println("Habitación: "+ numHabitacion);
            System.out.println("Momento informada: "+sdf.format(momento_informada));
            System.out.println("Fecha Tarea: "+sdf.format(fechaTarea));
            System.out.println("Tipo: "+tipo);
            System.out.println("Observaciones: "+observaciones);
            System.out.println("Prioritaria: "+prioritaria);

            //Guardamos la info en la BD
            boolean creada = BD.crearTareaLimpieza(numHabitacion, fechaTarea, momento_informada,tipo, observaciones, prioritaria);
            if(creada)
                System.out.println("Se ha creado la tarea de limpieza");
            else System.out.println("Error creando la tarea de limpieza");
        }
	}
	
	private void mostrarAnularReserva(){
            //Lanzamos el diálogo
            if(anularReservaDialog.showDialog()){}
	}
	
	private void mostrarCrearReserva(){
            //Lanzamos el diálogo
            if(crearReservaDialog.showDialog()){
                //Si se ha pulsado aceptar tenemos que guardar toda la info en la BD

                //Sacamos los datos
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
                
                
                //Necesitamos el idCliente para crear la reserva
                int idCliente;
                
                //Si el cliente es nuevo
                if(nuevoCliente){
                    //Creamos el cliente (y que nos retorne su idCliente en ese metodo???)
                    idCliente = BD_jdbc.crearCliente(nombre,apellidos,dni,pasaporte,tipoTarjeta,numeroTarjeta,
                            direccion,poblacion,cp,telefonoFijo,telefonoMovil,email,fechaNacimiento);//insert
                }
                
                else{
                    //Si ya existe
                    
                    //Sacamos su idCliente (usando el dni o el pasaporte)
                    Map<String,Object> datosCliente;
                    
                    if(dni != null)//Si no es nulo sacamos los datos con el dni
                        datosCliente = BD_jdbc.dameDatosClienteConDni(dni);
                    else//Si el dni es nulo, sacamos los datos del pasaporte (no pueden ser ambos nulos, asi que si el dni es nulo, el pasaporte tiene que tener valor)
                        datosCliente = BD_jdbc.dameDatosClienteConPasaporte(pasaporte);
                    
                    idCliente = (Integer) datosCliente.get("idCliente");
                    
                    //Si se modifican sus datos 
                    if(modificarDatosCliente){
                        //Actualizamos los datos en la BD
                        //Le pasamos toda la info y que la sobrescriba (si no cambio no va a pasar nada)
                        BD_jdbc.modificarDatosCliente(idCliente,nombre,apellidos,dni,pasaporte,tipoTarjeta,numeroTarjeta,
                                direccion,poblacion,cp,telefonoFijo,telefonoMovil,email,fechaNacimiento);//update
                    }
                    
                }
                    
                    
                    
                
                List<ReservaHabitacion> listaReservaHabitacion = crearReservaDialog.getListaReservaHabitacion();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                //Traza
                System.out.println("\nDatos para crear la reserva:");
                System.out.println("\nDatos del cliente:");
                System.out.println("IdCliente: "+ idCliente);
                System.out.println("Nombre: "+nombre);
                System.out.println("Apellidos: "+apellidos);
                System.out.println("dni: "+dni);
                System.out.println("pasaporte: "+pasaporte);
                System.out.println("tipoTarjeta: "+tipoTarjeta);
                System.out.println("numeroTarjeta: "+numeroTarjeta);
                
                System.out.println("Datos de las reservaHabitacion: ");
                for (ReservaHabitacion reservaHabitacion : listaReservaHabitacion) {
                    System.out.println(reservaHabitacion.toString());
                }

                //Guardamos la info en la BD
                
                boolean creada = BD_jdbc.crearReserva(idCliente,listaReservaHabitacion);
                if(creada)
                    System.out.println("Se ha creado la reserva");
                else System.out.println("Error creando la reserva");
                
            }
	}
	
	
	private JButton getBAtras() {
		if (bAtras == null) {
			bAtras = new JButton();
			bAtras.setText("Atras");
			bAtras.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					VolverAtras();
				}
			});
			bAtras.setBounds(403, 130, 164, 63);
		}
		return bAtras;
	}
	
	private void VolverAtras(){
		dispose();
		ActoresFrame.main(null);
	}
	private JButton getBAniadirReserva() {
		if (bAniadirReserva == null) {
			bAniadirReserva = new JButton("Crear reserva");
			bAniadirReserva.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					mostrarCrearReserva();
				}
			});
			bAniadirReserva.setBounds(52, 32, 164, 63);
		}
		return bAniadirReserva;
	}
	private JButton getBtnCheckIn() {
		if (btnCheckIn == null) {
			btnCheckIn = new JButton();
			btnCheckIn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					recep_52_alejandro_checkin.GUI.main(null);
				}
			});
			btnCheckIn.setText("Check in");
			btnCheckIn.setBounds(52, 130, 164, 63);
		}
		return btnCheckIn;
	}
	private JButton getBtnCheckOut() {
		if (btnCheckOut == null) {
			btnCheckOut = new JButton();
			btnCheckOut.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					recep_50_alejandro_checkout.GUI.main(null);
				}
			});
			btnCheckOut.setText("Check out");
			btnCheckOut.setActionCommand("Recepcionista: Check out");
			btnCheckOut.setBounds(227, 130, 164, 63);
		}
		return btnCheckOut;
	}
}
