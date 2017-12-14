/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package historia1.negocio;

import java.util.List;

import historia1.recursos.DAOTarifa;
import util.*;;

/**
 *
 * @author alex
 */
public class SATarifa {
	// 1 sencilla 2 doble 3 triple

	public SATarifa() {
	}

	public void alta(TransferTarifa transferTarifa) throws Exception {

		System.out.println("sa: me llega  " + transferTarifa);
		List<TransferTarifa> tarifas = new DAOTarifa().readAll();
		
		for(TransferTarifa t : tarifas) {
			System.out.println("sa:  recivo del dao " + t);
		}
		
		int id = transferTarifa.getId();
		
		for(TransferTarifa t : tarifas) {
			if(t.getId() == id) {
				if(!Util.esValida(t.getFechaIni(), t.getFechaFin(), transferTarifa.getFechaIni(), transferTarifa.getFechaFin())) {
					throw new Exception("Las fechas se solapan");
				}
			}
		}
		
		new DAOTarifa().write(transferTarifa);
	}

}
