package carlos.historias_1_2_3;

import java.time.LocalDate;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author carlos
 */
public class Extra {

    public Extra(int id, LocalDate fechaIni, LocalDate fechaFin, double porcentaje, String tipo) {
        this.id = id;
        this.fechaIni = fechaIni;
        this.fechaFin = fechaFin;
        this.porcentaje = porcentaje;
        this.tipo = tipo;
    }
    
    public int id;
    public LocalDate fechaIni;
    public LocalDate fechaFin;
    public double porcentaje;
    public String tipo;
}
