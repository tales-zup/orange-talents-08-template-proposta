package com.zup.propostaservice.avisodeviagem;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AvisoDeViagemDto {

    private Long id;
    private String idCartao;
    private String destino;
    private LocalDate dataTermino;
    private LocalDateTime dataCadastro;
    private String ipCliente;
    private String userAgent;

    public AvisoDeViagemDto(AvisoDeViagem avisoDeViagem) {
        this.id = avisoDeViagem.getId();
        this.idCartao = avisoDeViagem.getCartao().getId();
        this.destino = avisoDeViagem.getDestino();
        this.dataTermino = avisoDeViagem.getDataTermino();
        this.dataCadastro = avisoDeViagem.getDataCadastro();
        this.ipCliente = avisoDeViagem.getIpCliente();
        this.userAgent = avisoDeViagem.getUserAgent();
    }

    public Long getId() {
        return id;
    }

    public String getIdCartao() {
        return idCartao;
    }

    public String getDestino() {
        return destino;
    }

    public LocalDate getDataTermino() {
        return dataTermino;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public String getIpCliente() {
        return ipCliente;
    }

    public String getUserAgent() {
        return userAgent;
    }
}
