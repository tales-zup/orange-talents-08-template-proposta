package com.zup.propostaservice.feign.contas;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RenegociacaoCartaoResponse {

    private Long id;
    private Integer quantidade;
    private BigDecimal valor;
    private LocalDateTime dataDeCriacao;

    public Long getId() {
        return id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getDataDeCriacao() {
        return dataDeCriacao;
    }
}
