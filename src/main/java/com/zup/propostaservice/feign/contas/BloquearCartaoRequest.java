package com.zup.propostaservice.feign.contas;

public class BloquearCartaoRequest {

    private String sistemaResponsavel;

    public BloquearCartaoRequest(String sistemaResponsavel) {
        this.sistemaResponsavel = sistemaResponsavel;
    }

    public String getSistemaResponsavel() {
        return sistemaResponsavel;
    }
}
