package com.mycompany.checadormaven;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author zapat
 */
public class Mensaje extends JFrame{
    public Mensaje(String h){
            JLabel registrado = new JLabel("Se ha registrado la asistencia\n");
            JLabel hora = new JLabel(h);
            
            hora.setFont(new Font("Arial", Font.BOLD, 14));
            registrado.setFont(new Font("Arial", Font.BOLD, 14));
            setTitle("Mensaje");
            setBounds(new Rectangle(400, 200));
            registrado.setBounds(110, 60, 250, 30);
            hora.setBounds(155, 10, 400,200);
            setUndecorated(true);
            setLocationRelativeTo(null);
            add(registrado);
            add(hora);
            setLayout(null);
            getContentPane().setBackground(Color.white);
    }
    
    
}
