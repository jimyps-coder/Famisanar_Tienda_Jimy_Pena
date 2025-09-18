package com.tienda.domain;

import java.math.BigDecimal;

public class Producto {
    private final long id;
    private final String nombre;
    private final TipoProducto tipo;
    private final BigDecimal precioBase;
    private int stock;
    private final int umbralPedido;
    private int vendidas;         // acumulado de unidades vendidas
    private boolean pedido;       // “SI/NO” en UI

    public Producto(long id, String nombre, TipoProducto tipo,
                    BigDecimal precioBase, int stock, int umbralPedido) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.precioBase = precioBase;
        this.stock = stock;
        this.umbralPedido = umbralPedido;
    }

    public long getId() { return id; }
    public String getNombre() { return nombre; }
    public TipoProducto getTipo() { return tipo; }
    public BigDecimal getPrecioBase() { return precioBase; }
    public int getStock() { return stock; }
    public int getUmbralPedido() { return umbralPedido; }
    public int getVendidas() { return vendidas; }
    public boolean isPedido() { return pedido; }

    public void descontarStock(int cantidad) { this.stock -= cantidad; }
    public void sumarVendidas(int cantidad) { this.vendidas += cantidad; }
    public void setPedido(boolean pedido) { this.pedido = pedido; }
}
