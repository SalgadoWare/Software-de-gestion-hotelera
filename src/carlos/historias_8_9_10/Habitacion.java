package carlos.historias_8_9_10;

import java.sql.Date;

public class Habitacion {
	private String idhab;
	private String estado;
	//tipohab
	private int tipoh;
	private Date fechaIniTH;
	private Date fechaFinH;
	
	
	
	
	public Habitacion(String idhab, int tipo, Date fechaIniTH, Date fechaFinH, String estado) {
		super();
		this.idhab = idhab;
		this.tipoh = tipo;
		this.fechaIniTH = fechaIniTH;
		this.fechaFinH = fechaFinH;
		this.estado = estado;
	}
	
	public Habitacion() {
		
	}

	public String getIdhab() {
		return idhab;
	}

	public void setIdhab(String idhab) {
		this.idhab = idhab;
	}

	public int getTipoh() {
		return tipoh;
	}

	public void setTipoh(int tipoh) {
		this.tipoh = tipoh;
	}

	public Date getFechaIniTH() {
		return fechaIniTH;
	}

	public void setFechaIniTH(Date fechaIniTH) {
		this.fechaIniTH = fechaIniTH;
	}

	public Date getFechaFinH() {
		return fechaFinH;
	}

	public void setFechaFinH(Date fechaFinH) {
		this.fechaFinH = fechaFinH;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}



	

}
