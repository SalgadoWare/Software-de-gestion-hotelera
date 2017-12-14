package codigo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import codigo.com.toedter.micalendar.Dia;

public class BD {

	public static Connection login() throws SQLException {
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		String url = "jdbc:oracle:thin:@156.35.94.99:1521:DESA";
		String username = "UO267399";
		String password = "UO2673991";
		return DriverManager.getConnection(url, username, password);
	}

	public static List<TransferTrabajador> readAllTrabajadores(String tipo) {
		List<TransferTrabajador> l = new ArrayList<>();
		try {
			Connection con = login();
			Statement statement = con.createStatement();
			String query = "SELECT * FROM TRABAJADOR WHERE TIPO = '" + tipo + "'";
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				TransferTrabajador t = new TransferTrabajador("", rs.getString(1), rs.getString(3), rs.getString(2));
				l.add(t);
			}
			rs.close();
			statement.close();
			con.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("bd excepcion3 " + e.getMessage());
		}

		return l;

	}

	public static void writeTurno(String uo) throws SQLException {
		// TODO Auto-generated method stub
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		// Conectamos con la BD
		con = login();

		// Sacamos el maximo valor de Id que haya en la tabla (Oracle no tiene
		// ID autoincremental)
		int maxId = 0;
		st = con.createStatement();
		String query = "SELECT MAX(ID) from TURNO_SEMANAL";
		rs = st.executeQuery(query);
		while (rs.next())
			maxId = rs.getInt(1);
		// El id de la tupla que tenemos que a�adir ahora sera maxId+1
		int siguienteId = maxId + 1;

		// Creamos ahora el INSERT (Usando un PreparedStatement porque tenemos
		// parametros)
		String insert = "INSERT INTO TURNO_SEMANAL VALUES(?,?)";
		ps = con.prepareStatement(insert);
		ps.setInt(1, siguienteId);
		ps.setString(2, uo);

		// Ejecutamos el insert y si se ejecuto correctamente nos devuelve 1
		if (ps.executeUpdate() == 1)
			System.out.println("Introducido correctamente");

		rs.close();
		ps.close();
		con.close();

	}

	public static void writeDias(String uo, ArrayList<Dia> dias) throws SQLException {
		// TODO Auto-generated method stub
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;

		// Conectamos con la BD
		con = login();

		// Sacamos el maximo valor de Id que haya en la tabla (Oracle no tiene
		// ID autoincremental)

		for (Dia dia : dias) {

			int maxId = 0;
			st = con.createStatement();
			String query = "SELECT MAX(ID) from DIA";
			rs = st.executeQuery(query);
			while (rs.next())
				maxId = rs.getInt(1);
			// El id de la tupla que tenemos que a�adir ahora sera maxId+1
			int siguienteId = maxId + 1;

			// Creamos ahora el INSERT (Usando un PreparedStatement porque
			// tenemos parametros)
			String insert = "INSERT INTO DIA VALUES(?,?,?,?,?,?,?)";
			ps = con.prepareStatement(insert);
			ps.setInt(1, siguienteId);
			ps.setString(2, uo);
			ps.setInt(3, dia.firstHour);
			ps.setInt(4, dia.lastHour);
			ps.setInt(5, dia.first2hour);
			ps.setInt(6, dia.last2hour);
			ps.setDate(7, new java.sql.Date(dia.date.getTime()));

			// Ejecutamos el insert y si se ejecuto correctamente nos devuelve 1
			if (ps.executeUpdate() == 1)
				System.out.println("Introducido correctamente");

			rs.close();
			ps.close();
		}

		con.close();
	}

	public static List<Dia> realAllDays(String uo) {
		List<Dia> l = new ArrayList<>();
		try {
			Connection con = login();
			Statement statement = con.createStatement();
			String query = "SELECT * FROM DIA WHERE ID_TURNO = '" + uo + "'";
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				Dia t = new Dia(rs.getInt(3), (rs.getInt(4)), (rs.getInt(5)), (rs.getInt(6)), rs.getDate(7));
				l.add(t);
			}
			rs.close();
			statement.close();
			con.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("bd excepcion4 " + e.getMessage());
		}

		return l;
	}

	public static List<TransferEstado> readEstadosNoActivos(String uo) {
		List<TransferEstado> l = new ArrayList<>();
		try {
			Connection con = login();
			Statement statement = con.createStatement();
			String query = "SELECT ESTADO_TRABAJADOR.ESTADO,ESTADO_TRABAJADOR.FECHA_INICIO, ESTADO_TRABAJADOR.FECHA_FIN FROM TRABAJADOR,ESTADO_TRABAJADOR WHERE TRABAJADOR.NOMBRE_USUARIO = " +"'"+uo+"'"+ "and ESTADO_TRABAJADOR.ESTADO in ('baja','permiso', 'vacaciones') and TRABAJADOR.NOMBRE_USUARIO = ESTADO_TRABAJADOR.NOMBRE_USUARIO";
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				TransferEstado t = new TransferEstado(rs.getString(1), 0, 0, rs.getDate(2), rs.getDate(3));
				l.add(t);
			}
			rs.close();
			statement.close();
			con.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("bd excepcion5 " + e.getMessage());
		}

		return l;
	}

}
