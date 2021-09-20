package com.zup.propostaservice.cartao;

import java.time.LocalDateTime;

public class BloqueioCartaoDto {

    private Long id;
    private String idCartao;
    private Boolean ativo;
    private String ipCliente;
    private String userAgent;
    private LocalDateTime dataCadastro;

    public BloqueioCartaoDto(BloqueioCartao bloqueioCartao) {
        this.id = bloqueioCartao.getId();
        this.idCartao = bloqueioCartao.getCartao().getId();
        this.ativo = bloqueioCartao.getAtivo();
        this.ipCliente = bloqueioCartao.getIpCliente();
        this.userAgent = bloqueioCartao.getUserAgent();
        this.dataCadastro = bloqueioCartao.getDataCadastro();
    }

    public Long getId() {
        return id;
    }

    public String getIdCartao() {
        return idCartao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public String getIpCliente() {
        return ipCliente;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
}
