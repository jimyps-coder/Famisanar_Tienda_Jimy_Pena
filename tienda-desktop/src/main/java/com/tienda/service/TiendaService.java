package com.tienda.service;

import com.tienda.domain.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TiendaService {
    private final List<Producto> productos = new ArrayList<>();
    private final List<VentaItem> ventas = new ArrayList<>();
    private final NumberFormat money = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

    public TiendaService() {
        // Productos semilla (puedes ajustar cantidades y precios base)
        productos.add(new Producto(1, "lápiz",     TipoProducto.PAPELERIA,  new BigDecimal("1500.00"), 18, 15));
        productos.add(new Producto(2, "aspirina",  TipoProducto.DROGUERIA,  new BigDecimal("30000.00"), 25, 20));
        productos.add(new Producto(3, "borrador",  TipoProducto.PAPELERIA,  new BigDecimal("100.00"),30, 25));
        productos.add(new Producto(4, "pan",       TipoProducto.SUPERMERCADO,new BigDecimal("500.00"),15, 15));
        // Marca pedido si ya está en/por debajo del umbral
        productos.forEach(p -> p.setPedido(requierePedido(p)));
    }

    // ====== Cálculos ======
    public BigDecimal precioFinalUnit(Producto p) {
        BigDecimal iva = p.getTipo().tasaIva();
        return p.getPrecioBase().multiply(BigDecimal.ONE.add(iva)).setScale(2, RoundingMode.HALF_UP);
    }

    public boolean requierePedido(Producto p) {
        return p.getStock() <= p.getUmbralPedido();
    }

    // ====== Operaciones ======
    public void vender(long productoId, int cantidad) {
        if (cantidad <= 0) throw new IllegalArgumentException("Cantidad debe ser > 0");
        Producto p = buscarProducto(productoId);
        if (p.getStock() < cantidad) throw new IllegalStateException("Stock insuficiente");
        BigDecimal pf = precioFinalUnit(p);
        p.descontarStock(cantidad);
        p.sumarVendidas(cantidad);
        ventas.add(new VentaItem(p.getId(), cantidad, pf));
        p.setPedido(requierePedido(p));
    }

    public void pedir(long productoId) {
        Producto p = buscarProducto(productoId);
        if (!requierePedido(p))
            throw new IllegalStateException("El producto no requiere pedido (stock por encima del umbral)");
        p.setPedido(true); // Registro simple del pedido (marcado SI). No modifica stock.
    }

    // ====== Estadísticas ======
    public BigDecimal ingresos() {
        return ventas.stream()
                .map(VentaItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public int totalUnidadesVendidas() {
        return ventas.stream().mapToInt(VentaItem::getCantidad).sum();
    }

    public BigDecimal promedio() {
        int unidades = totalUnidadesVendidas();
        return unidades == 0 ? BigDecimal.ZERO :
                ingresos().divide(BigDecimal.valueOf(unidades), 2, RoundingMode.HALF_UP);
    }

    public Optional<Producto> masVendido() {
        return productos.stream().max(Comparator.comparingInt(Producto::getVendidas));
    }

    public Optional<Producto> menosVendido() {
        return productos.stream().min(Comparator.comparingInt(Producto::getVendidas));
    }

    // ====== Acceso UI ======
    public List<Producto> listarProductos() {
        // copia inmutable para la UI
        return Collections.unmodifiableList(productos);
    }

    public Producto buscarProducto(long id) {
        return productos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado: " + id));
    }

    public String formatoMoneda(BigDecimal valor) {
        return money.format(valor);
    }

    public Map<Long, BigDecimal> preciosFinalesPorProducto() {
        return productos.stream()
                .collect(Collectors.toMap(Producto::getId, this::precioFinalUnit));
    }
}
