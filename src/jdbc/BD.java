package jdbc;

import carlos.historias_1_2_3.Extra;
import carlos_historias_4_5_6_7.TareaLimpieza;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import historia1.negocio.TransferTarifa;
import historia2.negocio.TransferModalidad;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import util.Util;


public class BD {
	/*
	 * public static void main(String[] args) { try { int diaInicio = 1; int
	 * mesInicio = 11; int añoInicio = 2017;
	 * 
	 * int diaFin = 10; int mesFin = 11; int añoFin = 2017;
	 * 
	 * crearPromocion(new java.sql.Date(añoInicio,mesInicio,diaInicio), new
	 * java.sql.Date(añoFin,mesFin,diaFin), 30);
	 * 
	 * } catch (SQLException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } }
	 */

	/**
	 * Este método establece la conexion con la BD del servidor DESA de la
	 * Escuela de Informatica Devuelve un objeto de tipo Connection
	 */
	public static Connection login() throws SQLException {
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		String url = "jdbc:oracle:thin:@156.35.94.99:1521:DESA";
		String username = "UO267399";
		String password = "UO2673991";
		return DriverManager.getConnection(url, username, password);
	}

	// DESACOPLAR BD?

	@SuppressWarnings("finally")
	public static boolean crearTipoHabitacion(java.util.Date date, java.util.Date date2, double tarifa, int tipo) { // throws
																													// SQLException
																													// {
		// Por defecto asumimos que no se va a introducir bien
		boolean introducidoCorrectamente = false;

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;

		try {
			// Conectamos con la BD
			con = login();
			
			//Sacamos el maximo valor de Id que haya en la tabla (Oracle no tiene ID autoincremental)
			int maxId = 0;
			String query = "SELECT MAX(IDTH) from TIPOHABITACION";
			ps1 = con.prepareStatement(query);
			rs = ps1.executeQuery();
			while(rs.next())
				maxId=rs.getInt(1);
			//El id de la tupla que tenemos que añadir ahora sera maxId+1
			int siguienteId = maxId + 1;

			// Creamos ahora el INSERT (Usando un PreparedStatement porque
			// tenemos parametros)
			String insert = "INSERT INTO TIPOHABITACION VALUES(?,?,?,?,?)";
			ps = con.prepareStatement(insert);
			ps.setInt(1, tipo);
			ps.setDate(2, new java.sql.Date(date.getTime()));
			ps.setDate(3, new java.sql.Date(date2.getTime()));
			ps.setDouble(4, tarifa);
			ps.setInt(5, siguienteId);

			// Ejecutamos el insert y si se ejecuto correctamente nos devuelve 1
			if (ps.executeUpdate() == 1) {
				System.out.println("Introducido correctamente");
				introducidoCorrectamente = true;
			}

		} catch (SQLException e) {
			// e.printStackTrace();
			System.out.print(e.getMessage());
		} finally {
			// Cerramos todas las conexiones en el finally (por si se produjo
			// algun fallo antes cerrarlo todo bien)
			try {
				// El problema es que al cerrar las conexiones tambien se puede
				// producir una excepcion
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				return introducidoCorrectamente;
			}
		}
	}

	public static List<TransferTarifa> readAllTarifas() {

		List<TransferTarifa> l = new ArrayList<>();
		try {
			Connection con = login();
			Statement statement = con.createStatement();
			String query = "SELECT * FROM TIPOHABITACION";
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				TransferTarifa t = new TransferTarifa(rs.getDate(2), rs.getDate(3), rs.getInt(1), rs.getDouble(4));
				l.add(t);
			}
			rs.close();
			statement.close();
			con.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("bd excepcion " + e.getMessage());
		}

		return l;
	}

	public static List<TransferModalidad> readAllModalidades() {
		List<TransferModalidad> l = new ArrayList<>();
		try {
			Connection con = login();
			Statement statement = con.createStatement();
			String query = "SELECT * FROM MODALIDAD";
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				TransferModalidad t = new TransferModalidad(rs.getDate(2), rs.getDate(3), rs.getString(1), rs.getDouble(4), rs.getInt(5));
				l.add(t);
			}
			rs.close();
			statement.close();
			con.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("bd excepcion " + e.getMessage());
		}

		return l;
	}

	@SuppressWarnings("finally")
	public static boolean crearModalidad(Date fechaIni, Date fechaFin, double precio, String id, int i) {
		boolean introducidoCorrectamente = false;

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		try {
			// Conectamos con la BD
			con = login();

			// Creamos ahora el INSERT (Usando un PreparedStatement porque
			// tenemos parametros)
			String insert = "INSERT INTO MODALIDAD VALUES(?,?,?,?,?)";
			ps = con.prepareStatement(insert);
			ps.setString(1, id);
			ps.setDate(2, new java.sql.Date(fechaIni.getTime()));
			ps.setDate(3, new java.sql.Date(fechaFin.getTime()));
			ps.setDouble(4, precio);
			ps.setInt(5, i);

			// Ejecutamos el insert y si se ejecuto correctamente nos devuelve 1
			if (ps.executeUpdate() == 1) {
				System.out.println("Introducido correctamente");
				introducidoCorrectamente = true;
			}

		} catch (SQLException e) {
			// e.printStackTrace();
			System.out.print(e.getMessage());
			// El codigo de error 1 es el que nos va a indicar que se ha violado
			// una restriccion UNIQUE
			if (e.getErrorCode() == 1)// Es decir, que ya hay una promocion o un
										// descuento para las fechas
										// introducidas
				System.out.println("BD:error unicode");
		} finally {
			// Cerramos todas las conexiones en el finally (por si se produjo
			// algun fallo antes cerrarlo todo bien)
			try {
				// El problema es que al cerrar las conexiones tambien se puede
				// producir una excepcion
				if (rs != null)
					rs.close();
				if (st != null)
					st.close();
				if (ps != null)
					ps.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				return introducidoCorrectamente;
			}
		}
	}
        
        
        //~~~~~~~~~~~ Código Carlos ~~~~~~~~~~~~~~
        @SuppressWarnings("finally")
	public static boolean crearPromocion(java.sql.Date fechaInicio, java.sql.Date fechaFin, double porcentaje){ // throws SQLException {
		//Por defecto asumimos que no se va a introducir bien
		boolean introducidoCorrectamente = false;
		
		Connection con = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		PreparedStatement ps2 = null;
		
		try{
			//Conectamos con la BD
			con = login();
			
			//Sacamos el maximo valor de Id que haya en la tabla (Oracle no tiene ID autoincremental)
			int maxId = 0;
			String query = "SELECT MAX(IdExtra) from EXTRAS";
			ps1 = con.prepareStatement(query);
			rs = ps1.executeQuery();
			while(rs.next())
				maxId=rs.getInt(1);
			//El id de la tupla que tenemos que añadir ahora sera maxId+1
			int siguienteId = maxId + 1;
			
			//Creamos ahora el INSERT (Usando un PreparedStatement porque tenemos parametros)
			String insert = "INSERT INTO EXTRAS VALUES(?,?,?,?,'promocion')";
			ps2 = con.prepareStatement(insert);
			ps2.setInt(1, siguienteId);
			ps2.setDate(2, fechaInicio);
			ps2.setDate(3, fechaFin);
			ps2.setDouble(4, porcentaje);
			
			//Ejecutamos el insert y si se ejecuto correctamente nos devuelve 1
			if(ps2.executeUpdate()==1){
				System.out.println("Introducido correctamente");
				introducidoCorrectamente=true;
			}
			
		}catch(SQLException e){
			//e.printStackTrace();
                        System.out.print(e.getMessage());
                        //El codigo de error 1 es el que nos va a indicar que se ha violado una restriccion UNIQUE
                        if(e.getErrorCode()==1)//Es decir, que ya hay una promocion o un descuento para las fechas introducidas
                            System.out.println("Ya hay una promoción o un suplemento para esas fechas");
                        e.printStackTrace();
		}finally{
			//Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
			if(rs!=null) try{rs.close();} catch(SQLException e){}
        	if(ps1!=null) try{ps1.close();} catch(SQLException e){}
        	if(ps2!=null) try{ps2.close();} catch(SQLException e){}
        	if(con!=null) try{con.close();} catch(SQLException e){}
				
        	return introducidoCorrectamente;
		}
	}
	
	@SuppressWarnings("finally")
	public static boolean crearSuplemento(java.sql.Date fechaInicio, java.sql.Date fechaFin, double porcentaje){ // throws SQLException {
		//Por defecto asumimos que no se va a introducir bien
		boolean introducidoCorrectamente = false;
		
		Connection con = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		PreparedStatement ps2 = null;
		
		try{
			//Conectamos con la BD
			con = login();
			
			//Sacamos el maximo valor de Id que haya en la tabla (Oracle no tiene ID autoincremental)
			int maxId = 0;
			String query = "SELECT MAX(IdExtra) from EXTRAS";
			ps1 = con.prepareStatement(query);
			rs = ps1.executeQuery();
			while(rs.next())
				maxId=rs.getInt(1);
			//El id de la tupla que tenemos que añadir ahora sera maxId+1
			int siguienteId = maxId + 1;
			
			//Creamos ahora el INSERT (Usando un PreparedStatement porque tenemos parametros)
			String insert = "INSERT INTO EXTRAS VALUES(?,?,?,?,'suplemento')";
			ps2 = con.prepareStatement(insert);
			ps2.setInt(1, siguienteId);
			ps2.setDate(2, fechaInicio);
			ps2.setDate(3, fechaFin);
			ps2.setDouble(4, porcentaje);
			
			//Ejecutamos el insert y si se ejecuto correctamente nos devuelve 1
			if(ps2.executeUpdate()==1){
				System.out.println("Introducido correctamente");
				introducidoCorrectamente=true;
			}
			
		}catch(SQLException e){
			//e.printStackTrace();
                        System.out.print(e.getMessage());
                        //El codigo de error 1 es el que nos va a indicar que se ha violado una restriccion UNIQUE
                        if(e.getErrorCode()==1)//Es decir, que ya hay una promocion o un descuento para las fechas introducidas
                            System.out.println("Ya hay una promoción o un suplemento para esas fechas");
                        e.printStackTrace();
		}finally{
			//Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
			if(rs!=null) try{rs.close();} catch(SQLException e){}
        	if(ps1!=null) try{ps1.close();} catch(SQLException e){}
        	if(ps2!=null) try{ps2.close();} catch(SQLException e){}
        	if(con!=null) try{con.close();} catch(SQLException e){}
        	
        	return introducidoCorrectamente;

		}
	}
        
        public static boolean fechasPromocionesValidoParaTodosLosRangosDeLaBD(LocalDate fechaInicio, LocalDate fechaFinal) {
            //Tengo que sacar todas las promociones de la BD 
            //y ver si la promocion que quiero añadir ahora se superpone en fechas con alguna de las promociones (o promociones y suplementos???) que hay
            List<Extra> listaPromociones = damePromociones();
            boolean seSuperpone = false;
            System.out.println("\n\nVamos a comprobar si la fecha introducida se superpone con alguna otra promocion");
            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            System.out.println("Fecha introducida: "+dtf.format(fechaInicio)+" - "+dtf.format(fechaFinal));
            System.out.println("Mostramos todos los rangos de fechas de promociones de la BD (si hay):");
            for (Extra promocion : listaPromociones) {
                System.out.println("inicioRango="+dtf.format(promocion.fechaIni)+" finRango="+dtf.format(promocion.fechaFin));
                
                if(Util.fechasCaenDentroDelRango(fechaInicio, fechaFinal, promocion.fechaIni, promocion.fechaFin)){
                    seSuperpone = true; break;
                }
            }

            if(seSuperpone)
                System.out.println("¡¡Se superpone la fecha introducida con este rango de fechas!!");
            else
                System.out.println("NO se superpone con ningún rango de fechas");
            
            return !seSuperpone;
        }
        
        public static boolean fechasSuplementosValidoParaTodosLosRangosDeLaBD(LocalDate fechaInicio, LocalDate fechaFinal) {
            //Tengo que sacar todas las promociones de la BD 
            //y ver si la promocion que quiero añadir ahora se superpone en fechas con alguna de las promociones (o promociones y suplementos???) que hay
            List<Extra> listaSuplementos = dameSuplementos();
            boolean seSuperpone = false;
            System.out.println("\n\nVamos a comprobar si la fecha introducida se superpone con algun otro suplemento");
            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            System.out.println("Fecha introducida: "+dtf.format(fechaInicio)+" - "+dtf.format(fechaFinal));
            System.out.println("Mostramos todos los rangos de fechas de suplementos de la BD (si hay):");
            for (Extra suplemento : listaSuplementos) {
                System.out.println("inicioRango="+dtf.format(suplemento.fechaIni)+" finRango="+dtf.format(suplemento.fechaFin));
                
                if(Util.fechasCaenDentroDelRango(fechaInicio, fechaFinal, suplemento.fechaIni, suplemento.fechaFin)){
                    seSuperpone = true; break;
                }
            }

            if(seSuperpone)
                System.out.println("¡¡Se superpone la fecha introducida con este rango de fechas!!");
            else
                System.out.println("NO se superpone con ningún rango de fechas");
            
            return !seSuperpone;
        }
        
        @SuppressWarnings("finally")
		public static List<Extra> damePromociones(){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            List<Extra> listaPromociones = new LinkedList<>();
            System.out.println("\n\nTraza damePromociones()");

            try{
                con = login();
                String query = "SELECT idExtra, FechaInicio, FechaFin, porcentaje, tipo FROM EXTRAS WHERE tipo='promocion'";
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();
                
                int i = 1;
                while(rs.next()){
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    //Pasamos los java.sql.Date que nos devuelve la BD a los LocalDate con los que trabajamos
                    //Para ello primero creamos un util.Date pasandole el tiempo en ms del sql.Date
                    //Luego creamos el LocalDate usando el Date
                    //Tenemos que restar 1900 al año porque viene sumado 1900
                    
                    //FechaInicio
                    java.util.Date fechaIni = new java.util.Date(rs.getDate(2).getTime());
                    LocalDate fechaIniLD = fechaIni.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    fechaIniLD = fechaIniLD.minusYears(1900);
                    
                    //FechaFin
                    java.util.Date fechaFin = new java.util.Date(rs.getDate(3).getTime());
                    LocalDate fechaFinLD = fechaFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    fechaFinLD = fechaFinLD.minusYears(1900);
                   
                    int id = rs.getInt(1);
                    double porcentaje = rs.getDouble(4);
                    String tipo = rs.getString(5);
                    
                    //Traza
                    System.out.println("ID = " + id);
                    System.out.println("FechaIni = " + dtf.format(fechaIniLD));
                    System.out.println("FechaFin = " + dtf.format(fechaFinLD));
                    System.out.println("Porcentaje = " + porcentaje);
                    System.out.println("Tipo = " + tipo + "\n");
                    
                    //Creamos un extra
                    Extra promocion = new Extra(id,fechaIniLD,fechaFinLD,porcentaje,tipo);
                    
                    //Lo añadimos a la lista de promociones
                    listaPromociones.add(promocion);
                }
                
            }catch(SQLException e){
                    System.out.print(e.getMessage());
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps!=null) try{ps.close();} catch(SQLException e){}
            	if(con!=null) try{con.close();} catch(SQLException e){}
                
                return listaPromociones;
            }
        }
        
        @SuppressWarnings("finally")
        public static List<Extra> dameSuplementos(){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            List<Extra> listaSuplementos = new LinkedList<>();
            System.out.println("\n\nTraza dameSuplementos()");

            try{
                con = login();
                String query = "SELECT idExtra, FechaInicio, FechaFin, porcentaje, tipo FROM EXTRAS WHERE tipo='suplemento'";
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();
                
                int i = 1;
                while(rs.next()){
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    //Pasamos los java.sql.Date que nos devuelve la BD a los LocalDate con los que trabajamos
                    //Para ello primero creamos un util.Date pasandole el tiempo en ms del sql.Date
                    //Luego creamos el LocalDate usando el Date
                    //Tenemos que restar 1900 al año porque viene sumado 1900
                    
                    //FechaInicio
                    java.util.Date fechaIni = new java.util.Date(rs.getDate(2).getTime());
                    LocalDate fechaIniLD = fechaIni.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    fechaIniLD = fechaIniLD.minusYears(1900);
                    
                    //FechaFin
                    java.util.Date fechaFin = new java.util.Date(rs.getDate(3).getTime());
                    LocalDate fechaFinLD = fechaFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    fechaFinLD = fechaFinLD.minusYears(1900);
                   
                    int id = rs.getInt(1);
                    double porcentaje = rs.getDouble(4);
                    String tipo = rs.getString(5);
                    
                    //Traza
                    System.out.println("ID = " + id);
                    System.out.println("FechaIni = " + dtf.format(fechaIniLD));
                    System.out.println("FechaFin = " + dtf.format(fechaFinLD));
                    System.out.println("Porcentaje = " + porcentaje);
                    System.out.println("Tipo = " + tipo + "\n");
                    
                    //Creamos un extra
                    Extra promocion = new Extra(id,fechaIniLD,fechaFinLD,porcentaje,tipo);
                    
                    //Lo añadimos a la lista de suplementos
                    listaSuplementos.add(promocion);
                }
                
            }catch(SQLException e){
                    System.out.print(e.getMessage());
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps!=null) try{ps.close();} catch(SQLException e){}
            	if(con!=null) try{con.close();} catch(SQLException e){}
                
                return listaSuplementos;
            }
        }
        
        @SuppressWarnings("finally")
		public static List<Extra> damePromocionesYSuplementos(){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            List<Extra> listaExtras = new LinkedList<>();
            System.out.println("\n\nTraza damePromocionesYSuplementos()");

            try{
                con = login();
                String query = "SELECT idExtra, FechaInicio, FechaFin, porcentaje, tipo FROM EXTRAS";
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();
                
                int i = 1;
                while(rs.next()){
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    //Pasamos los java.sql.Date que nos devuelve la BD a los LocalDate con los que trabajamos
                    //Para ello primero creamos un util.Date pasandole el tiempo en ms del sql.Date
                    //Luego creamos el LocalDate usando el Date
                    //Tenemos que restar 1900 al año porque viene sumado 1900
                    
                    //FechaInicio
                    java.util.Date fechaIni = new java.util.Date(rs.getDate(2).getTime());
                    LocalDate fechaIniLD = fechaIni.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    fechaIniLD = fechaIniLD.minusYears(1900);
                    
                    //FechaFin
                    java.util.Date fechaFin = new java.util.Date(rs.getDate(3).getTime());
                    LocalDate fechaFinLD = fechaFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    fechaFinLD = fechaFinLD.minusYears(1900);
                   
                    int id = rs.getInt(1);
                    double porcentaje = rs.getDouble(4);
                    String tipo = rs.getString(5);
                    
                    //Traza
                    System.out.println("ID = " + id);
                    System.out.println("FechaIni = " + dtf.format(fechaIniLD));
                    System.out.println("FechaFin = " + dtf.format(fechaFinLD));
                    System.out.println("Porcentaje = " + porcentaje);
                    System.out.println("Tipo = " + tipo + "\n");
                    
                    //Creamos un extra
                    Extra extra = new Extra(id,fechaIniLD,fechaFinLD,porcentaje,tipo);
                    
                    //Lo añadimos a la lista de extras
                    listaExtras.add(extra);
                }
                
            }catch(SQLException e){
                    System.out.print(e.getMessage());
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps!=null) try{ps.close();} catch(SQLException e){}
            	if(con!=null) try{con.close();} catch(SQLException e){}
                
                return listaExtras;
            }
        }

        public static List<Extra> damePromocionesEntreFechas(LocalDate fechaInicioRango, LocalDate fechaFinRango){
            //Obtenemos todas las promociones
            List<Extra> listaPromociones = BD.damePromociones();
            //Eliminamos aquellas que no caen dentro del rango indicado por el usuario
            Iterator<Extra> itr = listaPromociones.iterator();
            while(itr.hasNext()) {
                Extra promocion = itr.next();
                if(!Util.fechasCaenDentroDelRango(promocion.fechaIni, promocion.fechaFin, fechaInicioRango, fechaFinRango))
                    itr.remove();
            }
            
            return listaPromociones;
        }
        
        public static List<Extra> dameSuplementosEntreFechas(LocalDate fechaInicioRango, LocalDate fechaFinRango){
            //Obtenemos todos los suplementos
            List<Extra> listaSuplementos = BD.dameSuplementos();
            //Eliminamos aquellas que no caen dentro del rango indicado por el usuario
            Iterator<Extra> itr = listaSuplementos.iterator();
            while(itr.hasNext()) {
                Extra suplemento = itr.next();
                if(!Util.fechasCaenDentroDelRango(suplemento.fechaIni, suplemento.fechaFin, fechaInicioRango, fechaFinRango))
                    itr.remove();
            }
            
            return listaSuplementos;
        }
        
        public static List<Extra> damePromocionesYSuplementosEntreFechas(LocalDate fechaInicioRango, LocalDate fechaFinRango){
            //Obtenemos todas las promociones
            List<Extra> listaExtras = BD.damePromocionesYSuplementos();
            //Eliminamos aquellas que no caen dentro del rango indicado por el usuario
            Iterator<Extra> itr = listaExtras.iterator();
            while(itr.hasNext()) {
                Extra extra = itr.next();
                if(!Util.fechasCaenDentroDelRango(extra.fechaIni, extra.fechaFin, fechaInicioRango, fechaFinRango))
                    itr.remove();
            }
            
            return listaExtras;
        }
        
        // ~~~~~ Carlos historias 4 5 6 y 7~~~~~~~
        @SuppressWarnings("finally")
        public static List<String> dameNumeroHabitaciones() {
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            List<String> listaNumeroHabitaciones = new ArrayList<>();
            System.out.println("\n\nTraza dameNumeroHabitaciones()");

            try{
                con = login();
                String query = "SELECT IdHabitacion FROM habitacion ORDER BY IdHabitacion";
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();
                
                int i = 1;
                while(rs.next()){
                    String IdHabitacion = rs.getString(1);
                    //Traza
                    System.out.println("IdHabitacion = " + IdHabitacion);
                    //Lo añadimos a la lista de promociones
                    listaNumeroHabitaciones.add(IdHabitacion);
                }
                
            }catch(SQLException e){
                    System.out.print(e.getMessage());
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps!=null) try{ps.close();} catch(SQLException e){}
            	if(con!=null) try{con.close();} catch(SQLException e){}
                
                return listaNumeroHabitaciones;
            }

        }
        
        public static java.sql.Timestamp getCurrentTimeStamp() {

        	java.util.Date today = new java.util.Date();
        	return new java.sql.Timestamp(today.getTime());

        }
        
        @SuppressWarnings("finally")
    	public static boolean crearTareaLimpieza(String numHabitacion, java.sql.Timestamp fechaTarea, java.sql.Timestamp momento_informada,String tipo, String observaciones, boolean prioritaria){
    		//Por defecto asumimos que no se va a introducir bien
    		boolean introducidoCorrectamente = false;
    		
    		Connection con = null;
    		PreparedStatement ps1 = null;
    		ResultSet rs = null;
    		PreparedStatement ps2 = null;
    		
    		try{
    			//Conectamos con la BD
    			con = login();
    			
    			//Sacamos el maximo valor de Id que haya en la tabla (Oracle no tiene ID autoincremental)
    			int maxId = 0;
    			String query = "SELECT MAX(IdTarea) from TAREA_LIMPIEZA";
    			ps1 = con.prepareStatement(query);
    			rs = ps1.executeQuery();
    			while(rs.next())
    				maxId=rs.getInt(1);
    			//El id de la tupla que tenemos que añadir ahora sera maxId+1
    			int siguienteId = maxId + 1;
    			
    			//Creamos ahora el INSERT (Usando un PreparedStatement porque tenemos parametros)
    			String insert = "INSERT INTO TAREA_LIMPIEZA VALUES (?,?,?,?,?,?,?,0)";//La tarea creada va a estar no realizada
    			ps2 = con.prepareStatement(insert);
    			ps2.setInt(1, siguienteId);//IdTarea
    			ps2.setString(2, numHabitacion);//IdHabitacion
    			ps2.setTimestamp(3, fechaTarea);//FechaTarea
    			ps2.setString(4, tipo);//Tipo
    			ps2.setString(5, observaciones);//Observaciones
    			ps2.setInt(6, (prioritaria ? 1 : 0));//Prioritaria
    			ps2.setTimestamp(7, momento_informada);//momento_informada
    			
    			//Ejecutamos el insert y si se ejecuto correctamente nos devuelve 1
    			if(ps2.executeUpdate()==1){
    				System.out.println("Tarea introducida correctamente");
    				introducidoCorrectamente=true;
    			}
    			
    		}catch(SQLException e){
                System.out.print(e.getMessage());
                e.printStackTrace();
    		}finally{
    			//Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
    			//Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps1!=null) try{ps1.close();} catch(SQLException e){}
            	if(ps2!=null) try{ps2.close();} catch(SQLException e){}
            	if(con!=null) try{con.close();} catch(SQLException e){}
    			
            	return introducidoCorrectamente;
    			
    		}
    	}
        
        @SuppressWarnings("finally")
	public static List<TareaLimpieza> dameTareasLimpiezaDelDiaActualOrdenadasPorFechaYPrioridad(java.sql.Date fechaActual){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            List<TareaLimpieza> listaTareas = new LinkedList<>();
            System.out.println("\n\nTraza dameTareasLimpiezaDelDiaActualOrdenadasPorFechaYPrioridad()");

            try{
                con = login();
                String query = "SELECT idTarea, idHabitacion, fechaTarea, tipo, observaciones, prioritaria, momento_informada FROM TAREA_LIMPIEZA "
                        + "WHERE realizada=0 AND TRUNC(fechaTarea) = TO_DATE( ? , 'yyyy-mm-dd' )"
                        + "ORDER BY prioritaria DESC, fechaTarea";
                ps = con.prepareStatement(query);
                fechaActual.setYear(fechaActual.getYear()-1900);
                String s = fechaActual.toString();
                System.out.println("fechaActual: "+fechaActual);
                System.out.println("s: "+s);
                ps.setString(1, s);
                rs = ps.executeQuery();
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                
                int i = 1;
                while(rs.next()){

                    int idTarea = rs.getInt(1);
                    String idHabitacion = rs.getString(2);
                    Timestamp fechaTarea=rs.getTimestamp(3);
                    String tipo = rs.getString(4);
                    String observaciones = rs.getString(5);
                    int prioritaria = rs.getInt(6);
                    Timestamp momento_informada = rs.getTimestamp(7);
                    
                    //Traza
                    System.out.println("IdTarea = " + idTarea);
                    System.out.println("IdHabitacion = " + idHabitacion);
                    System.out.println("FechaTarea = " + sdf.format(fechaTarea));
                    System.out.println("Tipo = " + tipo);
                    System.out.println("Observaciones = " + observaciones);
                    System.out.println("Prioritaria = " + prioritaria);
                    System.out.println("Momento informada = " + sdf.format(momento_informada) + "\n");
                   
                    
                    //Creamos una tarea de limpieza
                    TareaLimpieza tareaLimpieza = new TareaLimpieza(idTarea, idHabitacion, fechaTarea, tipo, observaciones, prioritaria, momento_informada);
                    
                    //Lo añadimos a la lista de promociones
                    listaTareas.add(tareaLimpieza);
                }
                
                fechaActual.setYear(fechaActual.getYear()+1900);
                
            }catch(SQLException e){
                System.out.println(e.getSQLState());
                e.printStackTrace();
                System.out.print(e.getMessage());
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps!=null) try{ps.close();} catch(SQLException e){}
            	if(con!=null) try{con.close();} catch(SQLException e){}
                
                return listaTareas;
            }
        }
        
        @SuppressWarnings("finally")
        public static void marcarTareasDeLimpiezaComoRealizadasPor(String usuario, List<Integer> listaIdTareas){
            Connection con = null;
            PreparedStatement ps = null;
            PreparedStatement ps2 = null;
            ResultSet rs = null;
            
            System.out.println("\n\nTraza marcarTareasDeLimpiezaComoRealizadasDeLaHabitacionPor()");
            System.out.println("BD.marcarTareasDeLimpiezaComoRealizadasDeLaHabitacionPor   nombreUsuario: " + usuario);

            try{
                con = login();
                
                //Para cada tarea de la lista de tareas, hay que marcarla como realizada
                String update = "UPDATE TAREA_LIMPIEZA SET realizada = 1 WHERE idTarea = ?";
                ps = con.prepareStatement(update);
                int numTareasModificadas = 0;
                for (Integer idTarea : listaIdTareas) {
                    System.out.println("IdTarea: "+idTarea);
                    ps.setInt(1, idTarea);
                    numTareasModificadas += ps.executeUpdate();
                }
                
                System.out.println("Numero de tareas marcadas como realizadas: "+numTareasModificadas);
                System.out.println("Vamos a insertar en REALIZAR_TAREA_LIMPIEZA");
                
                //Para cada una de las tareas de limpieza que se van a marcar como realizadas, hay que indicar que usuario las realiza
                String insert = "INSERT INTO REALIZAR_TAREA_LIMPIEZA VALUES(?,?)";
                ps2 = con.prepareStatement(insert);
                ps2.setString(2, usuario);
                for (Integer idTarea : listaIdTareas) {
                    System.out.println("IdTarea: "+idTarea);
                    ps2.setInt(1, idTarea);
                    ps2.executeUpdate();
                }
                
                
            }catch(SQLException e){
                    System.out.print(e.getMessage());
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
                if(ps!=null) try{ps.close();} catch(SQLException e){}
                if(ps2!=null) try{ps2.close();} catch(SQLException e){}
                if(con!=null) try{con.close();} catch(SQLException e){}                
            }
        }
        
        @SuppressWarnings("finally")
	public static List<String> dameNombresUsuarioTrabajadoresDeLimpieza(){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            List<String> listaNombresUsuario = new LinkedList<>();
            System.out.println("\n\nTraza dameNombresUsuarioTrabajadoresDeLimpieza()");

            try{
                con = login();
                String query = " SELECT nombre_usuario FROM trabajador WHERE tipo='limpieza' ";
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();
                
                System.out.println("Nombres de usuario del personal de limpieza guardados en la BD:");
                while(rs.next()){
                    
                    String nombreUsuario = rs.getString(1);
                    //Traza
                    System.out.println("nombreUsuario: " + nombreUsuario);
                    
                    //Lo añadimos a la lista de nombres de usuario
                    listaNombresUsuario.add(nombreUsuario);
                }
                
            }catch(SQLException e){
                    System.out.print(e.getMessage());
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps!=null) try{ps.close();} catch(SQLException e){}
            	if(con!=null) try{con.close();} catch(SQLException e){}
                
                return listaNombresUsuario;
            }
        }
        
        @SuppressWarnings("finally")
	public static List<String> dameTiposAlimentos(){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            List<String> listaTipos = new LinkedList<>();
            System.out.println("\n\nTraza dameTiposAlimentos()");

            try{
                con = login();
                String query = " SELECT DISTINCT tipo FROM ALIMENTOS ";
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();
                
                System.out.println("Tipos de alimentos guardados en la BD:");
                while(rs.next()){
                    
                    String tipo = rs.getString(1);
                    //Traza
                    System.out.println("tipo: " + tipo);
                    
                    //Lo añadimos a la lista de tipos de alimentos
                    listaTipos.add(tipo);
                }
                
            }catch(SQLException e){
                    System.out.print(e.getMessage());
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps!=null) try{ps.close();} catch(SQLException e){}
            	if(con!=null) try{con.close();} catch(SQLException e){}
                
                return listaTipos;
            }
        }
        
        public static boolean isAlimentoConLaMismaDescripcionYMismaFechaEnLaBD(String nombre, String tipo, String lugar, LocalDate fecha) {
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            //Tengo que hacer una consulta para ver si existe ya en la BD un alimento
            //con la misma descripcion y misma fecha. Si el rs es vacio es que no existe
            boolean existe = false;
            System.out.println("\n\nVamos a comprobar si existe en la BD un alimento con la misma descripcion en la misma fecha para el lugar: "+lugar);
            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            System.out.println("Datos del alimento que quiere introducir el usuario:");
            System.out.println("NOMBRE: "+nombre+" TIPO: "+tipo+" LUGAR: "+ lugar+" FECHA: "+dtf.format(fecha));
            
            try{
                con = login();
                String query = " SELECT * FROM alimentos WHERE nombre = ? AND tipo = ? AND lugar = ? AND fechaAlta = ? ";
                ps = con.prepareStatement(query);
                ps.setString(1, nombre);
                ps.setString(2, tipo);
                ps.setString(3, lugar);
                int dia = fecha.getDayOfMonth();
                int mes = fecha.getMonthValue();
                int anio = fecha.getYear();
                java.sql.Date fechaSQLDate = new java.sql.Date(anio,mes-1,dia);
                ps.setDate(4, fechaSQLDate);
                rs = ps.executeQuery();
                
                //La consulta o devuelve una tupla o no devuelve ninguna
                while(rs.next()){
                    //Si entramos dentro del bucle es que tenia una tupla por lo que existia ya ese alimento
                    existe=true;
                    //Traza
                    java.util.Date fechaBD = new java.util.Date(rs.getDate("fechaAlta").getTime());
                    LocalDate fechaBD_LD = fechaBD.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    fechaBD_LD = fechaBD_LD.minusYears(1900);
                    System.out.println("YA EXISTE ese alimento en la BD para la fecha actual en el lugar: "+lugar);
                    System.out.println("Datos del alimento que ya existe en la BD (han de coincidir con los introducidos por el usuario):");
                    System.out.println("ID: "+rs.getInt("IdAlimento")+" NOMBRE: "+rs.getString("nombre")+" TIPO: "+rs.getString("tipo")+" LUGAR: "+ rs.getString("lugar")+" FECHA: "+dtf.format(fechaBD_LD)+" PRECIO: "+rs.getDouble("precio"));
                }
                
                if(!existe)
                    System.out.println("NO EXISTE");
                            
            }catch(SQLException e){
                    System.out.print(e.getMessage());
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps!=null) try{ps.close();} catch(SQLException e){}
            	if(con!=null) try{con.close();} catch(SQLException e){}
                    
                return existe;
            }
        }
        
        @SuppressWarnings("finally")
	public static boolean crearAlimento(String nombre, String tipo, java.sql.Date fechaAlta, Map<String,Double> lugares_precios) {																				// {
		//Por defecto asumimos que se va a introducir bien
    		boolean introducidoCorrectamente = true;
    		
    		Connection con = null;
    		PreparedStatement ps1 = null;
    		ResultSet rs = null;
    		PreparedStatement ps2 = null;
    		
    		try{
    			//Conectamos con la BD
    			con = login();
    			
    			//Sacamos el maximo valor de Id que haya en la tabla (Oracle no tiene ID autoincremental)
    			int maxId = 0;
    			String query = "SELECT MAX(IdAlimento) from ALIMENTOS";
    			ps1 = con.prepareStatement(query);
    			rs = ps1.executeQuery();
    			while(rs.next())
    				maxId=rs.getInt(1);
    			//El id de la tupla que tenemos que añadir ahora sera maxId+1
    			int siguienteId = maxId + 1;
    			
    			//Creamos ahora el INSERT
    			String insert = "INSERT INTO ALIMENTOS VALUES (?,?,?,?,?,?)";//La tarea creada va a estar no realizada
    			ps2 = con.prepareStatement(insert);
                        
                        //Para cada uno de los lugares del diccionario ejecutamos un insert
                        for(Map.Entry<String,Double> par : lugares_precios.entrySet()){
                            ps2.setInt(1, siguienteId);//IdTarea
                            ps2.setString(2, nombre);//Nombre
                            ps2.setString(3, tipo);//Tipo
                            ps2.setString(4, par.getKey());//Lugar
                            ps2.setDate(5, fechaAlta);//Fecha alta
                            ps2.setDouble(6, par.getValue());//Precio
                            
                            siguienteId++;
                            
                            //Ejecutamos el insert y si se ejecuto correctamente nos devuelve 1
                            if(ps2.executeUpdate()==1)
                                    System.out.println("Alimento introducido correctamente en: "+par.getKey());
                            else
                                introducidoCorrectamente=false;//Si alguno no se introdujo bien hubo un fallo
                        }
    			
    		}catch(SQLException e){
                    System.out.print(e.getMessage());
                    e.printStackTrace();
    		}finally{
                    //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
                    //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
                    if(rs!=null) try{rs.close();} catch(SQLException e){}
                    if(ps1!=null) try{ps1.close();} catch(SQLException e){}
                    if(ps2!=null) try{ps2.close();} catch(SQLException e){}
                    if(con!=null) try{con.close();} catch(SQLException e){}

                    return introducidoCorrectamente;
    			
    		}
    	}


}
