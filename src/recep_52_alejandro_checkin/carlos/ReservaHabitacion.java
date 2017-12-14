package recep_52_alejandro_checkin.carlos;

import java.time.LocalDate;
import java.util.Date;

public class ReservaHabitacion {

    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private String idHabitacion;
    private String modalidad;

    public ReservaHabitacion(LocalDate fechaEntrada, LocalDate fechaSalida, String idHabitacion, String modalidad) {
            this.fechaEntrada = fechaEntrada;
            this.fechaSalida = fechaSalida;
            this.idHabitacion = idHabitacion;
            this.modalidad = modalidad;
    }

    public LocalDate getFechaEntrada() {
            return fechaEntrada;
    }

    public LocalDate getFechaSalida() {
            return fechaSalida;
    }

    public String getIdHabitacion() {
            return idHabitacion;
    }

    public String getModalidad() {
            return modalidad;
    }

    public void setModalidad(String modalidad) {
            this.modalidad = modalidad;
    }

    @Override
    public String toString() {
        return "ReservaHabitacion{" + "fechaEntrada=" + fechaEntrada + ", fechaSalida=" + fechaSalida + ", idHabitacion=" + idHabitacion + ", modalidad=" + modalidad + '}';
    }

        
        
}
