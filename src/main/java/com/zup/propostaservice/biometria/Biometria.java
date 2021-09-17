package com.zup.propostaservice.biometria;

import com.zup.propostaservice.cartao.Cartao;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Biometria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String fingerprint;

    @ManyToOne
    @NotNull
    private Cartao cartao;

    @NotNull
    private LocalDate dataCadastro;

    public Biometria() {
    }

    public Biometria(String fingerprint, Cartao cartao) {
        this.fingerprint = fingerprint;
        this.cartao = cartao;
        this.dataCadastro = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }
}
