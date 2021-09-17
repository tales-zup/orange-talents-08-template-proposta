package com.zup.propostaservice.feign.contas;

import java.time.LocalDateTime;

public class CarteiraCartaoResponse {

    private Long id;
    private String email;
    private LocalDateTime associadaEm;
    private String emissor;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getAssociadaEm() {
        return associadaEm;
    }

    public String getEmissor() {
        return emissor;
    }
}
