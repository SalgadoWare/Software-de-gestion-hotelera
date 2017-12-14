package codigo.com.toedter.micalendar;

import java.util.ArrayList;

public class TransferHorario {

	public ArrayList<Dia> dias;
	public String uo;
	public String tipo;

	public TransferHorario(ArrayList<Dia> dias2, String uo ,String tipo2 ) {
		dias = dias2;
		this.tipo = tipo2;
		this.uo = uo;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub

		String s = "";
		for (Dia d : dias) {
			s += d.toString();
			s += "\n";
		}
		return s + " " + uo +" " + " " +tipo;
	}
}
