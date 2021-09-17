package com.zup.propostaservice.feign.contas;

import java.time.LocalDateTime;

public class AvisoCartaoResponse {

    private LocalDateTime validoAte;
    private String destino;

    public LocalDateTime getValidoAte() {
        return validoAte;
    }

    public String getDestino() {
        return destino;
    }
}
