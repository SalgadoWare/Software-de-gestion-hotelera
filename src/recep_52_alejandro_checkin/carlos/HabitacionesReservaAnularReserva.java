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
public class HabitacionesReservaAnularReserva {
  
    private String idHabitacion;
    private int idReserva;
    private int idModalidadReservaHabitacion;
    private int tipo;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    
    public HabitacionesReservaAnularReserva(String idHabitacion, int idReserva, int idModalidadReservaHabitacion, int tipo, 
            LocalDate fechaEntrada, LocalDate fechaSalida) {
        this.idHabitacion = idHabitacion;
        this.idReserva=idReserva;
        this.idModalidadReservaHabitacion=idModalidadReservaHabitacion;
        this.tipo=tipo;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
    }
    
    public String getIdHabitacion() {
        return idHabitacion;
    }
    
    public int getIdReserva() {
        return idReserva;
    }

    public int getIdModalidadReservaHabitacion() {
        return idModalidadReservaHabitacion;
    }

    public int getTipo() {
        return tipo;
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
    
    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public void setIdModalidadReservaHabitacion(int idModalidadReservaHabitacion) {
        this.idModalidadReservaHabitacion = idModalidadReservaHabitacion;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    } 
    
    
}
