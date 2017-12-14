package codigo;


import java.util.Date;

public class TransferEstado {
	public TransferEstado(String estado, int id, int idTrabajador, Date inicio, Date fin) {
		super();
		this.estado = estado;
		this.id = id;
		this.idTrabajador = idTrabajador;
		this.inicio = inicio;
		this.fin = fin;
	}
	String estado;
	int id, idTrabajador;
	public int getIdTrabajador() {
		return idTrabajador;
	}
	public void setIdTrabajador(int idTrabajador) {
		this.idTrabajador = idTrabajador;
	}
	Date inicio, fin;

	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getInicio() {
		return inicio;
	}
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	public Date getFin() {
		return fin;
	}
	public void setFin(Date fin) {
		this.fin = fin;
	}

}
