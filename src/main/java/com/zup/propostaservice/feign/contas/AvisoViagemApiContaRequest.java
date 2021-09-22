package com.zup.propostaservice.feign.contas;

import java.time.LocalDate;

public class AvisoViagemApiContaRequest {

    private String destino;

    private LocalDate validoAte;

    public AvisoViagemApiContaRequest(String destino, LocalDate validoAte) {
        this.destino = destino;
        this.validoAte = validoAte;
    }

    public String getDestino() {
        return destino;
    }

    public LocalDate getValidoAte() {
        return validoAte;
    }
}
