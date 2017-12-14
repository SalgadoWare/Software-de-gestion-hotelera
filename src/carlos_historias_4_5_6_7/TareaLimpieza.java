/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carlos_historias_4_5_6_7;

import java.sql.Timestamp;
import java.time.LocalDate;

/**
 *
 * @author carlos
 */
public class TareaLimpieza {
    public int idTarea;
    public String idHabitacion;
    public Timestamp fechaTarea;
    public String tipo;
    public String observaciones;
    public int prioritaria;
    public Timestamp momento_informada;
    
    public int numeroTarea=0;//Esto es solo para aquellas tareas que sean de una habitacion que tiene mas tareas

    public TareaLimpieza(int idTarea, String idHabitacion, Timestamp fechaTarea, String tipo, String observaciones, int prioritaria, Timestamp momento_informada) {
        this.idTarea = idTarea;
        this.idHabitacion = idHabitacion;
        this.fechaTarea = fechaTarea;
        this.tipo = tipo;
        this.observaciones = observaciones;
        this.prioritaria = prioritaria;
        this.momento_informada = momento_informada;
    }
    
    public TareaLimpieza(TareaLimpieza tl){
        this(tl.idTarea,tl.idHabitacion,tl.fechaTarea,tl.tipo,tl.observaciones,tl.prioritaria,tl.momento_informada);
    }
    
    @Override
    public String toString(){
        if(numeroTarea==0)
            return "Habitacion "+idHabitacion;
        else
            return "Tarea " + numeroTarea;
    }
}