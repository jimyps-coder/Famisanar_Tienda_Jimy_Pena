package com.tienda.domain;

import java.math.BigDecimal;

public enum TipoProducto {
    PAPELERIA(new BigDecimal("0.16")),
    SUPERMERCADO(new BigDecimal("0.04")),
    DROGUERIA(new BigDecimal("0.12"));

    private final BigDecimal tasaIva;

    TipoProducto(BigDecimal tasaIva) {this.tasaIva = tasaIva;}

    public  BigDecimal tasaIva() {return  tasaIva;}
}
