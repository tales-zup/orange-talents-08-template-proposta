package com.zup.propostaservice.feign.contas;

import java.math.BigDecimal;

public class ParcelasCartaoResponse {

    private Long id;
    private Integer quantidade;
    private BigDecimal valor;

    public Long getId() {
        return id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }
}
