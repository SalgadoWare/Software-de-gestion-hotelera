/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carlos.historias_8_9_10;

/**
 *
 * @author carlos
 */
public class ParIdReservaIdHabitacion {
    private int idReserva;
    private String idHabitacion;

    public ParIdReservaIdHabitacion(int idReserva, String idHabitacion) {
        this.idReserva = idReserva;
        this.idHabitacion = idHabitacion;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public String getIdHabitacion() {
        return idHabitacion;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public void setIdHabitacion(String idHabitacion) {
        this.idHabitacion = idHabitacion;
    }
    
    
}