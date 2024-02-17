package com.mycompany.checadormaven;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JLabel;

public class Hora extends Thread {

    JLabel l;

    public Hora(JLabel l) {
        this.l = l;
    }

    public void run() {
        while (true) {
            String d = DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mm:ss a")
                    .format(LocalDateTime.now());
            l.setText(d + "");
        }
    }
}
