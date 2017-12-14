package recep_52_alejandro_checkin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import recep_50_alejandro_checkout.Cliente;
import recep_50_alejandro_checkout.ReservaHabitacion;


//prerequisito: en la bbdd solo puede haber una reserva del cliente en estado sin_confirmar
public class BD {
	public static Connection login() throws SQLException {
		DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		String url = "jdbc:oracle:thin:@156.35.94.99:1521:DESA";
		String username = "UO267399";
		String password = "UO2673991";
		return DriverManager.getConnection(url, username, password);
	}

	public static List<ReservaHabitacion> readAllReservasHabitacionSinConfirmadas(String id) {
		List<recep_50_alejandro_checkout.ReservaHabitacion> l = new ArrayList<>();
		try {
			Connection con = login();
			Statement statement = con.createStatement();
			String query = "select distinct reserva_habitacion.FECHAENTRADA,reserva_habitacion.FECHASALIDA, idhabitacion, tipodemodalidad, TIPO from RESERVA natural join RESERVA_HABITACION natural join MODALIDAD_RESERVA_HABITACION natural join hth natural join tipohabitacion WHERE RESERVA.IDCLIENTE =  "
					+ Integer.parseInt(id)
					+ " and reserva.estado = 'sin_confirmar'  and current_date >= TIPOHABITACION.fechaInicio and current_date <= TIPOHABITACION.fechaFin order by IDHABITACION";
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				recep_50_alejandro_checkout.ReservaHabitacion t = new recep_50_alejandro_checkout.ReservaHabitacion(
						rs.getDate(1), rs.getDate(2), rs.getString(3), rs.getString(4), rs.getInt(5));

				if (rs.getDate(1) != null)
					l.add(t);
				else {
					t = null;
					break;
				}
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

	public static Cliente readCliente(String dni) {
		Cliente t = null;

		try {
			Connection con = login();
			Statement statement = con.createStatement();
			String query = "SELECT idcliente, tarjeta, nombre , apellidos FROM cliente WHERE dni = '" + dni + "'";
			ResultSet rs = statement.executeQuery(query);
			rs.next();
			t = new Cliente(String.valueOf(rs.getInt(1)), rs.getString(2), rs.getString(3), rs.getString(4));
			if (t.id == null)
				t = null;
			rs.close();
			statement.close();
			con.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("bd excepcion3 " + e.getMessage());
		}

		return t;
	}

	public static void updateReservaConfirmada(String id) {
		try {
			Connection con = login();
			Statement statement = con.createStatement();
			String query = "update reserva set estado = 'confirmada' where idcliente = " + Integer.parseInt(id)
					+ " and estado = 'sin_confirmar'";
			ResultSet rs = statement.executeQuery(query);
			rs.close();
			statement.close();
			con.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("bd excepcion46 " + e.getMessage());
		}
	}

	public static void writeAcompanientes(TransferAcompanante transferAcompanante) {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {

			// Conectamos con la BD
			con = login();

			// Sacamos el maximo valor de Id que haya en la tabla (Oracle no
			// tiene
			// ID autoincremental)
			int maxId = 0;
			st = con.createStatement();
			String query = "SELECT MAX(IDACOMPANIANTE) from ACOMPANIANTE";
			rs = st.executeQuery(query);
			while (rs.next())
				maxId = rs.getInt(1);
			// El id de la tupla que tenemos que a�adir ahora sera maxId+1
			int siguienteId = maxId + 1;

			// Creamos ahora el INSERT (Usando un PreparedStatement porque
			// tenemos
			// parametros)
			String insert = "INSERT INTO Acompaniante VALUES(?,?,?,?,?,?)";
			ps = con.prepareStatement(insert);
			ps.setInt(1, siguienteId);
			ps.setString(2, transferAcompanante.apellidos);
			ps.setString(3, transferAcompanante.nombre);
			ps.setString(4, transferAcompanante.dni);
			ps.setString(5, transferAcompanante.pasaporte);
			ps.setDate(6, new java.sql.Date(transferAcompanante.nace.getTime()));

			// Ejecutamos el insert y si se ejecuto correctamente nos devuelve 1
			if (ps.executeUpdate() == 1)
				System.out.println("Introducido correctamente");

			rs.close();
			ps.close();
			con.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}

	}

	// public static void writeReservaAcompanante(int idAcompanate, int
	// idReserva) {
	// // TODO Auto-generated method stub
	//
	// }

	public static int readIDReserva(String id) {
		int i = 0;

		try {
			Connection con = login();
			Statement statement = con.createStatement();
			String query = "SELECT idreserva  FROM reserva WHERE idcliente = " + Integer.valueOf(id)
					+ " and estado = 'sin_confirmar'";
			ResultSet rs = statement.executeQuery(query);
			rs.next();
			i = rs.getInt(1);
			rs.close();
			statement.close();
			con.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("bd excepcion3 " + e.getMessage());
		}

		return i;
	}

	public static void deleteReservasHabitacion(int idReserva) {
		try {
			Connection con = login();
			Statement statement = con.createStatement();
			String query = "delete FROM reserva_habitacion WHERE idreserva = " + idReserva;
			ResultSet rs = statement.executeQuery(query);
			rs.close();
			statement.close();
			con.close();

		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("bd excepcion37 " + e.getMessage());
		}
	}
}
