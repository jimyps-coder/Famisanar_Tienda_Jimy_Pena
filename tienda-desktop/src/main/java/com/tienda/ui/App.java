package com.tienda.ui;
import com.tienda.service.TiendaService;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TiendaService service = new TiendaService();
            new TiendaFrame(service).setVisible(true);
        });
    }
}
