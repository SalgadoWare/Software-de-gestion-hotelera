package gert_44_alejandro;

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

	public static List<TransferTarifa> realAllTipos() {
		List<TransferTarifa> l = new ArrayList<>();
		try {
			Connection con = login();
			Statement statement = con.createStatement();
			String query = "SELECT * FROM TIPOHABITACION order by tipo";
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				TransferTarifa t = new TransferTarifa(rs.getDate(3), rs.getDate(4), rs.getInt(2), (rs.getInt(5)),
						rs.getInt(1));
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

	public static List<TransferHAbitacionPlus> realAllRoomsPlus() {
		List<TransferHAbitacionPlus> l = new ArrayList<>();
		try {
			Connection con = login();
			Statement statement = con.createStatement();
			String query = "select distinct habitacion.IDHABITACION, habitacion.ESTADOHABITACION, tipohabitacion.TIPO, tipohabitacion.FECHAINICIO, tipohabitacion.FECHAFIN, tipohabitacion.tarifa from hth, habitacion, tipohabitacion where hth.IDHABITACION = habitacion.IDHABITACION and hth.IDTH = tipohabitacion.IDTH";
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				TransferHAbitacionPlus t = new TransferHAbitacionPlus(rs.getString(1), rs.getString(2), rs.getInt(3),
						rs.getDate(4), rs.getDate(5), rs.getInt(6));
				l.add(t);
			}
			rs.close();
			statement.close();
			con.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("bd excepcion45 " + e.getMessage());
		}

		return l;
	}

	public static void updateHTH(String idh, int idth) {
		try {
			Connection con = login();
			Statement statement = con.createStatement();
			String query = "update hth set idth = " + idth + " where IDHABITACION = " + "'" + idh + "'";
			ResultSet rs = statement.executeQuery(query);
			rs.close();
			statement.close();
			con.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("bd excepcion46 " + e.getMessage());
		}
	}
}
