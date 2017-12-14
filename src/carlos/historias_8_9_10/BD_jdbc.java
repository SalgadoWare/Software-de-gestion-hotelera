package carlos.historias_8_9_10;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import util.Util;

public class BD_jdbc {


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

        private static int crearModalidadReservaHabitacion(String tipoModalidad, 
                java.sql.Date fechaEntradaSQLDate, java.sql.Date fechaSalidaSQLDate, Connection c) throws SQLException{
            
            PreparedStatement ps1 = null;
            PreparedStatement ps2 = null;
            ResultSet rs = null;
            Integer siguienteId = null;
            
            //Sacamos el maximo valor de Id que haya en la tabla (Oracle no tiene ID autoincremental)
            int maxId = 0;
            String query = "SELECT MAX(idModalidadReservaHabitacion) from MODALIDAD_RESERVA_HABITACION";
            ps1 = c.prepareStatement(query);
            rs = ps1.executeQuery();
            while(rs.next())
                maxId=rs.getInt(1);
            //El id de la tupla que tenemos que añadir ahora sera maxId+1
            siguienteId = maxId + 1;

            //Creamos ahora el INSERT (Usando un PreparedStatement porque tenemos parametros)
            String insert = "INSERT INTO MODALIDAD_RESERVA_HABITACION VALUES(?, ?, ?, ?)";
            ps2 = c.prepareStatement(insert);
            ps2.setInt(1, siguienteId);//idModalidadReservaHabitacion
            ps2.setString(2, tipoModalidad);//tipoModalidad
            ps2.setDate(3, fechaEntradaSQLDate);//fechaInicio
            ps2.setDate(4, fechaSalidaSQLDate);//fechaFin
            
            if(ps2.executeUpdate()==1)
                System.out.println("ModalidadReservaHabitacion creada correctamente");

            if (rs != null) {rs.close();}
            if (ps2 != null) {ps2.close();}
            if (ps1 != null) {ps1.close();}

            return siguienteId;
        }
        
	private static boolean crearReservaHabitacion(int idReserva, ReservaHabitacion rh, Connection c) throws SQLException{
		
		PreparedStatement ps = null;
                //TipoModalidad
                String tipoModalidad = rh.getModalidad();

                //FechaEntrada
                int diaEntrada = rh.getFechaEntrada().getDayOfMonth();
                int mesEntrada = rh.getFechaEntrada().getMonthValue();
                int anioEntrada = rh.getFechaEntrada().getYear();
                java.sql.Date fechaEntradaSQLDate = new java.sql.Date(anioEntrada,mesEntrada-1,diaEntrada);

                //FechaSalida
                int diaSalida = rh.getFechaSalida().getDayOfMonth();
                int mesSalida = rh.getFechaSalida().getMonthValue();
                int anioSalida = rh.getFechaSalida().getYear();
                java.sql.Date fechaSalidaSQLDate = new java.sql.Date(anioSalida,mesSalida-1,diaSalida);

                //Creamos antes una MODALIDAD_RESERVA_HABITACION con los datos: modalidad,fechaI y fechaF
                int idModalidadReservaHabitacion = crearModalidadReservaHabitacion(tipoModalidad, fechaEntradaSQLDate, fechaSalidaSQLDate,c);

                //Ahora creamos la reserva_habitacion
                ps = c.prepareStatement("INSERT INTO reserva_habitacion VALUES (?, ?, ?, ?, ?, '0', NULL)");//por defecto la reserva_hab no esta anulada y por tanto tapoco tiene fechaAnulada
                ps.setString(1, rh.getIdHabitacion());//idHabitacion
                ps.setInt(2, idReserva);//idReserva
                ps.setInt(3, idModalidadReservaHabitacion);//idModalidadReservaHabitacion
                ps.setDate(4, fechaEntradaSQLDate);//fechaEntrada
                ps.setDate(5, fechaSalidaSQLDate);//fechaSalida
                
                int resultadoUpdate = ps.executeUpdate();
                if(resultadoUpdate==1)
                    System.out.println("ReservaHabitacion creada correctamente");
                
                if (ps != null) {ps.close();}
                
                return resultadoUpdate==1;
                
	}
	
	@SuppressWarnings("finally")
	public static boolean crearReserva(int idCliente, List<ReservaHabitacion> reservaHabitaciones){ // throws SQLException {
            //Por defecto asumimos que no se va a introducir bien
            boolean introducidoCorrectamente = false;

            Connection con = null;
            Statement st = null;
            ResultSet rs = null;
            PreparedStatement ps = null;

            try{
                //Conectamos con la BD
                con = login();
                con.setAutoCommit(false);

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
                String insert = "INSERT INTO RESERVA VALUES(?,?,'sin_confirmar','0',NULL)";//al crear la reserva estará sin confirmar y no estará anulada totalmente ni tendra fecha anulada
                ps = con.prepareStatement(insert);
                ps.setInt(1, siguienteId);//idReserva
                ps.setInt(2, idCliente);//idCliente


                //Ejecutamos el insert y si se ejecuto correctamente nos devuelve 1
                if(ps.executeUpdate()==1){

                    introducidoCorrectamente=true;

                    for (ReservaHabitacion rh: reservaHabitaciones) {
                        boolean resHabIntroducidaCorrectamente = crearReservaHabitacion(siguienteId, rh, con);
                        introducidoCorrectamente = introducidoCorrectamente && resHabIntroducidaCorrectamente;
                    }
                    if(introducidoCorrectamente)
                        System.out.println("Reserva creada correctamente");
                }

                con.commit();

            }catch(SQLException e){
                con.rollback();
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
	
	
	public static void anularReservaHabitacion(String idHabitacion, int IdReserva, int idModalidadReservaHabitacion){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            
            System.out.println("\n\nTraza anularReservaHabitacion()");

            try{
                con = login();
                
                //Marcamos la reservaHabitacion como anulada
                String update = "UPDATE RESERVA_HABITACION SET anulada = 1 "
                        + "WHERE idHabitacion = ? and idReserva = ? and idModalidadReservaHabitacion = ?";
                ps = con.prepareStatement(update);
                ps.setString(1, idHabitacion);
                ps.setInt(2, IdReserva);
                ps.setInt(3, idModalidadReservaHabitacion);
                
                if(ps.executeUpdate()==1)
                    System.out.println("ReservaHabitacion anulada correctamente");
                
            }catch(SQLException e){
                System.out.print(e.getMessage());
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
                if(ps!=null) try{ps.close();} catch(SQLException e){}
                if(con!=null) try{con.close();} catch(SQLException e){}                
            }         
            
	}
	
	public static List<HabitacionesReservaAnularReserva> mostrarHabitacionesLibres2(LocalDate fechaInicio, LocalDate fechaFinal) throws SQLException {
            System.out.println("\n\nTraza mostrarHabitacionesLibres2()");
            System.out.println("\n\nVamos a comprobar cuales son las habitaciones que ya estan reservadas (no anuladas)\nen unas fechas que se superponen con las que nos da el usuario");
            
            List<HabitacionesReservaAnularReserva> listaHabitacionesReservadasNoAnuladas = dameHabitacionesReservadasNoAnuladas();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            System.out.println("Fecha introducida: "+dtf.format(fechaInicio)+" - "+dtf.format(fechaFinal));
            System.out.println("Vamos a recorrer todas las habs reservadas (no anuladas) de la BD:");
            System.out.println("Si alguna habitacin se superpone en fechas con las que introdujo el usuario se mostrara un mensaje indicandolo:");
            
            Set<String> idsHabitacionABorrarDeLaLista = new HashSet<>();
            
            //En este bucle lo que vamos a hacer es marcar aquellas habitaciones que no deberian salir, y guardar sus ids en un set
            for (HabitacionesReservaAnularReserva hrar : listaHabitacionesReservadasNoAnuladas) {
                
                System.out.println("Habitacion actual del for: idHabitacion: " + hrar.getIdHabitacion() + 
                        " tipo="+hrar.getTipo()+" fechaEntrada="+dtf.format(hrar.getFechaEntrada())+" fechaSalida="+dtf.format(hrar.getFechaSalida()));
                
                if(Util.fechasCaenDentroDelRango(fechaInicio, fechaFinal, hrar.getFechaEntrada(), hrar.getFechaSalida())){
                    System.out.println("LA HABITACION ACTUAL SE SUPERPONE!!!");
                    System.out.println("Vamos a guardar su id en un set");
                    //Guardamos el id de esa habitacion en un set
                    idsHabitacionABorrarDeLaLista.add(hrar.getIdHabitacion());
                }
            }
            
            //Traza
            System.out.println("TRAZA: ids del set:");
            for (String string : idsHabitacionABorrarDeLaLista) {
                System.out.println(string);
            }
            
            //Ahora tenemos que sacar de la bd todas las habs (guardamos en un set su id y su tipo)
            //Le restamos a este nuevo set el set viejo (que contienen los ids de las habitaciones que NO pueden salir en la tabla)
            List<HabitacionesReservaAnularReserva> listaHabitaciones = dameHabitaciones();
            
            //Recorremos las habitaciones y comparamos el id de la habitacion actual con los ids del set
            Iterator<HabitacionesReservaAnularReserva> itr = listaHabitacionesReservadasNoAnuladas.iterator();
            while(itr.hasNext()){
                HabitacionesReservaAnularReserva hrar = itr.next();
                for (String idHabitacionABorrar : idsHabitacionABorrarDeLaLista) {
                    //Si alguno de los ids del set coincide con el id de la habitacion actual, tenemos que borrar esa habitacion de la lista
                    //y pasar a la sgte ejecucion del while
                    System.out.println("idHabitacionDeLaLista: "+hrar.getIdHabitacion() + " idHabDelSet: "+idHabitacionABorrar);
                    if(hrar.getIdHabitacion().equals(idHabitacionABorrar)){
                        System.out.println("LA HABITACION ACTUAL SE VA A BORRAR, YA QUE SU ID COINCIDE CON UNO DE LOS DEL SET");
                        itr.remove();
                    }       
                }
            }
            
            return listaHabitacionesReservadasNoAnuladas;
        }
        
        public static List<HabitacionesReservaAnularReserva> mostrarHabitacionesLibres3(LocalDate fechaInicio, LocalDate fechaFinal) throws SQLException {
            System.out.println("\n\nTraza mostrarHabitacionesLibres2()");
            System.out.println("\n\nVamos a comprobar cuales son las habitaciones que ya estan reservadas (no anuladas)\nen unas fechas que se superponen con las que nos da el usuario");
            
            List<HabitacionesReservaAnularReserva> listaHabitacionesReservadasNoAnuladas = dameHabitacionesReservadasNoAnuladas();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            System.out.println("Fecha introducida: "+dtf.format(fechaInicio)+" - "+dtf.format(fechaFinal));
            System.out.println("Vamos a recorrer todas las habs reservadas (no anuladas) de la BD:");
            System.out.println("Si alguna habitacin se superpone en fechas con las que introdujo el usuario se mostrara un mensaje indicandolo:");
            
            Set<String> idsHabitacionABorrarDeLaLista = new HashSet<>();
            
            //En este bucle lo que vamos a hacer es marcar aquellas habitaciones que no deberian salir, y guardar sus ids en un set
            for (HabitacionesReservaAnularReserva hrar : listaHabitacionesReservadasNoAnuladas) {
                
                System.out.println("Habitacion actual del for: idHabitacion: " + hrar.getIdHabitacion() + 
                        " tipo="+hrar.getTipo()+" fechaEntrada="+dtf.format(hrar.getFechaEntrada())+" fechaSalida="+dtf.format(hrar.getFechaSalida()));
                
                if(Util.fechasCaenDentroDelRango(fechaInicio, fechaFinal, hrar.getFechaEntrada(), hrar.getFechaSalida())){
                    System.out.println("LA HABITACION ACTUAL SE SUPERPONE!!!");
                    System.out.println("Vamos a guardar su id en un set");
                    //Guardamos el id de esa habitacion en un set
                    idsHabitacionABorrarDeLaLista.add(hrar.getIdHabitacion());
                }
            }
            
            //Traza
            System.out.println("TRAZA: ids del set:");
            for (String string : idsHabitacionABorrarDeLaLista) {
                System.out.println(string);
            }
            
            //Ahora tenemos que sacar de la bd todas las habs (guardamos en un set su id y su tipo)
            //Le restamos a este nuevo set el set viejo (que contienen los ids de las habitaciones que NO pueden salir en la tabla)
            List<HabitacionesReservaAnularReserva> listaHabitaciones = dameHabitaciones();
            
            //Recorremos las habitaciones y comparamos el id de la habitacion actual con los ids del set
            Iterator<HabitacionesReservaAnularReserva> itr = listaHabitaciones.iterator();
            while(itr.hasNext()){
                HabitacionesReservaAnularReserva hrar = itr.next();
                for (String idHabitacionABorrar : idsHabitacionABorrarDeLaLista) {
                    //Si alguno de los ids del set coincide con el id de la habitacion actual, tenemos que borrar esa habitacion de la lista
                    //y pasar a la sgte ejecucion del while
                    System.out.println("idHabitacionDeLaLista: "+hrar.getIdHabitacion() + " idHabDelSet: "+idHabitacionABorrar);
                    if(hrar.getIdHabitacion().equals(idHabitacionABorrar)){
                        System.out.println("LA HABITACION ACTUAL SE VA A BORRAR, YA QUE SU ID COINCIDE CON UNO DE LOS DEL SET");
                        itr.remove();
                    }       
                }
            }
            
            return listaHabitaciones;
        }
        
        public static List<HabitacionesReservaAnularReserva> dameHabitaciones(){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            List<HabitacionesReservaAnularReserva> listaHabitaciones = new LinkedList<>();
            System.out.println("\n\nTraza dameHabitaciones()");

            try{
                con = login();
                String query = "SELECT idHabitacion, tipo "
                        + "FROM habitacion INNER JOIN hth USING(idHabitacion) INNER JOIN tipoHabitacion USING(idth) ";
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();
                
                int i = 1;
                while(rs.next()){
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    //Pasamos los java.sql.Date que nos devuelve la BD a los LocalDate con los que trabajamos
                    //Para ello primero creamos un util.Date pasandole el tiempo en ms del sql.Date
                    //Luego creamos el LocalDate usando el Date
                    //Tenemos que restar 1900 al año porque viene sumado 1900
                    
                    //Recogemos los datos
                    String idHabitacion = rs.getString(1);
                    int tipo = rs.getInt(2);
                    
                    //Creamos un HabitacionesReservaAnularReserva
                    HabitacionesReservaAnularReserva hrar = new HabitacionesReservaAnularReserva(idHabitacion, -1, -1, tipo, null, null);
                    
                    //Lo añadimos a la lista de suplementos
                    listaHabitaciones.add(hrar);
                }
                
            }catch(SQLException e){
                    System.out.print(e.getMessage());
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps!=null) try{ps.close();} catch(SQLException e){}
            	if(con!=null) try{con.close();} catch(SQLException e){}
                
                return listaHabitaciones;
            }
        }
        
        @SuppressWarnings("finally")
        public static List<HabitacionesReservaAnularReserva> dameHabitacionesReservadasNoAnuladas(){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            List<HabitacionesReservaAnularReserva> listaHabitacionesReservadasNoAnuladas = new LinkedList<>();
            System.out.println("\n\nTraza dameHabitacionesReservadasNoAnuladas()");

            try{
                con = login();
                String query = "SELECT idHabitacion, tipo, fechaEntrada, fechaSalida "
                        + "FROM reserva_habitacion INNER JOIN habitacion USING(idHabitacion) INNER JOIN hth USING(idHabitacion) INNER JOIN tipoHabitacion USING(idth) " +
                        "WHERE anulada='0'";
                ps = con.prepareStatement(query);
                rs = ps.executeQuery();
                
                int i = 1;
                while(rs.next()){
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    //Pasamos los java.sql.Date que nos devuelve la BD a los LocalDate con los que trabajamos
                    //Para ello primero creamos un util.Date pasandole el tiempo en ms del sql.Date
                    //Luego creamos el LocalDate usando el Date
                    //Tenemos que restar 1900 al año porque viene sumado 1900
                    
                    //Recogemos los datos
                    String idHabitacion = rs.getString(1);
                    int tipo = rs.getInt(2);
                    java.sql.Date fechaEntradaSQL = rs.getDate(3);
                    java.sql.Date fechaSalidaSQL = rs.getDate(4);
                    
                    //Trazamos los datos con las fechas tal cual salen de la bd
                    System.out.println("TRAZA: Datos nada mas salir de la BD:");
                    System.out.println("idHabitacion = " + idHabitacion);
                    System.out.println("tipo = " + tipo);
                    System.out.println("FechaEntrada (valor SQL recien salido) = " + dtf.format(fechaEntradaSQL.toLocalDate()));
                    System.out.println("FechaSalida (valor SQL recien salido) = " + dtf.format(fechaSalidaSQL.toLocalDate()));
                    
                    
                    //FechaEntrada
                    java.util.Date fechaEntradaDate = new java.util.Date(fechaEntradaSQL.getTime());
                    LocalDate fechaEntradaLD = fechaEntradaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    fechaEntradaLD = fechaEntradaLD.minusYears(1900);
                    
                    //FechaFin
                    java.util.Date fechaSalidaDate = new java.util.Date(fechaSalidaSQL.getTime());
                    LocalDate fechaSalidaLD = fechaSalidaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    fechaSalidaLD = fechaSalidaLD.minusYears(1900);
                    
                    System.out.println("FechaEntrada (valor restandole 1900) = " + dtf.format(fechaEntradaLD));
                    System.out.println("FechaSalida (valor restandole 1900) = " + dtf.format(fechaSalidaLD));
                   
                    
                    //Creamos un HabitacionesReservaAnularReserva
                    HabitacionesReservaAnularReserva hrar = new HabitacionesReservaAnularReserva(idHabitacion, -1, -1, tipo, fechaEntradaLD, fechaSalidaLD);
                    
                    //Lo añadimos a la lista de suplementos
                    listaHabitacionesReservadasNoAnuladas.add(hrar);
                }
                
            }catch(SQLException e){
                    System.out.print(e.getMessage());
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps!=null) try{ps.close();} catch(SQLException e){}
            	if(con!=null) try{con.close();} catch(SQLException e){}
                
                return listaHabitacionesReservadasNoAnuladas;
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

            //Salen todas las habitaciones - habitaciones que estan reservadas (y la reserva no esta anulada) y las fechas de la reserva se solapan con el rango (date,date2)
            String query = "SELECT DISTINCT idHabitacion, tipo FROM habitacion INNER JOIN hth USING(idHabitacion) INNER JOIN tipoHabitacion USING(idth) WHERE idHabitacion NOT IN (SELECT idHabitacion FROM reserva_habitacion WHERE anulada=0 AND NOT (FECHAENTRADA > to_date(?, 'DD/MM/YYYY') OR FECHASALIDA < to_date(?, 'DD/MM/YYYY')) ) AND to_date(?, 'DD/MM/YYYY') >= tipoHabitacion.fechainicio and to_date(?, 'DD/MM/YYYY') <= tipoHabitacion.fechaFin";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setDate(1, new java.sql.Date(date2.getTime()));//finRango
            ps.setDate(2, new java.sql.Date(date.getTime()));//iniRango
            java.util.Date fechaAhora = new java.util.Date();
            ps.setDate(3, new java.sql.Date(fechaAhora.getTime()));
            ps.setDate(4, new java.sql.Date(fechaAhora.getTime()));
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                    String IdHabitacion = rs.getString(1);
                    int tipo= rs.getInt(2);
                    habitaciones.add(new Habitacion(IdHabitacion, tipo, null, null, null));
                    System.out.printf("habitacion: %s   tipo %s\n", IdHabitacion , tipo);
            }
            rs.close();
            ps.close();
            con.close();
            return habitaciones;
	}
        
        
	@SuppressWarnings("finally")
	public static List<Integer> dameIdsReservaDelClienteConDni(String dni){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            List<Integer> listaIdsReserva = new LinkedList<>();
            System.out.println("\n\nTraza dameIdsReservaDelClienteConDni()");

            try{
                con = login();
                String query = " SELECT idReserva FROM CLIENTE NATURAL JOIN RESERVA WHERE DNI = ? ORDER BY idReserva";
                ps = con.prepareStatement(query);
                ps.setString(1, dni);
                rs = ps.executeQuery();
                
                System.out.println("IdReservas del cliente con DNI "+dni+":");
                while(rs.next()){
                    
                    int idReserva = rs.getInt(1);
                    //Traza
                    System.out.println("idReserva: " + idReserva);
                    
                    //Lo añadimos a la lista de idsReserva
                    listaIdsReserva.add(idReserva);
                }
                
            }catch(SQLException e){
                    System.out.print(e.getMessage());
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps!=null) try{ps.close();} catch(SQLException e){}
            	if(con!=null) try{con.close();} catch(SQLException e){}
                
                return listaIdsReserva;
            }
        }
        
        @SuppressWarnings("finally")
	public static List<Integer> dameIdsReservaDelClienteConPasaporte(String pasaporte){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            List<Integer> listaIdsReserva = new LinkedList<>();
            System.out.println("\n\nTraza dameIdReservasDelClienteConPasaporte()");

            try{
                con = login();
                String query = " SELECT idReserva FROM CLIENTE NATURAL JOIN RESERVA WHERE pasaporte = ? ORDER BY idReserva ";
                ps = con.prepareStatement(query);
                ps.setString(1, pasaporte);
                rs = ps.executeQuery();
                
                System.out.println("IdReservas del cliente con pasaporte "+pasaporte+":");
                while(rs.next()){
                    
                    int idReserva = rs.getInt(1);
                    //Traza
                    System.out.println("idReserva: " + idReserva);
                    
                    //Lo añadimos a la lista de idsReserva
                    listaIdsReserva.add(idReserva);
                }
                
            }catch(SQLException e){
                    System.out.print(e.getMessage());
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps!=null) try{ps.close();} catch(SQLException e){}
            	if(con!=null) try{con.close();} catch(SQLException e){}
                
                return listaIdsReserva;
            }
        }
        
        @SuppressWarnings("finally")
	public static Map<String,List<HabitacionesReservaAnularReserva>> dameHabitacionesReservaAnuladasYNoAnuladasCuyoIdReservaEs(int idReserva){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            Map<String,List<HabitacionesReservaAnularReserva>> diccionario = new HashMap<>();
            System.out.println("\n\nTraza dameHabitacionesReservaAnuladasYNoAnuladasCuyoIdReservaEs()");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            try{
                con = login();
                /*String query = "SELECT DISTINCT idhabitacion, reserva_habitacion.idReserva, reserva_habitacion.idmodalidadreservahabitacion, " +
                "tipohabitacion.tipo, reserva_habitacion.fechaEntrada, reserva_habitacion.fechaSalida " +
                "FROM reserva_habitacion NATURAL JOIN habitacion NATURAL JOIN hth NATURAL JOIN tipohabitacion " +
                "WHERE reserva_habitacion.idReserva = ? " +
                "and to_date(?, 'DD/MM/YYYY') >= TIPOHABITACION.fechaInicio and to_date(?, 'DD/MM/YYYY') <= TIPOHABITACION.fechaFin " +
                "and anulada= ? ";*/
                
                String query = "SELECT DISTINCT idhabitacion, reserva_habitacion.idReserva, reserva_habitacion.idmodalidadreservahabitacion, " +
                "tipohabitacion.tipo, reserva_habitacion.fechaEntrada, reserva_habitacion.fechaSalida " +
                "FROM reserva_habitacion NATURAL JOIN habitacion NATURAL JOIN hth NATURAL JOIN tipohabitacion " +
                "WHERE reserva_habitacion.idReserva = ? " +
                "and anulada= ? ";
                
                ps = con.prepareStatement(query);
                ps.setInt(1, idReserva);//idReserva
                
                java.util.Date fechaActual = new Date();
                java.sql.Date fechaActualSQLDate = new java.sql.Date(fechaActual.getTime());
                
                //ps.setDate(2, fechaActualSQLDate);//fechaActual
                //ps.setDate(3, fechaActualSQLDate);//fechaActual
                
                //~~~~~~~~Primero sacamos los datos de las habitaciones no anuladas~~~~~~~~
                List<HabitacionesReservaAnularReserva> listaHabitacionesNoAnuladas = new LinkedList<>();
                
                ps.setInt(2, 0);//no anuladas
                rs = ps.executeQuery();
                
                System.out.println("Datos de las habs reservadas (no anuladas) de la reserva cuyo id es:"+idReserva+":");
                while(rs.next()){
                    String idHabitacion = rs.getString(1);//idHabitacion
                    int idReservaBD = rs.getInt(2);//idReserva
                    int idModalidadReservaHabitacion = rs.getInt(3);//idModalidadReservaHabitacion
                    
                    int tipoHabitacion = rs.getInt(4);
                    
                    java.util.Date fechaEntrada = new java.util.Date(rs.getDate(5).getTime());
                    LocalDate fechaEntrada_LD = fechaEntrada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    fechaEntrada_LD = fechaEntrada_LD.minusYears(1900);
                    
                    java.util.Date fechaSalida = new java.util.Date(rs.getDate(6).getTime());
                    LocalDate fechaSalida_LD = fechaSalida.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    fechaSalida_LD = fechaSalida_LD.minusYears(1900);
                    
                    //Traza
                    System.out.println("idHabitacion: " + idHabitacion + " tipo: " + tipoHabitacion + 
                            " fechaEntrada: " + dtf.format(fechaEntrada_LD) + " fechaSalidaa: " + dtf.format(fechaSalida_LD));
                    
                    //Añadimos la resHabNoAnulada a la lista
                    HabitacionesReservaAnularReserva hsarNA = new HabitacionesReservaAnularReserva(idHabitacion, 
                            idReservaBD, idModalidadReservaHabitacion, tipoHabitacion, fechaEntrada_LD, fechaSalida_LD);
                    listaHabitacionesNoAnuladas.add(hsarNA);
                }
                
                //Añadimos los datos al diccionario
                diccionario.put("no_anuladas", listaHabitacionesNoAnuladas);
                
                //~~~~~~~~Sacamos los datos de las habitaciones anuladas~~~~~~~~
                List<HabitacionesReservaAnularReserva> listaHabitacionesAnuladas = new LinkedList<>();
                
                ps.setInt(2, 1);//anuladas
                rs = ps.executeQuery();
                
                System.out.println("Datos de las habs reservadas (anuladas) de la reserva cuyo id es:"+idReserva+":");
                while(rs.next()){
                    String idHabitacion = rs.getString(1);//idHabitacion
                    int idReservaBD = rs.getInt(2);//idReserva
                    int idModalidadReservaHabitacion = rs.getInt(3);//idModalidadReservaHabitacion
                    
                    int tipoHabitacion = rs.getInt(4);
                    
                    java.util.Date fechaEntrada = new java.util.Date(rs.getDate(5).getTime());
                    LocalDate fechaEntrada_LD = fechaEntrada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    fechaEntrada_LD = fechaEntrada_LD.minusYears(1900);
                    
                    java.util.Date fechaSalida = new java.util.Date(rs.getDate(6).getTime());
                    LocalDate fechaSalida_LD = fechaSalida.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    fechaSalida_LD = fechaSalida_LD.minusYears(1900);
                    
                    //Traza
                    System.out.println("idHabitacion: " + idHabitacion + " tipo: " + tipoHabitacion + 
                            " fechaEntrada: " + dtf.format(fechaEntrada_LD) + " fechaSalidaa: " + dtf.format(fechaSalida_LD));
                    
                    //Añadimos la resHabAnulada a la lista
                    HabitacionesReservaAnularReserva hsarA = new HabitacionesReservaAnularReserva(idHabitacion, 
                            idReservaBD, idModalidadReservaHabitacion, tipoHabitacion, fechaEntrada_LD, fechaSalida_LD);
                    listaHabitacionesAnuladas.add(hsarA);
                }
                
                //Añadimos los datos al diccionario
                diccionario.put("anuladas", listaHabitacionesAnuladas);              
                
            }catch(SQLException e){
                    System.out.print(e.getMessage());
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps!=null) try{ps.close();} catch(SQLException e){}
            	if(con!=null) try{con.close();} catch(SQLException e){}
                
                return diccionario;
            }
        }
        
        public static void crearFacturaAnulada(int idReserva){
            Connection con = null;
            PreparedStatement ps1 = null;
            PreparedStatement ps2 = null;
            ResultSet rs = null;
            Integer siguienteId = null;
            
            System.out.println("crearFacturaAnulada()");
            
            try{
                con = login();
                //Primero: calculamos el importe de las habitaciones reservadas
                double importe = getImporteHabitacionesReservadasEnFuncionDelTipo(idReserva,con);
                
                //Segundo: escribimos la factura
                String contenido = generaContenidoFactura(idReserva,importe,con);
                
                //Sacamos el maximo valor de Id que haya en la tabla (Oracle no tiene ID autoincremental)
                int maxId = 0;
                String query = "SELECT MAX(idFacturaAnulada) from FACTURA_ANULADA";
                ps1 = con.prepareStatement(query);
                rs = ps1.executeQuery();
                while(rs.next())
                    maxId=rs.getInt(1);
                //El id de la tupla que tenemos que añadir ahora sera maxId+1
                siguienteId = maxId + 1;

                //Creamos ahora el INSERT
                String insert = "INSERT INTO FACTURA_ANULADA VALUES(?, ?, ?, ?)";
                ps2 = con.prepareStatement(insert);
                ps2.setInt(1, siguienteId);//idFactura
                ps2.setDouble(2, importe);//importe
                ps2.setString(3, contenido);//contenido
                ps2.setInt(4, idReserva);//idReserva

                if(ps2.executeUpdate()==1)
                    System.out.println("Factura creada correctamente");
                
            }catch(SQLException e){
                    System.out.print(e.getMessage());
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps1!=null) try{ps1.close();} catch(SQLException e){}
            	if(ps2!=null) try{ps2.close();} catch(SQLException e){}
                if(con!=null) try{con.close();} catch(SQLException e){}
            }

        }
        
        private static String generaContenidoFactura(int idReserva, double importe, Connection con) throws SQLException{
            System.out.println("\ngeneraContenidoFactura()");

            //Primero: obtenemos el cliente al que pertenece la reserva
            Cliente cliente = getCliente(idReserva,con);
            
            //Segundo: obtenemos los datos de las habitaciones de la reserva
            List<ReservaHabitacionNEW> habs = getHabitacionesReserva(idReserva,con);
            
            //Tercero: calculamos el importe de la factura (50% del importe)
            double importeFactura = importe * 0.50;
            
            //Cuarto: escribimos la factura
            LocalDate fechaNacimiento;
            if(cliente.getFechaNacimiento()==null) fechaNacimiento=null;
            else fechaNacimiento = cliente.getFechaNacimiento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            
            
            String contenido = "Factura del hotel generada debido a la anulación total de la reserva por parte del cliente\n24 horas antes de la entrada al hotel.\n";
            contenido += "\nId de la reserva anulada: " + idReserva;
            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            contenido += "\nFecha de la anulación total de la reserva: " + dtf.format(LocalDate.now()) + "\n\n";
            
            contenido += "Datos del cliente:";
            contenido += "\n\t-Nombre: " + cliente.getNombre();
            contenido += "\n\t-Apellidos: " + cliente.getApellidos();
            contenido += "\n\t-DNI: " + ( cliente.getDni()!=null ? cliente.getDni() : "" );
            contenido += "\n\t-Pasaporte: " + ( cliente.getPasaporte()!=null ? cliente.getPasaporte() : "" );
            contenido += "\n\t-Tarjeta: " + cliente.getTarjeta();
            contenido += "\n\t-E-mail: " + ( cliente.getEmail()!=null ? cliente.getEmail() : "" );
            contenido += "\n\t-Fecha de nacimiento: " + ( fechaNacimiento!=null ? dtf.format(fechaNacimiento) : "" );
            contenido += "\n\t-Móvil: " + ( cliente.getMovil()!=null ? cliente.getMovil() : "" );
            contenido += "\n\t-Teléfono fijo: " + ( cliente.getTelefonoFijo()!=null ? cliente.getTelefonoFijo() : "" );
            contenido += "\n\t-Dirección: " + ( cliente.getDireccion()!=null ? cliente.getDireccion() : "" );
            contenido += "\n\t-Código postal: " + ( cliente.getCp()!=null ? cliente.getCp() : "" );
            
            contenido += "\n\nInformación de las habitaciones reservadas:";
            for (ReservaHabitacionNEW hab : habs)
                contenido += "\n\t- " + hab.getIdHabitacion() + " - " + dtf.format(hab.getFechaEntrada()) + " - " + dtf.format(hab.getFechaSalida()) ;
            
            contenido += "\n\nImporte de las habitaciones reservadas (teniendo en cuenta sólo el tipo de la habitación) ............. " + importe + "€";
            contenido += "\nImporte de la factura (50% del importe anterior) ........................................................................ " + importeFactura + "€";
            
            contenido += "\n\nEsta factura se va a cargar a la tarjeta " + cliente.getTarjeta() + " asociada al cliente.";
            
            contenido += "\n\nFecha de la generación de la factura: " + dtf.format(LocalDate.now()) + "\n\n";
            
            return contenido;
        }
        
        private static Cliente getCliente(int idReserva, Connection con) throws SQLException{
            System.out.println("\ngetCliente()");
            
            Cliente cliente = null;
            
            String query = "SELECT idCliente,nombre,apellidos,dni,pasaporte,tarjeta,email,fecha_Nacimiento,movil,telefono_Fijo,direccion,poblacion,cp " +
            "FROM CLIENTE NATURAL JOIN RESERVA " +
            "WHERE idReserva = ? ";

            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, idReserva);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int idCliente = rs.getInt(1);
                String nombre = rs.getString(2);
                String apellidos = rs.getString(3);
                String dni = rs.getString(4);
                String pasaporte = rs.getString(5);
                String tarjeta = rs.getString(6);
                String email = rs.getString(7);
                
                java.sql.Date fechaNacimientoSQL = rs.getDate(8);
                java.util.Date fechaNacimiento = new java.util.Date(fechaNacimientoSQL.getTime());
                
                String movil = rs.getString(9);
                String telefonoFijo = rs.getString(10);
                String direccion = rs.getString(11);
                String poblacion = rs.getString(12);
                String cp = rs.getString(13);
                
                cliente = new Cliente(idCliente, nombre, apellidos, dni, pasaporte, tarjeta, email, fechaNacimiento, movil, telefonoFijo, direccion, poblacion, cp);
            }
            
            if(rs!=null) try{rs.close();} catch(SQLException e){}
            if(ps!=null) try{ps.close();} catch(SQLException e){}
            
            return cliente;
        }
                  
        private static double getImporteHabitacionesReservadasEnFuncionDelTipo(int idReserva, Connection con) throws SQLException{
            //Para cada una de las habitaciones de la reserva, calculamos su importe en funcion del tipo de habitacion
            //El tipo de una habitacion va a ser aquel cuyas fechas inicio y fecha fin incluyan al dia actual
            System.out.println("\ngetImporteHabitacionesReservadasEnFuncionDelTipo()");
            //Sacamos las habitaciones de la reserva
            List<ParIdReservaIdHabitacion> paresIdReservaIdHabitacion  = dameHabitacionesReserva(idReserva);
            
            //Para cada una de las habitaciones, calculamos su importe, y lo sumamos al total
            double importeTotal = 0;
            for (ParIdReservaIdHabitacion parIdReservaIdHabitacion : paresIdReservaIdHabitacion) {
                double importeHabitacion = getImporteHabitacion(parIdReservaIdHabitacion.getIdReserva(), parIdReservaIdHabitacion.getIdHabitacion(),con);
                //Traza
                System.out.println("Importe total de la habitacion " +  parIdReservaIdHabitacion.getIdHabitacion() + ": " +importeHabitacion);
                importeTotal+=importeHabitacion;
            }
            //Traza
            System.out.println("Importe total de la reserva cuyo id es " + idReserva+ ": " +importeTotal);
            
            return importeTotal;
        }
        
        private static double getImporteHabitacion(int idReserva, String idHabitacion, Connection con) throws SQLException{
            //Primero sacamos el importe/dia que tiene esa habitacion
            //Despues, calculamos cuantos dias va a estar esa habitacion reservada
            //Multiplicamos los dias por el importe/dia
            //todo lo hacemos a nivel de BD
            Double importeHabitacion = null;
            
            try{
                System.out.println("\ngetImporteHabitacion()");

                /*String query = "SELECT tarifa*(RESERVA_HABITACION.FECHASALIDA-RESERVA_HABITACION.FECHAENTRADA+1) " +
                "FROM RESERVA_HABITACION NATURAL JOIN HABITACION NATURAL JOIN HTH NATURAL JOIN TIPOHABITACION " +
                "WHERE idReserva = ? and idHabitacion = ? " +
                "and to_date(?, 'DD/MM/YYYY') >= TIPOHABITACION.fechaInicio and to_date(?, 'DD/MM/YYYY') <= TIPOHABITACION.fechaFin";*/
                
                String query = "SELECT tarifa*(RESERVA_HABITACION.FECHASALIDA-RESERVA_HABITACION.FECHAENTRADA+1) " +
                "FROM RESERVA_HABITACION NATURAL JOIN HABITACION NATURAL JOIN HTH NATURAL JOIN TIPOHABITACION " +
                "WHERE idReserva = ? and idHabitacion = ? ";
                

                PreparedStatement ps = con.prepareStatement(query);
                ps.setInt(1, idReserva);
                ps.setString(2, idHabitacion);
                //java.util.Date fechaActual = new Date();
                //java.sql.Date fechaActualSQLDate = new java.sql.Date(fechaActual.getTime());

                //ps.setDate(3, fechaActualSQLDate);//fechaActual
                //ps.setDate(4, fechaActualSQLDate);//fechaActual

                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    System.out.print("Importe de la habitacion " + idHabitacion + ": ");
                    importeHabitacion = rs.getDouble(1);
                    System.out.println(importeHabitacion + "€");
                }
                
                if(rs!=null) try{rs.close();} catch(SQLException e){}
                if(ps!=null) try{ps.close();} catch(SQLException e){}

                
                
            }catch(Exception e){
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
            
            return importeHabitacion;
            
        }
        
        public static LocalDate getDiaEntradaCliente(int idReserva){
            //El dia de entrada del cliente es el minimo dia
            //de los dias de entrada de todas sus habs reservadas
            
            //Con el idReserva, sacamos la minima fechaEntrada de las habs reservadas en dicha reserva
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            LocalDate fechaEntradaClienteLD= null;
            System.out.println("\n\nTraza getDiaEntradaCliente()");

            try{
                con = login();
                String query = " SELECT MIN(fechaEntrada) FROM RESERVA_HABITACION WHERE idReserva = ? ";
                ps = con.prepareStatement(query);
                ps.setInt(1, idReserva);
                rs = ps.executeQuery();
                
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                System.out.println("Fecha de entrada del cliente al hotel en la IdReserva="+idReserva+" :");
                while(rs.next()){
                    
                    //java.sql.Date fechaEntradaClienteSQL = rs.getDate(1);
                    //fechaEntradaCliente = new java.util.Date(fechaEntradaClienteSQL.getTime());
                    
                    java.util.Date fechaEntradaClienteDate = new java.util.Date(rs.getDate(1).getTime());
                    fechaEntradaClienteLD = fechaEntradaClienteDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    fechaEntradaClienteLD = fechaEntradaClienteLD.minusYears(1900);
                    
                    
                    //Traza
                    System.out.println("BD_jdbc --> fechaEntrada: " + dtf.format(fechaEntradaClienteLD));
                    
                }
                
            }catch(SQLException e){
                    System.out.print(e.getMessage());
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps!=null) try{ps.close();} catch(SQLException e){}
            	if(con!=null) try{con.close();} catch(SQLException e){}
                
                return fechaEntradaClienteLD;
            }
            
        }
        
        @SuppressWarnings("finally")
	public static List<ParIdReservaIdHabitacion> dameHabitacionesReserva(int idReserva){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            List<ParIdReservaIdHabitacion> lista = new LinkedList<>();
            System.out.println("\n\nTraza dameHabitacionesReserva()");
            
            try{
                con = login();
                String query = "SELECT DISTINCT idHabitacion FROM RESERVA_HABITACION WHERE idReserva = ? ";
                ps = con.prepareStatement(query);
                ps.setInt(1, idReserva);//idReserva

                rs = ps.executeQuery();
                
                System.out.println("Datos de las habs de la reserva cuyo id es:"+idReserva+":");
                while(rs.next()){
                    String idHabitacion = rs.getString(1);//idHabitacion
                    
                    //Traza
                    System.out.println("idReserva: " +idReserva + " idHabitacion: " + idHabitacion);
                    
                    lista.add(new ParIdReservaIdHabitacion(idReserva, idHabitacion));
                }
                           
                
            }catch(SQLException e){
                System.out.print(e.getMessage());
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps!=null) try{ps.close();} catch(SQLException e){}
            	if(con!=null) try{con.close();} catch(SQLException e){}
                
                return lista;
            }
        }
        
        @SuppressWarnings("finally")
	public static List<ReservaHabitacionNEW> getHabitacionesReserva(int idReserva, Connection con) throws SQLException {
            List<ReservaHabitacionNEW> lista = new LinkedList<>();
            System.out.println("\n\nTraza getHabitacionesReserva()");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            con = login();
            String query = "SELECT idHabitacion, fechaEntrada, fechaSalida " +
            "FROM RESERVA_HABITACION WHERE idReserva = ? ";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, idReserva);//idReserva

            ResultSet rs = ps.executeQuery();

            System.out.println("Datos de las habs de la reserva cuyo id es:"+idReserva+":");
            while(rs.next()){
                String idHabitacion = rs.getString(1);//idHabitacion

                java.util.Date fechaEntrada = new java.util.Date(rs.getDate(2).getTime());
                LocalDate fechaEntrada_LD = fechaEntrada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                fechaEntrada_LD = fechaEntrada_LD.minusYears(1900);

                java.util.Date fechaSalida = new java.util.Date(rs.getDate(3).getTime());
                LocalDate fechaSalida_LD = fechaSalida.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                fechaSalida_LD = fechaSalida_LD.minusYears(1900);

                //Traza
                System.out.println("idReserva: " +idReserva + " idHabitacion: " + idHabitacion+ " fechaEntrada: " + dtf.format(fechaEntrada_LD)+ " fechaSalida: " + dtf.format(fechaSalida_LD));

                lista.add(new ReservaHabitacionNEW(idHabitacion, fechaEntrada_LD, fechaSalida_LD));
            }

            if(rs!=null) try{rs.close();} catch(SQLException e){}
            if(ps!=null) try{ps.close();} catch(SQLException e){}

            return lista;
        }
        
        public static boolean existeFactura(int idReserva){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            //Tengo que hacer una consulta para ver si existe la factura
            //Si el rs es vacio es que no existe
            boolean existe = false;
            System.out.println("\n\nVamos a comprobar si existe en la BD una factura asociada al idReserva: "+idReserva);
            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            try{
                con = login();
                String query = " SELECT * " +
                "FROM RESERVA NATURAL JOIN FACTURA_ANULADA " +
                "WHERE idReserva = ? ";
                
                ps = con.prepareStatement(query);
                ps.setInt(1, idReserva);
                rs = ps.executeQuery();
                
                //La consulta o devuelve una tupla o no devuelve ninguna
                while(rs.next())
                    existe=true;//Si entramos dentro del bucle es que tenia una tupla por lo que existia ya ese alimento
                
                if(existe)
                    System.out.println("EXISTE la factura");
                else
                    System.out.println("NO EXISTE la factura");
                            
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

        public static String getContenidoFactura(int idReserva){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            String contenido = null;
            
            try{
                con = login();
                
                String query = "SELECT contenido " +
                "FROM RESERVA NATURAL JOIN FACTURA_ANULADA " +
                "WHERE idReserva = ? ";
                ps = con.prepareStatement(query);
                ps.setInt(1, idReserva);
                
                rs = ps.executeQuery();
                while(rs.next())
                    contenido=rs.getString(1);
                
            }catch(SQLException e){
                    System.out.print(e.getMessage());
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps!=null) try{ps.close();} catch(SQLException e){}
                if(con!=null) try{con.close();} catch(SQLException e){}
                
                return contenido;
            }

        }
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ GERT-47-CARLOS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        public static boolean isHabitacionConElMismoIdEnLaBD(String idHabitacion){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            //Tengo que hacer una consulta para ver si existe ya en la BD una habitacion con ese id
            //Si el rs es vacio es que no existe
            boolean existe = false;
            System.out.println("\n\nTRAZA: isHabitacionConElMismoIdEnLaBD()");            
            System.out.println("Vamos a comprobar si existe en la BD una habitacoin con el mismo id en la BD");
            System.out.println("(El idHabitacion que nos pasan es: " + idHabitacion + " )");
            
            try{
                con = login();
                String query = " SELECT * FROM habitacion WHERE idHabitacion = ? ";
                ps = con.prepareStatement(query);
                ps.setString(1, idHabitacion);

                rs = ps.executeQuery();
                
                //La consulta o devuelve una tupla o no devuelve ninguna
                while(rs.next()){
                    //Si entramos dentro del bucle es que tenia una tupla por lo que existia ya ese alimento
                    existe=true;
                }
                
                if(existe)
                    System.out.println("YA EXISTE esa habitación en la BD");
                else
                    System.out.println("NO EXISTE esa habitación en la BD");
                            
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
        
        public static boolean crearHabitacion(String idHabitacion, int tipo, int planta, String descripcion, List<String> pathsRelativosImagenes){
            
            Connection con = null;
            PreparedStatement ps1 = null;
            PreparedStatement ps2 = null;
            ResultSet rs = null;
            
            boolean creada=false;
            
            try{
                con = login();
                con.setAutoCommit(false);
                
                //Creamos la tupla en la tabla habitacion
                String insert_HABITACION = "INSERT INTO HABITACION (idHabitacion,estadoHabitacion,planta,descripcion) VALUES(?, 'libre' , ?, ?)";
                ps1 = con.prepareStatement(insert_HABITACION);
                ps1.setString(1, idHabitacion);//idHabitacion
                ps1.setInt(2, planta);//planta
                ps1.setString(3, descripcion);//descripcion

                if(ps1.executeUpdate()==1)
                    System.out.println("Tupla insertada en la tabla habitacion correctamente");
                
                //Obtenemos el idTH que debe tener asignada la habitacion en la tabla HTH, en funcion del tipo y de la fecha actual
                int idTH = dameIdTHEnFuncionDelTipo(tipo,con);

                //Creamos la tupla en la tabla hth
                String insert_HTH = "INSERT INTO HTH (idHabitacion,idTH) VALUES(?, ?)";
                ps2 = con.prepareStatement(insert_HTH);
                ps2.setString(1, idHabitacion);//idHabitacion
                ps2.setInt(2, idTH);//idTH
                
                if(ps2.executeUpdate()==1)
                    System.out.println("Tupla insertada en la tabla hth correctamente");
                
                //Creamos las tuplas en la tabla IMAGEN
                creaImagenes(idHabitacion, pathsRelativosImagenes, con);
                
                //Hacemos commit de la transaccion
                con.commit();
                creada=true;
                
            }catch(SQLException e){
                con.rollback();//Si hubo algun problema tenemos que hacer rollback y deshacer todos los cambios
                System.out.print(e.getMessage());
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps1!=null) try{ps1.close();} catch(SQLException e){}
              	if(ps2!=null) try{ps2.close();} catch(SQLException e){}
            	if(con!=null) try{con.close();} catch(SQLException e){}
                    
                return creada;
            }
        }
	
        
        private static void creaImagenes(String idHabitacion, List<String> pathsRelativosImagenes, Connection con) throws SQLException{
            PreparedStatement ps1 = null;
            PreparedStatement ps2 = null;
            ResultSet rs = null;
            
            //Para cada imagen
            for (String pathRelativoImagen : pathsRelativosImagenes) {
                
                //Sacamos el maximo valor de Id que haya en la tabla (Oracle no tiene ID autoincremental)
                int maxId = 0;
                String query = "SELECT MAX(idImagen) from IMAGEN";
                ps1 = con.prepareStatement(query);
                rs = ps1.executeQuery();
                while(rs.next())
                    maxId=rs.getInt(1);
                //El id de la tupla que tenemos que añadir ahora sera maxId+1
                int siguienteId = maxId + 1;

                
                //Una vez tenemos el idImagen, insertamos en la tabla la nueva tupla
                String insert_IMAGEN = "INSERT INTO IMAGEN (idImagen,idHabitacion,pathArchivo) VALUES(?,?,?)";
                ps2 = con.prepareStatement(insert_IMAGEN);
                ps2.setInt(1, siguienteId);//idImagen
                ps2.setString(2, idHabitacion);//idHabitacion
                ps2.setString(3, pathRelativoImagen);//pathArchivo

                if(ps2.executeUpdate()==1)
                    System.out.println("Tupla insertada en la tabla imagen correctamente");
            }
                

            if(rs!=null) try{rs.close();} catch(SQLException e){}
            if(ps1!=null) try{ps1.close();} catch(SQLException e){}
            if(ps2!=null) try{ps2.close();} catch(SQLException e){}
                
        }
        
        private static int dameIdTHEnFuncionDelTipo(int tipo, Connection con) throws SQLException{
            PreparedStatement ps;
            ResultSet rs;
            Integer idTH = null;
                    
            /*String query = " SELECT idTH FROM TIPOHABITACION WHERE tipo = ?  AND "
                    + " to_date(?, 'DD/MM/YYYY') >= tipoHabitacion.fechainicio and to_date(?, 'DD/MM/YYYY') <= tipoHabitacion.fechaFin";*/
            
            String query = " SELECT idTH FROM TIPOHABITACION WHERE tipo = ? ";
            
            ps = con.prepareStatement(query);
            ps.setInt(1, tipo);
            //java.util.Date fechaAhora = new java.util.Date();
            //ps.setDate(2, new java.sql.Date(fechaAhora.getTime()));
            //ps.setDate(3, new java.sql.Date(fechaAhora.getTime()));

            rs = ps.executeQuery();

            //La consulta o devuelve una tupla o no devuelve ninguna
            while(rs.next()){
                //Si entramos dentro del bucle es que tenia una tupla por lo que existia ya ese alimento
                idTH=rs.getInt(1);
                System.out.println("El tipo de la habitacion es: "+tipo+" y su idTH va a ser: "+idTH);
            }
                

            if(rs!=null) try{rs.close();} catch(SQLException e){}
            if(ps!=null) try{ps.close();} catch(SQLException e){}
            
            return idTH;
        }
        
        
        
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ RECEP-51-CARLOS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        
        public static Map<String,Object> dameDatosClienteConDni(String dni){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            Map<String,Object> datosCliente = new HashMap<>();
            System.out.println("\n\nTraza dameDatosClienteConDni()");
            
            //Por defecto suponemos que no existe
            datosCliente.put("existe", false);

            try{
                con = login();
                con.setAutoCommit(false);
                String query = " SELECT * FROM CLIENTE WHERE DNI = ?";
                ps = con.prepareStatement(query);
                ps.setString(1, dni);
                rs = ps.executeQuery();
                
                while(rs.next()){
                    //Si entramos aqui es que existe
                    datosCliente.put("existe", true);
                    
                    int idCliente = rs.getInt("idCliente");
                    String nombre = rs.getString("nombre");
                    String apellidos = rs.getString("apellidos");
                    String pasaporte = rs.getString("pasaporte");
                    String tipoTarjeta = rs.getString("tipo_tarjeta");
                    String numeroTarjeta = rs.getString("tarjeta");
                    String direccion = rs.getString("direccion");
                    String poblacion = rs.getString("poblacion");
                    String cp = rs.getString("cp");
                    String telefonoFijo = rs.getString("telefono_fijo");
                    String telefonoMovil = rs.getString("movil");
                    String email = rs.getString("email");
                    
                    LocalDate fechaNacimiento_LD;
                    if(rs.getDate("fecha_nacimiento")==null)
                        fechaNacimiento_LD=null;
                    else{
                        java.util.Date fechaNacimiento = new java.util.Date(rs.getDate("fecha_nacimiento").getTime());
                        fechaNacimiento_LD = fechaNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        //fechaNacimiento_LD = fechaNacimiento_LD.minusYears(1900);
                    }
                    
                    
                    //Traza
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    
                    System.out.println("Datos del cliente con DNI "+dni+":");
                    System.out.println("idCliente: " + idCliente);
                    System.out.println("nombre: " + nombre);
                    System.out.println("apellidos: " + apellidos);
                    System.out.println("dni: " + dni);
                    System.out.println("pasaporte: " + pasaporte);
                    System.out.println("tipoTarjeta: " + tipoTarjeta);
                    System.out.println("numeroTarjeta: " + numeroTarjeta);
                    System.out.println("direccion: " + direccion);
                    System.out.println("poblacion: " + poblacion);
                    System.out.println("cp: " + cp);
                    System.out.println("telefonoFijo: " + telefonoFijo);
                    System.out.println("telefonoMovil: " + telefonoMovil);
                    System.out.println("email: " + email);
                    if(fechaNacimiento_LD==null)
                        System.out.println("fechaNacimiento: null" );
                    else
                        System.out.println("fechaNacimiento: " + dtf.format(fechaNacimiento_LD));
                    
                    //Lo añadimos al mapa
                    datosCliente.put("idCliente", idCliente);
                    datosCliente.put("nombre", nombre);
                    datosCliente.put("apellidos", apellidos);
                    datosCliente.put("dni", dni);
                    datosCliente.put("pasaporte", pasaporte);
                    datosCliente.put("tipoTarjeta", tipoTarjeta);
                    datosCliente.put("numeroTarjeta", numeroTarjeta);
                    datosCliente.put("direccion", direccion);
                    datosCliente.put("poblacion", poblacion);
                    datosCliente.put("cp", cp);
                    datosCliente.put("telefonoFijo", telefonoFijo);
                    datosCliente.put("telefonoMovil", telefonoMovil);
                    datosCliente.put("email", email);
                    datosCliente.put("fechaNacimiento", fechaNacimiento_LD);
                    
                }
                
                con.commit();
                
            }catch(SQLException e){
                con.rollback();
                System.out.print(e.getMessage());
            }catch(Exception e){
                    System.out.print(e.getMessage());
                    e.printStackTrace();
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps!=null) try{ps.close();} catch(SQLException e){}
            	if(con!=null) try{con.close();} catch(SQLException e){}
                
                return datosCliente;
            }
        }
        
        
        public static Map<String,Object> dameDatosClienteConPasaporte(String pasaporte){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            Map<String,Object> datosCliente = new HashMap<>();
            System.out.println("\n\nTraza dameDatosClienteConPasaporte()");
            
            //Por defecto suponemos que no existe
            datosCliente.put("existe", false);

            try{
                con = login();
                con.setAutoCommit(false);
                
                String query = " SELECT * FROM CLIENTE WHERE pasaporte = ?";
                ps = con.prepareStatement(query);
                ps.setString(1, pasaporte);
                rs = ps.executeQuery();
                
                while(rs.next()){
                    //Si entramos aqui es que existe
                    datosCliente.put("existe", true);
                    
                    int idCliente = rs.getInt("idCliente");
                    String nombre = rs.getString("nombre");
                    String apellidos = rs.getString("apellidos");
                    String dni = rs.getString("dni");
                    String tipoTarjeta = rs.getString("tipo_tarjeta");
                    String numeroTarjeta = rs.getString("tarjeta");
                    String direccion = rs.getString("direccion");
                    String poblacion = rs.getString("poblacion");
                    String cp = rs.getString("cp");
                    String telefonoFijo = rs.getString("telefono_fijo");
                    String telefonoMovil = rs.getString("movil");
                    String email = rs.getString("email");
                    
                    LocalDate fechaNacimiento_LD;
                    if(rs.getDate("fecha_nacimiento")==null)
                        fechaNacimiento_LD=null;
                    else{
                        java.util.Date fechaNacimiento = new java.util.Date(rs.getDate("fecha_nacimiento").getTime());
                        fechaNacimiento_LD = fechaNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        //fechaNacimiento_LD = fechaNacimiento_LD.minusYears(1900);
                    }
                    
                    //Traza
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    
                    System.out.println("Datos del cliente con PASAPORTE "+pasaporte+":");
                    System.out.println("idCliente: " + idCliente);
                    System.out.println("nombre: " + nombre);
                    System.out.println("apellidos: " + apellidos);
                    System.out.println("dni: " + dni);
                    System.out.println("pasaporte: " + pasaporte);
                    System.out.println("tipoTarjeta: " + tipoTarjeta);
                    System.out.println("numeroTarjeta: " + numeroTarjeta);
                    System.out.println("direccion: " + direccion);
                    System.out.println("poblacion: " + poblacion);
                    System.out.println("cp: " + cp);
                    System.out.println("telefonoFijo: " + telefonoFijo);
                    System.out.println("telefonoMovil: " + telefonoMovil);
                    System.out.println("email: " + email);
                    if(fechaNacimiento_LD==null)
                        System.out.println("fechaNacimiento: null" );
                    else
                        System.out.println("fechaNacimiento: " + dtf.format(fechaNacimiento_LD));
                    
                    //Lo añadimos al mapa
                    datosCliente.put("idCliente", idCliente);
                    datosCliente.put("nombre", nombre);
                    datosCliente.put("apellidos", apellidos);
                    datosCliente.put("dni", dni);
                    datosCliente.put("pasaporte", pasaporte);
                    datosCliente.put("tipoTarjeta", tipoTarjeta);
                    datosCliente.put("numeroTarjeta", numeroTarjeta);
                    datosCliente.put("direccion", direccion);
                    datosCliente.put("poblacion", poblacion);
                    datosCliente.put("cp", cp);
                    datosCliente.put("telefonoFijo", telefonoFijo);
                    datosCliente.put("telefonoMovil", telefonoMovil);
                    datosCliente.put("email", email);
                    datosCliente.put("fechaNacimiento", fechaNacimiento_LD);
                    
                }
                con.commit();
                
            }catch(SQLException e){
                con.rollback();
                System.out.print(e.getMessage());
            }catch(Exception e){
                    System.out.print(e.getMessage());
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps!=null) try{ps.close();} catch(SQLException e){}
            	if(con!=null) try{con.close();} catch(SQLException e){}
                
                return datosCliente;
            }
        }
        
        public static int crearCliente(String nombre,String apellidos,String dni,String pasaporte,String tipoTarjeta,
                String numeroTarjeta, String direccion,String poblacion,String cp,String telefonoFijo,
                            String telefonoMovil,String email,LocalDate fechaNacimiento){
            
            System.out.println("\n\ncrearCliente()");
            
            Connection con = null;
            PreparedStatement ps1 = null;
            PreparedStatement ps2 = null;
            ResultSet rs = null;
            
            Integer idCliente = null;
            
            try{
                con = login();
                
                Integer siguienteId = null;
            
                //Sacamos el maximo valor de Id que haya en la tabla (Oracle no tiene ID autoincremental)
                int maxId = 0;
                String query = "SELECT MAX(idCliente) from CLIENTE";
                ps1 = con.prepareStatement(query);
                rs = ps1.executeQuery();
                while(rs.next())
                    maxId=rs.getInt(1);
                //El id de la tupla que tenemos que añadir ahora sera maxId+1
                siguienteId = maxId + 1;
                
                idCliente = siguienteId;
                
                System.out.println("siguienteIdCliente: "+idCliente);
                
                //Creamos la tupla en la tabla cliente
                String insert_CLIENTE = "INSERT INTO CLIENTE (idCliente,nombre,apellidos,dni,pasaporte,tipo_tarjeta,tarjeta,"
                        + "email,fecha_nacimiento,movil,telefono_fijo,direccion,poblacion,cp) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                ps2 = con.prepareStatement(insert_CLIENTE);
                ps2.setInt(1, idCliente);//idCliente
                ps2.setString(2, nombre);//nombre
                ps2.setString(3, apellidos);//apellidos
                ps2.setString(4, dni);//dni
                ps2.setString(5, pasaporte);//pasaporte
                ps2.setString(6, tipoTarjeta);//tipo_tarjeta
                ps2.setString(7, numeroTarjeta);//tarjeta
                ps2.setString(8, email);//email
                
                if(fechaNacimiento!=null){
                    java.util.Date fechaNacimiento_Date = Date.from(fechaNacimiento.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    java.sql.Date fechaNacimiento_SQLDate = new java.sql.Date(fechaNacimiento_Date.getTime());
                    ps2.setDate(9, fechaNacimiento_SQLDate);//fecha_nacimiento
                }else
                    ps2.setDate(9, null);//fecha_nacimiento NULL
                
                ps2.setString(10, telefonoMovil);//movil
                ps2.setString(11, telefonoFijo);//telefono_fijo
                ps2.setString(12, direccion);//direccion
                ps2.setString(13, poblacion);//poblacion
                ps2.setString(14, cp);//cp
                
                

                if(ps2.executeUpdate()==1)
                    System.out.println("Tupla insertada en la tabla cliente correctamente");
                else
                    System.out.println("Fallo al crear el cliente");
                
                
            }catch(SQLException e){
                System.out.print(e.getMessage());
            }catch(Exception e){
                System.out.print(e.getMessage());
                e.printStackTrace();
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps1!=null) try{ps1.close();} catch(SQLException e){}
              	if(ps2!=null) try{ps2.close();} catch(SQLException e){}
            	if(con!=null) try{con.close();} catch(SQLException e){}
                    
                return idCliente;
            }
        }
        
        public static void modificarDatosCliente(int idCliente, String nombre,String apellidos,String dni,String pasaporte,String tipoTarjeta,
                String numeroTarjeta, String direccion,String poblacion,String cp,String telefonoFijo,
                            String telefonoMovil,String email,LocalDate fechaNacimiento){
            
            System.out.println("\n\nmodificarDatosCliente()");
            
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            
            try{
                con = login();

                String update = "UPDATE CLIENTE SET nombre = ?, apellidos = ?,dni = ?,pasaporte = ?,tipo_tarjeta = ?,tarjeta = ?,"
                        + "email = ?,fecha_nacimiento = ?,movil = ?,telefono_fijo = ?,direccion = ?,poblacion = ?,cp = ? "
                        + "WHERE idCliente = ?";
                ps = con.prepareStatement(update);
                
                ps.setString(1, nombre);//nombre
                ps.setString(2, apellidos);//apellidos
                ps.setString(3, dni);//dni
                ps.setString(4, pasaporte);//pasaporte
                ps.setString(5, tipoTarjeta);//tipo_tarjeta
                ps.setString(6, numeroTarjeta);//tarjeta
                ps.setString(7, email);//email
                
                if(fechaNacimiento!=null){
                    java.util.Date fechaNacimiento_Date = Date.from(fechaNacimiento.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    java.sql.Date fechaNacimiento_SQLDate = new java.sql.Date(fechaNacimiento_Date.getTime());
                    ps.setDate(8, fechaNacimiento_SQLDate);//fecha_nacimiento
                }else
                    ps.setDate(8, null);//fecha_nacimiento NULL
                
                ps.setString(9, telefonoMovil);//movil
                ps.setString(10, telefonoFijo);//telefono_fijo
                ps.setString(11, direccion);//direccion
                ps.setString(12, poblacion);//poblacion
                ps.setString(13, cp);//cp
                
                ps.setInt(14,idCliente);

                if(ps.executeUpdate()==1)
                    System.out.println("Tupla actualizada en la tabla cliente correctamente");

                
            }catch(SQLException e){
                System.err.print(e.getMessage());
            }catch(Exception e){
                System.err.print(e.getMessage());
            }finally{
                //Cerramos todas las conexiones en el finally (por si se produjo algun fallo antes cerrarlo todo bien)
            	if(rs!=null) try{rs.close();} catch(SQLException e){}
            	if(ps!=null) try{ps.close();} catch(SQLException e){}
            	if(con!=null) try{con.close();} catch(SQLException e){}
            }
            
            
        }
        
        public static boolean existeDniEnlaBD(String dni){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            //Tengo que hacer una consulta para ver si existe un cliente en la BD con el mismo dni
            //Si el rs es vacio es que no existe
            boolean existe = false;
            System.out.println("\n\nVamos a comprobar si existe en la BD un cliente con el dni: "+dni);
                        
            try{
                con = login();
                String query = " SELECT * FROM CLIENTE WHERE dni = ? ";
                
                ps = con.prepareStatement(query);
                ps.setString(1, dni);
                rs = ps.executeQuery();
                
                //La consulta o devuelve una tupla o no devuelve ninguna
                while(rs.next())
                    existe=true;//Si entramos dentro del bucle es que tenia una tupla por lo que existia ya ese alimento
                
                if(existe)
                    System.out.println("EXISTE un cliente con ese DNI");
                else
                    System.out.println("NO EXISTE un cliente con ese DNI");
                            
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
        
        public static boolean existePasaporteEnlaBD(String pasaporte){
            Connection con = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            //Tengo que hacer una consulta para ver si existe un cliente en la BD con el mismo PASAPORTE
            //Si el rs es vacio es que no existe
            boolean existe = false;
            System.out.println("\n\nVamos a comprobar si existe en la BD un cliente con el pasaporte: "+pasaporte);
                        
            try{
                con = login();
                String query = " SELECT * FROM CLIENTE WHERE pasaporte = ? ";
                
                ps = con.prepareStatement(query);
                ps.setString(1, pasaporte);
                rs = ps.executeQuery();
                
                //La consulta o devuelve una tupla o no devuelve ninguna
                while(rs.next())
                    existe=true;//Si entramos dentro del bucle es que tenia una tupla por lo que existia ya ese alimento
                
                if(existe)
                    System.out.println("EXISTE un cliente con ese PASAPORTE");
                else
                    System.out.println("NO EXISTE un cliente con ese PASAPORTE");
                            
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
        
}