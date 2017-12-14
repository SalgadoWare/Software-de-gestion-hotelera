package recep_50_alejandro_checkout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.swing.JTextArea;

public class SACheckout {

	public void checkout(Cliente cliente, JTextArea jTextArea1, List<ReservaHabitacion> listaDeReservasHabitaciones) {
		// TODO Auto-generated method stub

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String formatted = df.format(new Date());

		jTextArea1
				.append("                                                                                                           Hotel ips - "
						+ formatted);
		jTextArea1.append(
				"\n  Cliente: " + cliente.nombre + " " + cliente.apellidos + " con targeta: " + cliente.targeta);
		jTextArea1.append(" \n\n");

		float total = 0;

		List<Float> ListaDePreciosPorHabitacion = new ArrayList<>();

		for (ReservaHabitacion rv : listaDeReservasHabitaciones) {

			Tarifaytipo tyt = BD.readTarifayTipo(rv.getIdHabitacion());

			int days = daysBetween(rv.getFechaEntrada(), rv.getFechaSalida());

			Float parcialTarifas = (float) (days * tyt.tarifa);

			Integer suplemento_modalidad = BD.readSuplementoModalidad(rv.getModalidad(), tyt.tipo);
			System.out.println(suplemento_modalidad);
			jTextArea1.append("-Habitacion: " + rv.getIdHabitacion() + "  (" + rv.getFechaEntrada().toString() + "  "
					+ rv.getFechaSalida().toString() + ")" + "\n durante " + days + " noches, con una tarifa de: "
					+ tyt.tarifa + " e/noche ...                                                            "
					+ parcialTarifas + " $");

			Double parcial_modalidades = (double) (parcialTarifas + suplemento_modalidad * days);
			jTextArea1.append("\n" + " Con una modalidad " + rv.getModalidad() + " de: " + suplemento_modalidad
					+ " e/noche ...                                                                            "
					+ parcial_modalidades + " $");

			List<Extra> listadeExtras = BD.damePromocionesYSuplementosEntreFechas(fromDate(rv.getFechaEntrada()),
					fromDate(rv.getFechaSalida()));

			System.out.println("---------------------------");
			System.out.println(rv.getIdHabitacion() + " " + rv.getFechaEntrada().toString() + " "
					+ rv.getFechaSalida().toString());

			List<Double> l = new ArrayList<>();
			for (Extra x : listadeExtras) {

				int diasSolapado = 0;

				diasSolapado = diasSolapadoIntervalo(new Date(rv.getFechaEntrada().getTime()),
						new Date(rv.getFechaSalida().getTime()), DateUtils.asDate(x.fechaIni),
						DateUtils.asDate(x.fechaFin));

				float precioPorDia = (float) (parcial_modalidades / (days));

				float dineroRealivoAlextra = diasSolapado * precioPorDia;

				Double extra = dineroRealivoAlextra * (x.porcentaje / 100);

				switch (x.tipo) {
				case "suplemento":
					l.add(extra);
					break;
				default:
					extra = extra - (2 * extra);
					l.add(extra);
					break;
				}
				
				

				jTextArea1.append("\n  Se solapa " + diasSolapado + " dias  con un " + x.tipo + " de " + x.porcentaje
						+ " % (" + x.fechaIni.toString() + "  " + x.fechaFin.toString() + ")  ...           " + extra.floatValue()
						+ "$");

				System.out.println(x);
			}

			for (Double e : l) {
				parcial_modalidades += e;
			}

			jTextArea1
					.append("\n                                                                                                                                            Total ..."
							+ parcial_modalidades.floatValue() + "$");

			System.out.println("---------------------------");

			jTextArea1.append("\n\n");

			ListaDePreciosPorHabitacion.add(parcial_modalidades.floatValue());
		}

		float finalmoney = 0;

		for (Float f : ListaDePreciosPorHabitacion) {
			finalmoney += f;
		}

		jTextArea1.append("\nEl precio final de la reserva es de " + finalmoney + " $");

		BD.updateReservaFacturada(cliente.id);

	}

	private int diasSolapadoIntervalo(Date a, Date b, Date ap, Date bp) {

		a = DateUtils.setTimeToMidnight(a);
		ap = DateUtils.setTimeToMidnight(ap);
		b = DateUtils.setTimeToMidnight(b);
		bp = DateUtils.setTimeToMidnight(bp);

		if (ap.compareTo(a) <= 0 && bp.compareTo(b) >= 0)
			return DateUtils.daysBetween(a, b);
		
		if (ap.compareTo(a) >= 0 && bp.compareTo(b) <= 0)
			return DateUtils.daysBetween(ap, bp);

		if (ap.compareTo(a) < 0 && bp.compareTo(b) <= 0)
			return DateUtils.daysBetween(a, bp);

		if (ap.compareTo(a) >= 0 && bp.compareTo(b) >= 0)
			return DateUtils.daysBetween(ap, b);

		return 0;
	}

	public static LocalDate fromDate(Date date) {
		Instant instant = Instant.ofEpochMilli(date.getTime());
		return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
	}

	public int daysBetween(Date d1, Date d2) {
		return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}
}
