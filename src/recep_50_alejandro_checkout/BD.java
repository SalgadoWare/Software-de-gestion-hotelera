package recep_50_alejandro_checkout;

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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BD {

	public static Connection login() throws SQLException {
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		String url = "jdbc:oracle:thin:@156.35.94.99:1521:DESA";
		String username = "UO267399";
		String password = "UO2673991";
		return DriverManager.getConnection(url, username, password);
	}

	public static Cliente readCliente(String dni) {
		Cliente t = null;

		try {
			Connection con = login();
			Statement statement = con.createStatement();
			String query = "SELECT idcliente, tarjeta, nombre , apellidos FROM cliente WHERE dni = '" + dni + "'";
			ResultSet rs = statement.executeQuery(query);
			rs.next();
			t = new Cliente(String.valueOf(rs.getInt(1)), rs.getString(2), rs.getString(3), rs.getString(4));
			rs.close();
			statement.close();
			con.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("bd excepcion3 " + e.getMessage());
		}

		return t;
	}

	public static List<recep_50_alejandro_checkout.ReservaHabitacion> readAllReservasHabitacionConfirmadas(String id) {
		List<recep_50_alejandro_checkout.ReservaHabitacion> l = new ArrayList<>();
		try {
			Connection con = login();
			Statement statement = con.createStatement();
			String query = "select distinct reserva_habitacion.FECHAENTRADA,reserva_habitacion.FECHASALIDA, reserva_habitacion.idhabitacion, modalidad_reserva_habitacion.TIPODEMODALIDAD from RESERVA natural join RESERVA_HABITACION natural join modalidad_reserva_habitacion WHERE RESERVA.IDCLIENTE =  "
					+ "'" + id + "'" + " and reserva.estado = 'confirmada' order by RESERVA_HABITACION.IDHABITACION";
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				recep_50_alejandro_checkout.ReservaHabitacion t = new recep_50_alejandro_checkout.ReservaHabitacion(
						rs.getDate(1), rs.getDate(2), rs.getString(3), rs.getString(4), 0);
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

	public static Tarifaytipo readTarifayTipo(String idHabitacion) {
		Tarifaytipo l = null;
		try {
			Connection con = login();
			Statement statement = con.createStatement();
			String query = "select tarifa, tipo from hth natural join tipohabitacion where idhabitacion = " + "'"
					+ idHabitacion + "'";
			ResultSet rs = statement.executeQuery(query);
			rs.next();
			String tipo_s = null;
			switch (rs.getInt(2)) {
			case 1:
				tipo_s = "individual";
				break;
			case 2:
				tipo_s = "doble";
				break;
			case 3:
				tipo_s = "triple";
				break;

			default:
				break;
			}
			l = new Tarifaytipo(rs.getInt(1), tipo_s);
			rs.close();
			statement.close();
			con.close();
			return l;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("bd excepcion1 " + e.getMessage());
			return null;
		}
	}

	@SuppressWarnings("finally")
	public static List<Extra> damePromocionesYSuplementos() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Extra> listaExtras = new LinkedList<>();
		System.out.println("\n\nTraza damePromocionesYSuplementos()");

		try {
			con = login();
			String query = "SELECT idExtra, FechaInicio, FechaFin, porcentaje, tipo FROM EXTRAS";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();

			int i = 1;
			while (rs.next()) {
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
				// Pasamos los java.sql.Date que nos devuelve la BD a los
				// LocalDate con los que trabajamos
				// Para ello primero creamos un util.Date pasandole el tiempo en
				// ms del sql.Date
				// Luego creamos el LocalDate usando el Date
				// Tenemos que restar 1900 al año porque viene sumado 1900

				// FechaInicio
				java.util.Date fechaIni = new java.util.Date(rs.getDate(2).getTime());
				LocalDate fechaIniLD = fechaIni.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				// fechaIniLD = fechaIniLD.minusYears(1900);

				// FechaFin
				java.util.Date fechaFin = new java.util.Date(rs.getDate(3).getTime());
				LocalDate fechaFinLD = fechaFin.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				// fechaFinLD = fechaFinLD.minusYears(1900);

				int id = rs.getInt(1);
				double porcentaje = rs.getDouble(4);
				String tipo = rs.getString(5);

				// Traza
				System.out.println("ID = " + id);
				System.out.println("FechaIni = " + dtf.format(fechaIniLD));
				System.out.println("FechaFin = " + dtf.format(fechaFinLD));
				System.out.println("Porcentaje = " + porcentaje);
				System.out.println("Tipo = " + tipo + "\n");

				// Creamos un extra
				Extra extra = new Extra(id, fechaIniLD, fechaFinLD, porcentaje, tipo);

				// Lo añadimos a la lista de extras
				listaExtras.add(extra);
			}

		} catch (SQLException e) {
			System.out.print(e.getMessage());
		} finally {
			// Cerramos todas las conexiones en el finally (por si se produjo
			// algun fallo antes cerrarlo todo bien)
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if (ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
				}

			return listaExtras;
		}
	}

	public static List<Extra> damePromocionesYSuplementosEntreFechas(LocalDate fechaInicioRango,
			LocalDate fechaFinRango) {
		// Obtenemos todas las promociones
		List<Extra> listaExtras = BD.damePromocionesYSuplementos();
		// Eliminamos aquellas que no caen dentro del rango indicado por el
		// usuario
		Iterator<Extra> itr = listaExtras.iterator();
		while (itr.hasNext()) {
			Extra extra = itr.next();
			if (!Util.fechasCaenDentroDelRango(extra.fechaIni, extra.fechaFin, fechaInicioRango, fechaFinRango))
				itr.remove();
		}

		return listaExtras;
	}

	public static Integer readSuplementoModalidad(String modalidad, String tipo) {
		Integer s = null;
		try {
			Connection con = login();
			Statement statement = con.createStatement();
			int tipo_i = 0;
			switch (tipo) {
			case "individual":
				tipo_i = 1;
				break;
			case "doble":
				tipo_i = 2;
				break;
			case "triple":
				tipo_i = 3;
				break;
			default:
				break;
			}
			String query = "select suplemento from modalidad where tipohabitacion = " + tipo_i + " and tipo = " + "'"
					+ modalidad + "'";
			ResultSet rs = statement.executeQuery(query);
			rs.next();
			s = rs.getInt(1);
			rs.close();
			statement.close();
			con.close();
			return s;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("bd excepcion2 " + e.getMessage());
			return null;
		}
	}

	public static void updateReservaFacturada(String id) {
		try {
			Connection con = login();
			Statement statement = con.createStatement();
			String query = "update reserva set estado = 'facturada' where idcliente = " + Integer.parseInt(id)
					+ " and estado = 'confirmada'";
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
