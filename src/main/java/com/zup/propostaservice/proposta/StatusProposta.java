package com.zup.propostaservice.proposta;

import com.zup.propostaservice.feign.analisefinanceira.ResultadoSolicitacao;

public enum StatusProposta {

    ELEGIVEL,
    NAO_ELEGIVEL;

    public static StatusProposta converter(ResultadoSolicitacao resultadoSolicitacao) {
        if(resultadoSolicitacao.equals(ResultadoSolicitacao.COM_RESTRICAO))
            return NAO_ELEGIVEL;
        else
            return ELEGIVEL;
    }
}
