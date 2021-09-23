package com.zup.propostaservice.avisodeviagem;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zup.propostaservice.cartao.Cartao;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class AvisoDeViagemRequest {

    @NotBlank
    private String destino;

    @NotNull
    @Future
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataTermino;

    public AvisoDeViagemRequest(String destino, LocalDate dataTermino) {
        this.destino = destino;
        this.dataTermino = dataTermino;
    }

    public String getDestino() {
        return destino;
    }

    public LocalDate getDataTermino() {
        return dataTermino;
    }

    public AvisoDeViagem toModel(Cartao cartao, String ipCliente, String userAgent) {
        return new AvisoDeViagem(cartao, destino, dataTermino, ipCliente, userAgent);
    }
}

