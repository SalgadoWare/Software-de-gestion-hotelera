package recep_52_alejandro_checkin.carlos;

public class Reserva {
	//propios
	private int idReserva;
	private String tarjeta;
	//cliente
	private int idCliente;
	
	public Reserva(String tarjeta, int idCliente) {
		super();
		this.tarjeta = tarjeta;
		this.idCliente = idCliente;
	}

	public int getIdReserva() {
		return idReserva;
	}

	public String getTarjeta() {
		return tarjeta;
	}

	public int getIdCliente() {
		return idCliente;
	}

	
	
}
