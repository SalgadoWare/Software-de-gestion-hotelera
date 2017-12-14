package recep_52_alejandro_checkin.carlos;

import java.sql.Date;

public class Tipohab {
	private String tipoh;
	private Date fechaIniTH;
	private Date fechaFinTH;
	private int tarifa;
	public Tipohab(String tipoh, Date fechaIniTH, Date fechaFinTH, int tarifa) {
		super();
		this.tipoh = tipoh;
		this.fechaIniTH = fechaIniTH;
		this.fechaFinTH = fechaFinTH;
		this.tarifa = tarifa;
	}
	public Tipohab() {
		super();
	}
	public String getTipoh() {
		return tipoh;
	}
	public void setTipoh(String tipoh) {
		this.tipoh = tipoh;
	}
	public Date getFechaIniTH() {
		return fechaIniTH;
	}
	public void setFechaIniTH(Date fechaIniTH) {
		this.fechaIniTH = fechaIniTH;
	}
	public Date getFechaFinTH() {
		return fechaFinTH;
	}
	public void setFechaFinTH(Date fechaFinTH) {
		this.fechaFinTH = fechaFinTH;
	}
	public int getTarifa() {
		return tarifa;
	}
	public void setTarifa(int tarifa) {
		this.tarifa = tarifa;
	}
	
	

}
