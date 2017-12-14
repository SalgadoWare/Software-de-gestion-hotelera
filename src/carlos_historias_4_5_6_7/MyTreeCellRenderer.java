package carlos_historias_4_5_6_7;

import java.awt.Color;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
/**
 * @web http://www.jc-mouse.net/
 * @author Mouse
 */
public class MyTreeCellRenderer extends DefaultTreeCellRenderer{
    
    private final Icon prioridad = new ImageIcon(getClass().getResource("/carlos/iconos/prioridad.png")); 
    private final Icon lc = new ImageIcon(getClass().getResource("/carlos/iconos/cubo.png")); 
    private final Icon lc_prior = new ImageIcon(getClass().getResource("/carlos/iconos/cubo_prior.png")); 
    private final Icon o = new ImageIcon(getClass().getResource("/carlos/iconos/lavadora.png")); 
    private final Icon o_prior = new ImageIcon(getClass().getResource("/carlos/iconos/lavadora_prior.png")); 
    private final Icon bct = new ImageIcon(getClass().getResource("/carlos/iconos/toalla.png")); 
    private final Icon bct_prior = new ImageIcon(getClass().getResource("/carlos/iconos/toalla_prior.png")); 
    
    
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
    boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected,expanded, leaf, row, hasFocus);
        //altura de cada nodo
        //tree.setRowHeight(26);

        //setOpaque(true);     
        //color de texto
        //setForeground( Color.black );
        //if( selected )
        //    setForeground( Color.red );  
        
        //-- Asigna iconos
        // si value es la raiz
        if ( tree.getModel().getRoot().equals( (DefaultMutableTreeNode) value ) ) {
            setIcon(o);
            return this;
        } 
        
        if( ((DefaultMutableTreeNode) value).getUserObject() instanceof TareaLimpieza){
	        DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) value;
	        Object objeto = nodo.getUserObject();
	        TareaLimpieza tarea = (TareaLimpieza) objeto;
	                
	        if(tarea.prioritaria==0){//Si no es prioritaria
	            //Los iconos seran los normales
	            switch(tarea.tipo){
	                case "LC":
	                    setIcon(lc);
	                    break;
	                case "BCT":
	                    setIcon(bct);
	                    break;
	                case "O":
	                    setIcon(o);
	                    break;
	            }
	        }
	        else{//Si es prioritaria
	            //Los iconos seran rojos
	            switch(tarea.tipo){
	                case "LC":
	                    setIcon(lc_prior);
	                    break;
	                case "BCT":
	                    setIcon(bct_prior);
	                    break;
	                case "O":
	                    setIcon(o_prior);
	                    break;
	            }
	        }
        }
        
        return this;
}

}//end:MyTreeCellRenderer