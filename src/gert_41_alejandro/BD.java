package gert_41_alejandro;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BD {
	
	public static Connection login() throws SQLException {
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		String url = "jdbc:oracle:thin:@156.35.94.99:1521:DESA";
		String username = "UO267399";
		String password = "UO2673991";
		return DriverManager.getConnection(url, username, password);
	}

	public static List<TrabajadoresConEstado> readAllTrabajadoresConEstado(String tipo) {
		List<TrabajadoresConEstado> l = new ArrayList<>();
		try {
			Connection con = login();
			Statement statement = con.createStatement();
			String query = "SELECT * FROM TRABAJADOR natural join ESTADO_TRABAJADOR WHERE TIPO = '" + tipo + "'"
					+ " order by estado";
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				TrabajadoresConEstado t = new TrabajadoresConEstado(rs.getString(1), rs.getString(2), rs.getString(3),
						rs.getString(6), rs.getDate(7), rs.getDate(8));
				l.add(t);
			}
			rs.close();
			statement.close();
			con.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("bd excepcion2 " + e.getMessage());
		}

		return l;
	}

	public static List<TrabajadoresActivos> readAllTrabajadoresActivos(String tipo) {
		List<TrabajadoresActivos> l = new ArrayList<>();
		try {
			Connection con = login();
			Statement statement = con.createStatement();
			String query = "select distinct * from TRABAJADOR, DIA 	where tipo = '" + tipo + "'"
					+ "and TRABAJADOR.NOMBRE_USUARIO = DIA.ID_TURNO order by fecha";
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				TrabajadoresActivos t = new TrabajadoresActivos(rs.getString(1), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getInt(10), rs.getDate(11));
				l.add(t);
			}
			rs.close();
			statement.close();
			con.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("bd excepcion1 " + e.getMessage());
		}

		return l;
	}
}
