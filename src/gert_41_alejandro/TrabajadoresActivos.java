package gert_41_alejandro;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TrabajadoresActivos {
	public TrabajadoresActivos(String n, int inicio, int fin, int inicio2, int fin2, Date date) {
		super();
		this.inicio = inicio;
		this.fin = fin;
		this.inicio2 = inicio2;
		this.fin2 = fin2;
		this.date = date;
		nombre = n;
		f = new SimpleDateFormat("dd/MM/yy");
	}

	@Override
	public String toString() {
		String s = "";
		s = "Empieza la jornada laboral a las " + inicio + ", y termina a las " + fin;
		if (inicio2 != -1) {
			s += '\n'+"                               Tiene jornada partida, sale a las " + inicio2
					+ " del turno de manana\n"+"                               "+" y vuelve a entrar por la tarde a las " + fin2 + " (" + f.format(date) + ")";
		}
		return s;
	}

	SimpleDateFormat f;
	String nombre;
	int inicio, fin, inicio2, fin2;
	Date date;
}
