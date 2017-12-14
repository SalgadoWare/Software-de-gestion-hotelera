/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package historia1.recursos;
import java.util.List;

import historia1.negocio.TransferTarifa;
import jdbc.BD;

/**
 *
 * @author alex
 */
public class DAOTarifa {

	public DAOTarifa() {
	}

	public List<TransferTarifa> readAll() {
		return BD.readAllTarifas();
	}

	public void write(TransferTarifa transferTarifa) throws Exception {
		if (!BD.crearTipoHabitacion(transferTarifa.getFechaIni(), transferTarifa.getFechaFin(),
				transferTarifa.getPrecio(), transferTarifa.getId()))
			throw new Exception("Fallo al aceder a la BBDD");
	}
}
