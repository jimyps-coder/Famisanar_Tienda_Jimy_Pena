package com.tienda.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VentaItem {
    private final long productoId;
    private final int cantidad;
    private final BigDecimal precioFinalUnit;
    private final BigDecimal subtotal;
    private final LocalDateTime fecha;

    public VentaItem(long productoId, int cantidad, BigDecimal precioFinalUnit) {
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioFinalUnit = precioFinalUnit;
        this.subtotal = precioFinalUnit.multiply(BigDecimal.valueOf(cantidad));
        this.fecha = LocalDateTime.now();
    }

    public long getProductoId() { return productoId; }
    public int getCantidad() { return cantidad; }
    public BigDecimal getPrecioFinalUnit() { return precioFinalUnit; }
    public BigDecimal getSubtotal() { return subtotal; }
    public LocalDateTime getFecha() { return fecha; }
}
