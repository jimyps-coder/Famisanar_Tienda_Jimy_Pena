package com.tienda.ui;

import com.tienda.domain.Producto;
import com.tienda.service.TiendaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Map;

public class TiendaFrame extends JFrame {
    private final TiendaService service;

    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Producto", "Cantidad", "IVA", "Precio", "Pedido"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable tabla = new JTable(model);

    private final JLabel lblIngresos = new JLabel("$ 0");
    private final JLabel lblMasVendido = new JLabel("-");
    private final JLabel lblMenosVendido = new JLabel("-");
    private final JLabel lblPromedio = new JLabel("$ 0");

    public TiendaFrame(TiendaService service) {
        super("Famisanar-Prueba Jimy Peña: Tienda");
        this.service = service;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(650, 600);
        setLocationRelativeTo(null);

        // Panel principal con BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(8, 8));
        setContentPane(mainPanel);

        // Panel central con BoxLayout en vertical
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        centerPanel.add(crearPanelProductos());
        centerPanel.add(crearPanelOperaciones());
        centerPanel.add(crearPanelCalculos());

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        refrescarTablaYCalculos();
    }

    private JPanel crearPanelProductos() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder("Productos"));
        tabla.setRowHeight(24);
        p.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return p;
    }

    private JPanel crearPanelOperaciones() {
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createTitledBorder("Operaciones"));

        JButton btnVender = new JButton("Vender Producto");
        btnVender.addActionListener(e-> venderAccion());

        JButton btnPedir = new JButton("Pedir Producto");
        btnPedir.addActionListener(e -> pedirAccion());

        p.add(btnVender);
        p.add(btnPedir);
        return p;
    }

    private JPanel crearPanelCalculos() {
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createTitledBorder("Cálculos"));
        p.setLayout(new GridLayout(4, 2, 8, 8));

        p.add(new JLabel("Ingresos"));         p.add(lblIngresos);
        p.add(new JLabel("Producto más vendido"));   p.add(lblMasVendido);
        p.add(new JLabel("Producto menos vendido")); p.add(lblMenosVendido);
        p.add(new JLabel("Promedio"));          p.add(lblPromedio);
        return p;
    }

    private void venderAccion() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto en la tabla.");
            return;
        }
        // el ID interno es índice+1 según el seed (o puedes mapear por nombre)
        long productoId = row + 1L;

        String sCant = JOptionPane.showInputDialog(this, "Cantidad a vender:", "1");
        if (sCant == null) return; // cancel
        try {
            int cant = Integer.parseInt(sCant.trim());
            service.vender(productoId, cant);
            refrescarTablaYCalculos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void pedirAccion() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto en la tabla.");
            return;
        }
        long productoId = row + 1L;
        try {
            service.pedir(productoId);
            refrescarTablaYCalculos();
            JOptionPane.showMessageDialog(this, "Pedido registrado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refrescarTablaYCalculos() {
        model.setRowCount(0);
        Map<Long, BigDecimal> precios = service.preciosFinalesPorProducto();
        for (Producto p : service.listarProductos()) {
            String ivaTxt = switch (p.getTipo()) {
                case PAPELERIA -> "16.0%";
                case SUPERMERCADO -> "4.0%";
                case DROGUERIA -> "12.0%";
            };
            String precioTxt = service.formatoMoneda(precios.get(p.getId()));
            String pedidoTxt = p.isPedido() ? "SI" : "NO";
            model.addRow(new Object[]{p.getNombre(), p.getStock(), ivaTxt, precioTxt, pedidoTxt});
        }

        lblIngresos.setText(service.formatoMoneda(service.ingresos()));
        lblPromedio.setText(service.formatoMoneda(service.promedio()));
        lblMasVendido.setText(service.masVendido().map(Producto::getNombre).orElse("-"));
        lblMenosVendido.setText(service.menosVendido().map(Producto::getNombre).orElse("-"));
    }
}
