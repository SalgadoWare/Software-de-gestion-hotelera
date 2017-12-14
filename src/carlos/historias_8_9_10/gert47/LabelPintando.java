/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carlos.historias_8_9_10.gert47;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author carlos
 */
public class LabelPintando extends javax.swing.JLabel {
    
    public LabelPintando(){
        super();
        color=Color.LIGHT_GRAY;
        x1=0;y1=0;
        x2=0;y2=0;
    }
    
    private int x1,y1,x2,y2;
    private Color color;

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public Color getColor() {
        return color;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    
    public void setX1(int x1) {
        this.x1 = x1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }
    
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
        
        g.setColor(color);
        g.drawRect((x1<=x2)? x1 : x2, (y1<=y2)? y1 : y2, (x1<=x2)? x2-x1 : x1-x2, (y1<=y2)? y2-y1 : y1-y2);
        
        //Traza
        System.out.println("Paint");
    }
    
    public void borra(){
        x1=0;y1=0;
        x2=0;y2=0;
    }
    
}
