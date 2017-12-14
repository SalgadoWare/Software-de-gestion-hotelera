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
class ValidadorDNI
{
    private String dni;
     
    public boolean isDniValido(String dni) {
 
        this.dni=dni;
         
        String letraMayuscula = ""; //Guardaremos la letra introducida en formato mayúscula
             
        // Aquí excluimos cadenas distintas a 9 caracteres que debe tener un dni y también si el último caracter no es una letra
        if(dni.length() != 9 || Character.isLetter(this.dni.charAt(8)) == false ) {
            return false;
        }
 
         
        // Al superar la primera restricción, la letra la pasamos a mayúscula
        letraMayuscula = (this.dni.substring(8)).toUpperCase();
 
        // Por último validamos que sólo tengo 8 dígitos entre los 8 primeros caracteres y que la letra introducida es igual a la de la ecuación
        // Llamamos a los métodos privados de la clase soloNumeros() y letraDNI()
        if(soloNumeros() == true && letraDNI().equals(letraMayuscula)) {
            return true;
        }
        else {
            return false;
        }
    }
 
    private boolean soloNumeros() {

        int i, j = 0;
        String numero = ""; // Es el número que se comprueba uno a uno por si hay alguna letra entre los 8 primeros dígitos
        String miDNI = ""; // Guardamos en una cadena los números para después calcular la letra
        String[] unoNueve = {"0","1","2","3","4","5","6","7","8","9"};

        for(i = 0; i < this.dni.length() - 1; i++) {
            numero = this.dni.substring(i, i+1);

            for(j = 0; j < unoNueve.length; j++) {
                if(numero.equals(unoNueve[j])) {
                    miDNI += unoNueve[j];
                }
            }
        }

        if(miDNI.length() != 8) {
            return false;
        }
        else {
            return true;
        }
    }

    private String letraDNI() {
        // pasar miNumero a integer
        int miDNI = Integer.parseInt(this.dni.substring(0,8));
        int resto = 0;
        String miLetra = "";
        String[] asignacionLetra = {"T", "R", "W", "A", "G", "M", "Y", "F", "P", "D", "X", "B", "N", "J", "Z", "S", "Q", "V", "H", "L", "C", "K", "E"};

        resto = miDNI % 23;

        miLetra = asignacionLetra[resto];

        return miLetra;
    }
}
