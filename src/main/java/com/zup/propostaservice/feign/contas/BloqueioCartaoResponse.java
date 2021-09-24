package com.zup.propostaservice.feign.contas;

public class BloqueioCartaoResponse {

    private String resultado;

    public BloqueioCartaoResponse(String resultado) {
        this.resultado = resultado;
    }

    public String getResultado() {
        return resultado;
    }
}
