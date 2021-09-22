package com.zup.propostaservice.carteira;

import com.zup.propostaservice.cartao.Cartao;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
public class Carteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cartao cartao;

    @Enumerated(EnumType.STRING)
    private CarteiraEnum carteira;

    @NotNull
    @Email
    private String email;

    public Carteira() {
    }

    public Carteira(Cartao cartao, CarteiraEnum carteira, String email) {
        this.cartao = cartao;
        this.carteira = carteira;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public CarteiraEnum getCarteira() {
        return carteira;
    }

    public String getEmail() {
        return email;
    }
}
