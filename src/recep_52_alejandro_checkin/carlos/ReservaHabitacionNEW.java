/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recep_52_alejandro_checkin.carlos;

import java.time.LocalDate;

/**
 *
 * @author carlos
 */
public class ReservaHabitacionNEW {
    
    private String idHabitacion;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    
    public ReservaHabitacionNEW(String idHabitacion, LocalDate fechaEntrada, LocalDate fechaSalida) {
        this.idHabitacion = idHabitacion;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
    }

    public String getIdHabitacion() {
        return idHabitacion;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setIdHabitacion(String idHabitacion) {
        this.idHabitacion = idHabitacion;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }
    
    

}
