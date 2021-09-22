package com.zup.propostaservice.feign.contas;

import com.zup.propostaservice.carteira.CarteiraEnum;

public class CadastroCarteiraApiContaRequest {

    private String email;

    private CarteiraEnum carteira;

    public CadastroCarteiraApiContaRequest(String email, CarteiraEnum carteira) {
        this.email = email;
        this.carteira = carteira;
    }

    public String getEmail() {
        return email;
    }

    public CarteiraEnum getCarteira() {
        return carteira;
    }
}
