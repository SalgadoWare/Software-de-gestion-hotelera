package codigo;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import codigo.com.toedter.micalendar.Dia;
import codigo.com.toedter.micalendar.TransferHorario;

public class SATurno {

	public void create(TransferHorario t) {
		// TODO Auto-generated method stub
		try {

			List<TransferEstado> estadosNoActivos = BD.readEstadosNoActivos(t.uo);

			boolean statefail = false;
			for (Dia d : t.dias) {
				d.date = setTimeToMidnight(d.date);

				for (TransferEstado te : estadosNoActivos) {
					if (estaEnRango(d.date, te.inicio, te.fin)) {
						statefail = true;
						SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
						JOptionPane.showMessageDialog(new JFrame(), t.uo.toUpperCase() + " el dia " + sdf.format(d.date)
								+ " esta " + te.estado.toUpperCase());
						break;
					}
				}

			}

			if (!statefail) {

				List<Dia> lista = BD.realAllDays(t.uo);

				if (t.dias.size() == 1) {
					// deberia comprobar si es lunes o domingo
				} else {

					boolean fail = false;
					boolean fail2 = false;

					Date first = t.dias.get(0).date;
					System.out.println("                first " + first.toString());
					Calendar c = new GregorianCalendar();
					c.setTime(first);
					if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
						c.add(Calendar.DATE, -1);
						Date ex = c.getTime();
						System.out.println("               ex  " + ex.toString());
						ex = setTimeToMidnight(ex);

						for (Dia dia : lista) {
							if (dia.date.equals(ex) && dia.lastHour - t.dias.get(0).firstHour > 12) {
								fail = true;
								JOptionPane.showMessageDialog(new JFrame(), "El dia " + t.dias.get(0).date.toString()
										+ " no tiene un descanso con el domingo anterior ");
								break;
							}
						}
					}

					if (!fail) {

						Date last = t.dias.get(t.dias.size() - 1).date;
						Calendar c2 = new GregorianCalendar();
						c2.setTime(last);
						if (c2.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {

							c2.add(Calendar.DATE, 1);
							Date ex = c2.getTime();
							ex = setTimeToMidnight(ex);
							fail2 = false;

							for (Dia dia : lista) {
								if (dia.date.equals(ex)
										&& t.dias.get(t.dias.size() - 1).lastHour - dia.firstHour > 12) {
									fail2 = true;
									JOptionPane.showMessageDialog(new JFrame(),
											"El dia " + t.dias.get(0).date.toString()
													+ " no tiene un descanso con el lunes siguiente ");
									break;
								}
							}

						}
						if (!fail2) {
							BD.writeTurno(t.uo);
							BD.writeDias(t.uo, t.dias);
							JOptionPane.showMessageDialog(new JFrame(), "Introducido correctamente");
						}

					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(new JFrame(), "Error en la BBDD");
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
}
