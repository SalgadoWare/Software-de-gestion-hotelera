package recep_52_alejandro_checkin.carlos;

import java.sql.Date;

public class Modalidad {
	
	private String tipoM;
	private Date fechaIniM;
	private Date fechaFinM;
	private int suplemento;
	public Modalidad(String tipoM, Date fechaIniM, Date fechaFinM, int suplemento) {
		super();
		this.tipoM = tipoM;
		this.fechaIniM = fechaIniM;
		this.fechaFinM = fechaFinM;
		this.suplemento = suplemento;
	}
	public Modalidad() {
		super();
	}
	public String getTipoM() {
		return tipoM;
	}
	public void setTipoM(String tipoM) {
		this.tipoM = tipoM;
	}
	public Date getFechaIniM() {
		return fechaIniM;
	}
	public void setFechaIniM(Date fechaIniM) {
		this.fechaIniM = fechaIniM;
	}
	public Date getFechaFinM() {
		return fechaFinM;
	}
	public void setFechaFinM(Date fechaFinM) {
		this.fechaFinM = fechaFinM;
	}
	public int getSuplemento() {
		return suplemento;
	}
	public void setSuplemento(int suplemento) {
		this.suplemento = suplemento;
	}
	
	

}
