/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carlos_historias_4_5_6_7;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import javax.swing.JOptionPane;
import jdbc.BD;
import launcher.ActoresFrame;
import launcher.PersonalLimpiezaFrame;

/**
 *
 * @author carlos
 */
public class IntroducirUsuarioLimpiezaFrame extends javax.swing.JFrame {

    /**
     * Creates new form IntroducirUsuarioLimpiezaFrame
     */
    public IntroducirUsuarioLimpiezaFrame() {
        initComponents();
        personalLimpiezaFrame=new PersonalLimpiezaFrame();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        tfNombreUsuario = new javax.swing.JTextField();
        bVolver = new javax.swing.JButton();
        bAceptar = new javax.swing.JButton();
        dcFechaActual = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel1.setText("Introduce el nombre de usuario:");

        bVolver.setBackground(new java.awt.Color(100, 100, 100));
        bVolver.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        bVolver.setForeground(new java.awt.Color(255, 255, 255));
        bVolver.setText("Volver");
        bVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bVolverActionPerformed(evt);
            }
        });

        bAceptar.setBackground(new java.awt.Color(43, 110, 232));
        bAceptar.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        bAceptar.setForeground(new java.awt.Color(255, 255, 255));
        bAceptar.setText("Aceptar");
        bAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAceptarActionPerformed(evt);
            }
        });

        dcFechaActual.setDateFormatString("dd/MM/yyyy");

        jLabel2.setText("Fecha actual (para las pruebas):");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(bVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bAceptar))
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tfNombreUsuario))
                    .addComponent(dcFechaActual, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tfNombreUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dcFechaActual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bVolver)
                    .addComponent(bAceptar))
                .addGap(35, 35, 35))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bVolverActionPerformed
        dispose();
        ActoresFrame.main(null);
    }//GEN-LAST:event_bVolverActionPerformed

    private void bAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAceptarActionPerformed
        //Si alguno de los dos campos es vacio lanzamos un dialogo
        if(tfNombreUsuario.getText().isEmpty() || dcFechaActual.getDate()==null){
            JOptionPane.showMessageDialog(null,"Has de introducir un valor en los dos campos", "Campos vacíos",JOptionPane.WARNING_MESSAGE);
            return;
        }


        //Obtenemos la lista de los nombres de usuario de los trabajadores de limpieza guardados en la BD
        List<String> listaNombresUsuarioLimpieza = BD.dameNombresUsuarioTrabajadoresDeLimpieza();
        //Si el nombre de usuario introducido coincide con el nombre de usuario de algun trabajador de limpieza de la BD
        boolean usuarioCorrecto = false;
        String nombreUsuarioIntroducido = tfNombreUsuario.getText();
        for (String nombreUsuario : listaNombresUsuarioLimpieza) {
            if(nombreUsuarioIntroducido.equals(nombreUsuario)){
                usuarioCorrecto=true;
                break;
            }
        }
        
        

        if(usuarioCorrecto){
            //Guardamos el usuario que entro como p limpieza
            this.nombreUsuario = tfNombreUsuario.getText();
            LocalDate fechaActualLD = dcFechaActual.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int dia = fechaActualLD.getDayOfMonth();
            int mes = fechaActualLD.getMonthValue();
            int anio = fechaActualLD.getYear();
            this.fechaActual = new java.sql.Date(anio,mes-1,dia);
            
            System.out.println("IntroducirUsuarioLimpiezaFrame   nombreUsuario: " + nombreUsuario + " fechaActual: "+fechaActual);
            //Lanzamos la ventana del personal de limpieza
            this.setVisible(false);
            personalLimpiezaFrame.setVisible(true);
            personalLimpiezaFrame.setNombreUsuario(nombreUsuario);
            personalLimpiezaFrame.setFechaActual(fechaActual);
        }
        else{
            //Lanzamos un dialogo indicando que el usuario no es correcto
            JOptionPane.showMessageDialog(null,"El usuario introducido no se corresponde con el usuario\n de ningún miembro del personal de limpieza", "Usuario incorrector",JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_bAceptarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(IntroducirUsuarioLimpiezaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IntroducirUsuarioLimpiezaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IntroducirUsuarioLimpiezaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IntroducirUsuarioLimpiezaFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IntroducirUsuarioLimpiezaFrame().setVisible(true);
            }
        });
    }
    
    private PersonalLimpiezaFrame personalLimpiezaFrame;
    private String nombreUsuario;
    private java.sql.Date fechaActual;


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAceptar;
    private javax.swing.JButton bVolver;
    private com.toedter.calendar.JDateChooser dcFechaActual;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField tfNombreUsuario;
    // End of variables declaration//GEN-END:variables
}