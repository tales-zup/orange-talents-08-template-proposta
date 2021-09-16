package com.zup.propostaservice.feign.analisefinanceira;

public class AnaliseFinanceiraRequest {

    private String documento;
    private String nome;
    private String idProposta;

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public String getIdProposta() {
        return idProposta;
    }
}
