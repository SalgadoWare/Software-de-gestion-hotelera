package recep_50_alejandro_checkout;
import java.time.LocalDate;
import java.util.Date;

public class Util {
    public static boolean esValida(Date inicioRango, Date finRango, Date fechaInicio, Date fechaFinal) {
            return fechaInicio.after(finRango) || fechaFinal.before(inicioRango);
    }

    public static boolean fechasCaenDentroDelRango(LocalDate fechaInicio, LocalDate fechaFinal, LocalDate inicioRango, LocalDate finRango) {
            return !(fechaInicio.isAfter(finRango) || fechaFinal.isBefore(inicioRango));
    }  
}
