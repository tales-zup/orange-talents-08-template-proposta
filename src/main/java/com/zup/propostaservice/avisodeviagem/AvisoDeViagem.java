package com.zup.propostaservice.avisodeviagem;

import com.zup.propostaservice.cartao.Cartao;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class AvisoDeViagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cartao cartao;

    @NotBlank
    private String destino;

    @NotNull
    @Future
    private LocalDate dataTermino;

    @NotNull
    private LocalDateTime dataCadastro;

    private String ipCliente;

    private String userAgent;

    public AvisoDeViagem() {
    }

    public AvisoDeViagem(Cartao cartao, String destino, LocalDate dataTermino, String ipCliente, String userAgent) {
        this.cartao = cartao;
        this.destino = destino;
        this.dataTermino = dataTermino;
        this.ipCliente = ipCliente;
        this.userAgent = userAgent;
        this.dataCadastro = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Cartao getCartao() {
        return cartao;
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
