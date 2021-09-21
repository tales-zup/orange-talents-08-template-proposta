package com.zup.propostaservice.cartao;

import com.zup.propostaservice.proposta.Proposta;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Cartao {

    @Id
    private String id;

    private LocalDateTime emitidoEm;

    @NotBlank
    private String titular;

    @NotNull
    private BigDecimal limite;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusCartao statusCartao;

    @ManyToOne
    private Proposta proposta;

    public Cartao() {
    }

    public Cartao(String id, LocalDateTime emitidoEm, String titular, BigDecimal limite, Proposta proposta) {
        this.id = id;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.limite = limite;
        this.statusCartao = StatusCartao.ATIVO;
        this.proposta = proposta;
    }

    public void setProposta(Proposta proposta) {
        this.proposta = proposta;
    }

    public void setStatusCartao(StatusCartao statusCartao) {
        this.statusCartao = statusCartao;
    }

    public String getId() {
        return id;
    }
}
