/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package historia2.negocio;
import java.util.List;

import historia2.recursos.DAOModalidad;
import util.*;
/**
 *
 * @author alex
 */
public class SAModalidad {
    	public SAModalidad() {
	}

	public void alta(TransferModalidad transferModalidad) throws Exception {

		System.out.println("sa: me llega " + transferModalidad);
		List<TransferModalidad> modalidades = new DAOModalidad().readAll();
		
		for(TransferModalidad t : modalidades) {
			System.out.println("sa: recivo del dao  " + t);
		}
		
		String id = transferModalidad.getId();
		int i = transferModalidad.getTipohabitacion();
		
		for(TransferModalidad t : modalidades) {
			if(t.getId().equalsIgnoreCase(id) && t.getTipohabitacion() == i) {
				if(!Util.esValida(t.getFechaIni(), t.getFechaFin(), transferModalidad.getFechaIni(), transferModalidad.getFechaFin())) {
					throw new Exception("Las fechas se solapan");
				}
			}
		}
		
		new DAOModalidad().write(transferModalidad);
	}
}
