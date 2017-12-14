package carlos.historias_8_9_10;

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
