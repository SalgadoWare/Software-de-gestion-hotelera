package gert_41_alejandro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JTextArea;

import com.toedter.calendar.DateUtil;

public class SAEstado {

	List<Date> fechas;
	JTextArea jTextArea1;

	public SAEstado(JTextArea jTextArea1) {
		// TODO Auto-generated constructor stub
		this.jTextArea1 = jTextArea1;
	}

	public void read(String tipo, Date inicio, Date fin) {
		fillDates(inicio, fin);
		List<TrabajadoresConEstado> trabajadoresConEstado = BD.readAllTrabajadoresConEstado(tipo);
		List<TrabajadoresActivos> trabajadoresActivos = BD.readAllTrabajadoresActivos(tipo);
		for (Date date : fechas) {
			SimpleDateFormat f = new SimpleDateFormat("dd/MM/yy");
			jTextArea1.append("\nPara la FECHA " + f.format(date) + "\n");
			if (trabajadoresConEstado.isEmpty()) {
				jTextArea1.append("         Todavia no hay trabajadores asignados a un estado" + "\n");
			} else {
				for (TrabajadoresConEstado tce : trabajadoresConEstado) {
					if (estaEnRango(date, tce.inicio, tce.fin)) {
						jTextArea1.append("         " + tce.toString() + "\n");
						if (tce.estado.equalsIgnoreCase("activo")) {
							for (TrabajadoresActivos ta : trabajadoresActivos) {
								if (ta.nombre.equalsIgnoreCase(tce.nombre) && setTimeToMidnight(ta.date).equals(date))
									jTextArea1.append("                               " + ta.toString() + "\n" + "\n");
							}
						}
					}
				}
			}

		}
	}

	public static Date setTimeToMidnight(Date date) {
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	private boolean estaEnRango(Date date, Date inicio, Date fin) {
		// TODO Auto-generated method stub
		return ((date.after(inicio) && date.before(fin)) || date.equals(inicio) || date.equals(fin));
	}

	private void fillDates(Date inicio, Date fin) {
		fechas = new ArrayList<>();
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(inicio);
		Date ini = new Date(inicio.getTime());
		while (ini.before(fin)) {
			fechas.add(setTimeToMidnight(ini));
			gc.add(GregorianCalendar.DATE, 1);
			ini = gc.getTime();
		}
		for (Date date : fechas) {
			System.out.println(date.toString());
		}
	}
}
