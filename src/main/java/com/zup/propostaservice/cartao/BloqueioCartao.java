package com.zup.propostaservice.cartao;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class BloqueioCartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cartao cartao;

    @NotNull
    private Boolean ativo;

    private String ipCliente;

    private String userAgent;

    @NotNull
    private LocalDateTime dataCadastro;

    public BloqueioCartao() {
    }

    public BloqueioCartao(Cartao cartao, Boolean ativo, String ipCliente, String userAgent) {
        this.cartao = cartao;
        this.ativo = ativo;
        this.ipCliente = ipCliente;
        this.userAgent = userAgent;
        this.dataCadastro = LocalDateTime.now();
    }

    public void liberarBloqueio() {
        this.ativo = false;
    }

    public Long getId() {
        return id;
    }

    public Cartao getCartao() {
        return cartao;
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
