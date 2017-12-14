package fran.logica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fran.datos.Habitacion;
import fran.datos.Reserva;
import fran.datos.ReservaHabitacion;
import fran.vistas.EliminarReserva;

public class BD_jdbc {
	/*
	public static void main(String[] args) {
		try {
			int diaInicio = 1;
			int mesInicio = 11;
			int añoInicio = 2017;

			int diaFin = 10;
			int mesFin = 11;
			int añoFin = 2017;

			crearPromocion(new java.sql.Date(añoInicio,mesInicio,diaInicio), new java.sql.Date(añoFin,mesFin,diaFin), 30);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/

	/**
	*Este método establece la conexion con la BD del servidor DESA de la Escuela de Informatica
	*Devuelve un objeto de tipo Connection
	*/
	public static Connection login() throws SQLException
	{
		DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
		String url = "jdbc:oracle:thin:@156.35.94.99:1521:DESA";
		String username = "UO267399";
		String password = "UO2673991";
		return DriverManager.getConnection(url, username, password);
	}

//	@SuppressWarnings("finally")
//	public static boolean crearPromocion(java.sql.Date fechaInicio, java.sql.Date fechaFin, int porcentaje){ // throws SQLException {
//		//Por defecto asumimos que no se va a introducir bien
//		boolean introducidoCorrectamente = false;
//
//		Connection con = null;
//		Statement st = null;
//		ResultSet rs = null;
//		PreparedStatement ps = null;
//
//		try{
//			//Conectamos con la BD
//			con = login();
//
//			//Sacamos el maximo valor de Id que haya en la tabla (Oracle no tiene ID autoincremental)
//			int maxId = 0;
//			st = con.createStatement();
//			String query = "SELECT MAX(IdExtra) from EXTRAS";
//			rs = st.executeQuery(query);
//			while(rs.next())
//				maxId=rs.getInt(1);
//			//El id de la tupla que tenemos que añadir ahora sera maxId+1
//			int siguienteId = maxId + 1;
//
//			//Creamos ahora el INSERT (Usando un PreparedStatement porque tenemos parametros)
//			String insert = "INSERT INTO EXTRAS VALUES(?,?,?,?,'promocion')";
//			ps = con.prepareStatement(insert);
//			ps.setInt(1, siguienteId);
//			ps.setDate(2, fechaInicio);
//			ps.setDate(3, fechaFin);
//			ps.setInt(4, porcentaje);
//
//			//Ejecutamos el insert y si se ejecuto correctamente nos devuelve 1
//			if(ps.executeUpdate()==1){
//				System.out.println("Introducido correctamente");
//				introducidoCorrectamente=true;
//			}
//
//		}catch(SQLException e){
//			//e.printStackTrace();
//                        System.out.print(e.getMessage());
//                        //El codigo de error 1 es el que nos va a indicar que se ha violado una restriccion UNIQUE
//                        if(e.getErrorCode()==1)//Es decir, que ya hay una promocion o un descuento para las fechas introducidas
//                            System.out.println("Ya hay una promoción o un suplemento para esas fechas");
//		}finally{
//			//Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
//			try{
//				//El problema es que al cerrar las conexiones tambien se puede producir una excepcion
//				if(rs!=null)
//					rs.close();
//				if(st!=null)
//					st.close();
//				if(ps!=null)
//					ps.close();
//				if(con!=null)
//					con.close();
//			}catch(SQLException e){
//				e.printStackTrace();
//			}finally{
//				return introducidoCorrectamente;
//			}
//		}
//	}
//
//	@SuppressWarnings("finally")
//	public static boolean crearSuplemento(java.sql.Date fechaInicio, java.sql.Date fechaFin, int porcentaje){ // throws SQLException {
//		//Por defecto asumimos que no se va a introducir bien
//		boolean introducidoCorrectamente = false;
//
//		Connection con = null;
//		Statement st = null;
//		ResultSet rs = null;
//		PreparedStatement ps = null;
//
//		try{
//			//Conectamos con la BD
//			con = login();
//
//			//Sacamos el maximo valor de Id que haya en la tabla (Oracle no tiene ID autoincremental)
//			int maxId = 0;
//			st = con.createStatement();
//			String query = "SELECT MAX(IdExtra) from EXTRAS";
//			rs = st.executeQuery(query);
//			while(rs.next())
//				maxId=rs.getInt(1);
//			//El id de la tupla que tenemos que añadir ahora sera maxId+1
//			int siguienteId = maxId + 1;
//
//			//Creamos ahora el INSERT (Usando un PreparedStatement porque tenemos parametros)
//			String insert = "INSERT INTO EXTRAS VALUES(?,?,?,?,'suplemento')";
//			ps = con.prepareStatement(insert);
//			ps.setInt(1, siguienteId);
//			ps.setDate(2, fechaInicio);
//			ps.setDate(3, fechaFin);
//			ps.setInt(4, porcentaje);
//
//			//Ejecutamos el insert y si se ejecuto correctamente nos devuelve 1
//			if(ps.executeUpdate()==1){
//				System.out.println("Introducido correctamente");
//				introducidoCorrectamente=true;
//			}
//
//		}catch(SQLException e){
//			//e.printStackTrace();
//                        System.out.print(e.getMessage());
//                        //El codigo de error 1 es el que nos va a indicar que se ha violado una restriccion UNIQUE
//                        if(e.getErrorCode()==1)//Es decir, que ya hay una promocion o un descuento para las fechas introducidas
//                            System.out.println("Ya hay una promoción o un suplemento para esas fechas");
//		}finally{
//			//Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
//			try{
//				//El problema es que al cerrar las conexiones tambien se puede producir una excepcion
//				if(rs!=null)
//					rs.close();
//				if(st!=null)
//					st.close();
//				if(ps!=null)
//					ps.close();
//				if(con!=null)
//					con.close();
//			}catch(SQLException e){
//				e.printStackTrace();
//			}finally{
//				return introducidoCorrectamente;
//			}
//		}
//	}
	
	private static void crearReservaHabitacion(int idReserva, ReservaHabitacion rh, Connection c) throws SQLException{
		
		PreparedStatement sent = null;
		try {
			
			sent = c.prepareStatement("INSERT INTO reserva_habitacion VALUES (?, ?, ?, ?, ?)");
			sent.setInt(1, rh.getIdHabitacion());
			sent.setInt(2, idReserva);
			sent.setDate(3, new java.sql.Date( rh.getFechaEntrada().getTime()));
			sent.setDate(4, new java.sql.Date( rh.getFechaSalida().getTime()));
			sent.setString(5, rh.getModalidad());
			
			sent.executeUpdate();
		}
		finally {
			if (sent != null) {
				sent.close();
			}
		}
	}
	
	@SuppressWarnings("finally")
	public static boolean crearReserva(Reserva reserva, List<ReservaHabitacion> reservaHabitaciones){ // throws SQLException {
		//Por defecto asumimos que no se va a introducir bien
		boolean introducidoCorrectamente = false;

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try{
			//Conectamos con la BD
			con = login();

			//Sacamos el maximo valor de Id que haya en la tabla (Oracle no tiene ID autoincremental)
			int maxId = 0;
			st = con.createStatement();
			String query = "SELECT MAX(IdReserva) from RESERVA";
			rs = st.executeQuery(query);
			while(rs.next())
				maxId=rs.getInt(1);
			//El id de la tupla que tenemos que añadir ahora sera maxId+1
			int siguienteId = maxId + 1;

			//Creamos ahora el INSERT (Usando un PreparedStatement porque tenemos parametros)
			String insert = "INSERT INTO RESERVA VALUES(?,?,?)";
			ps = con.prepareStatement(insert);
			ps.setInt(1, siguienteId);
			ps.setString(2, reserva.getTarjeta());
			ps.setInt(3, reserva.getIdCliente());
			

			//Ejecutamos el insert y si se ejecuto correctamente nos devuelve 1
			if(ps.executeUpdate()==1){
				System.out.println("Introducido correctamente");
				introducidoCorrectamente=true;
				
				for (ReservaHabitacion rh: reservaHabitaciones) {
					crearReservaHabitacion(siguienteId, rh, con);
				}
			}

		}catch(SQLException e){
			e.printStackTrace();
            System.out.print(e.getMessage());

		}finally{
			//Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
			try{
				//El problema es que al cerrar las conexiones tambien se puede producir una excepcion
				if(rs!=null)
					rs.close();
				if(st!=null)
					st.close();
				if(ps!=null)
					ps.close();
				if(con!=null)
					con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}finally{
				return introducidoCorrectamente;
			}
		}
	}
	
	
	
	@SuppressWarnings("finally")
	public static boolean eliminarReserva(int IdReserva){ // throws SQLException {
		//Por defecto asumimos que no se va a introducir bien
		boolean introducidoCorrectamente = false;

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try{
			//Conectamos con la BD
			con = login();

			//Creamos ahora el delete (Usando un PreparedStatement porque tenemos parametros)
			String delete = "DELETE FROM RESERVAS WERE IdReserva = ?";
			ps = con.prepareStatement(delete);
			ps.setInt(1, IdReserva);
			

			//Ejecutamos el insert y si se ejecuto correctamente nos devuelve 1
			if(ps.executeUpdate()==1){
				System.out.println("Introducido correctamente");
				introducidoCorrectamente=true;
			}

		}catch(SQLException e){
			//e.printStackTrace();
                        System.out.print(e.getMessage());
                        if(e.getErrorCode()==1)
                            System.out.println("No se pudo eliminar la reserva");
		}finally{
			//Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
			try{
				//El problema es que al cerrar las conexiones tambien se puede producir una excepcion
				if(rs!=null)
					rs.close();
				if(st!=null)
					st.close();
				if(ps!=null)
					ps.close();
				if(con!=null)
					con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}finally{
				return introducidoCorrectamente;
			}
		}
	}
	
	
	
	public static List<Habitacion> mostrarHabitacionesLibres(Date date, Date date2) throws SQLException {
		Connection con = login();
		List<Habitacion> habitaciones = new ArrayList<Habitacion>();
		/*
		String query = "select distinct H.IDHABITACION, HTH.TIPO "
				+ "from HABITACION h "
				+ "join HABITACION_TIPOHABITACION hth ON H.IDHABITACION = hth.IDHABITACION "
				+ "where h.IDHABITACION not in "
				+ 	"(select IdHabitacion from reserva_habitacion r WhERE ? <= r.FECHASALIDA and ? >= r.FECHAENTRADA)";
		*/
		
		//		String query = "SELECT idHabitacion, tipo FROM habitacion NATURAL JOIN hth NATURAL JOIN tipoHabitacion WHERE idHabitacion NOT IN (SELECT idHabitacion FROM reserva_habitacion WHERE NOT FECHAENTRADA >= to_date(?, 'DD/MM/YYYY') AND NOT FECHASALIDA <= to_date(?, 'DD/MM/YYYY')) AND ? >= tipoHabitacion.fechainicio and ? <= tipoHabitacion.fechaFin";

		
		String query = "SELECT DISTINCT idHabitacion, tipo FROM habitacion NATURAL JOIN hth NATURAL JOIN tipoHabitacion WHERE idHabitacion NOT IN (SELECT idHabitacion FROM reserva_habitacion WHERE NOT (FECHAENTRADA > ? OR FECHASALIDA < ?) ) AND ? >= tipoHabitacion.fechainicio and ? <= tipoHabitacion.fechaFin";
			
		PreparedStatement ps = con.prepareStatement(query);
		ps.setDate(1, new java.sql.Date(date2.getTime()));//finRango
		ps.setDate(2, new java.sql.Date(date.getTime()));//iniRango
		java.util.Date fechaAhora = new java.util.Date();
		ps.setDate(3, new java.sql.Date(fechaAhora.getTime()));
		ps.setDate(4, new java.sql.Date(fechaAhora.getTime()));
		ResultSet rs = ps.executeQuery();
		System.out.println();System.out.println();
		while(rs.next()){
			int IdHabitacion = rs.getInt(1);
			int tipo= rs.getInt(2);
			habitaciones.add(new Habitacion(IdHabitacion, tipo, null, null, null));
			System.out.printf("habitacion: %s   tipo %s\n", IdHabitacion , tipo);
		}
		rs.close();
		ps.close();
		con.close();
		return habitaciones;
	}
	
	public static void rellenarTabla() throws SQLException {
		Connection con = login();
		
		String query = " select IdReserva, IdHabitacion, IdCliente from reservas r ;";
		PreparedStatement ps = con.prepareStatement(query);
		ResultSet rs = ps.executeQuery(query);
		
		while(rs.next()){
			Object[] fila = new Object[3];
			fila[0] = rs.getInt(1);
			fila[1] = rs.getInt(2);
			fila[2] = rs.getInt(3);
			EliminarReserva.modelo.addRow(fila);
			
		}
		EliminarReserva.table.updateUI();
		
		rs.close();
		ps.close();
		con.close();
	}
	
	public static List<String> getTiposModalidad() {
		
		try {
			Connection con = login();
			
			String query = " select distinct TIPO from MODALIDAD";
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery(query);
			
			List<String> tipos = new ArrayList<>();
			while(rs.next()){
				tipos.add(rs.getString(1));			
			}
			
			rs.close();
			ps.close();
			con.close();
			
			return tipos;
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	//public static double calcularPrecioReserva(int idReserva) {
		
		// costeTotal = 0
		// Recuperar reservaHabitaciones
		// POR CADA reservaHabitacion
			// RECUPERAR tiposDeHabitacion en funcion de las fechas
			// POR CADA tipoDeHabitacion
				// CALCULAR dias de ese tipo
				// costeTotal += dias * tipoDeHabitacion.importeNoche
			// RECUPERAR modalidades en funcion de las fechas
			// POR CADA modalidad
				// CALCULAR dias de esa modalidad
				// costeTotal += dias * modalidad.costeDia
		// RECUPERAR extras de la reserva
		// POR CADA extra
			// costeTotal += extra.precio
		// return costeTotal
		
		
	//}
}
