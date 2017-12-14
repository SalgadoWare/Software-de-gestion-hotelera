/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package historia2.recursos;

import java.util.List;

import historia2.negocio.TransferModalidad;
import jdbc.BD;

/**
 *
 * @author alex
 */
public class DAOModalidad {

    public DAOModalidad() {
    }

	public List<TransferModalidad> readAll() {
		// TODO Auto-generated method stub
		return BD.readAllModalidades();
	}

	public void write(TransferModalidad transferModalidad) throws Exception {
		// TODO Auto-generated method stub
		if (!BD.crearModalidad(transferModalidad.getFechaIni(), transferModalidad.getFechaFin(),
				transferModalidad.getPrecio(), transferModalidad.getId(), transferModalidad.getTipohabitacion()))
			throw new Exception("Fallo al aceder a la BBDD");
	}
    
}
