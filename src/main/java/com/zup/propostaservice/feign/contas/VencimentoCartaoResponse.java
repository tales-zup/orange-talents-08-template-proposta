package com.zup.propostaservice.feign.contas;

import java.time.LocalDateTime;

public class VencimentoCartaoResponse {

    private Long id;
    private Integer dia;
    private LocalDateTime dataDeCriacao;

    public Long getId() {
        return id;
    }

    public Integer getDia() {
        return dia;
    }

    public LocalDateTime getDataDeCriacao() {
        return dataDeCriacao;
    }
}
