package com.zup.propostaservice.feign.contas;

public class AvisoViagemApiContaResponse {

    private String resultado;

    public AvisoViagemApiContaResponse(String resultado) {
        this.resultado = resultado;
    }

    public String getResultado() {
        return resultado;
    }
}
